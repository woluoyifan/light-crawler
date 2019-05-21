package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.model.Page;
import com.luoyifan.lightcrawler.core.model.Seed;
import com.luoyifan.lightcrawler.core.queue.FilterableMemorySeedQueue;
import com.luoyifan.lightcrawler.core.queue.MemoryPageQueue;
import com.luoyifan.lightcrawler.core.queue.MemorySeedQueue;
import com.luoyifan.lightcrawler.core.queue.ResourceQueue;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author EvanLuo
 * @date 2019/5/11 13:36
 */
@Slf4j
public class DefaultDispatcher implements Dispatcher {
    /**
     * 请求线程池
     */
    private ExecutorService requestThreadPool;
    /**
     * 解析线程池
     */
    private ExecutorService visitThreadPool;
    /**
     * 调度线程池
     */
    private ExecutorService dispatchThreadPool;
    /**
     * 计数器
     */
    private AtomicInteger counter;
    /**
     * 执行器自旋间隔
     */
    private long spinInterval = 3L;
    /**
     * 爬取配置
     */
    private CrawlerConfig config;

    /**
     * 种子队列
     */
    private ResourceQueue<Seed> seedQueue;

    /**
     * 页面队列
     */
    private ResourceQueue<Page> pageQueue;

    private boolean initialized = false;

    @Override
    public void init(Collection<Seed> seedList, CrawlerConfig config) {
        if (seedList == null) {
            throw new IllegalArgumentException("seedList can not be null");
        }
        if (config == null) {
            throw new IllegalArgumentException("config can not be null");
        }
        if (config.getRequester() == null) {
            throw new IllegalArgumentException("requester not set");
        }
        if (config.getVisitor() == null) {
            throw new IllegalArgumentException("visitor not set");
        }
        this.config = config;
        this.seedQueue = initSeedQueue(config);
        int pushNum = seedList.stream()
                .mapToInt(s -> this.seedQueue.add(s) ? 1 : 0)
                .sum();
        this.counter = new AtomicInteger(pushNum);
        this.pageQueue = initPageQueue(config);
        this.requestThreadPool = initRequestThreadPool(config);
        this.visitThreadPool = initVisitThreadPool(config);
        this.dispatchThreadPool = initDispatchThreadPool(config);
        this.initialized = true;
    }

    @Override
    public void dispatch() {
        log.info("start...");
        if (!initialized) {
            throw new RuntimeException("The init method must be executed first");
        }
        if (seedQueue.isEmpty()) {
            log.info("you should add at least one seed/url");
            log.info("stop");
            return;
        }
        log.info("init size: {}", seedQueue.size());
        log.info("start seed processor");
        dispatchThreadPool.execute(() -> dispatchRequest(config, requestThreadPool, seedQueue, pageQueue));
        log.info("start page processor");
        dispatchThreadPool.execute(() -> dispatchVisit(config, visitThreadPool, seedQueue, pageQueue));
        log.info("start watch dog");
        watch();
        log.info("try to shutdown request thread pool");
        requestThreadPool.shutdown();
        log.info("try to shutdown visit thread pool");
        visitThreadPool.shutdown();
        log.info("try to shutdown dispatch thread pool");
        dispatchThreadPool.shutdown();
        while (true) {
            sleep(1000L);
            if (requestThreadPool.isTerminated()) {
                if (visitThreadPool.isTerminated()) {
                    if (dispatchThreadPool.isTerminated()) {
                        break;
                    }else{
                        log.info("waiting for the dispatch thread pool to close");
                    }
                }else{
                    log.info("waiting for the visit thread pool to close");
                }
            } else {
                log.info("waiting for the request thread pool to close");
            }
        }
        log.info("stop");
    }

    protected void dispatchRequest(CrawlerConfig config, ExecutorService threadPool, ResourceQueue<Seed> seedQueue, ResourceQueue<Page> pageQueue) {
        AtomicInteger flowValve = new AtomicInteger(config.getThread());
        boolean enableRequestInterval = config.getRequestInterval() > 0;
        while (true) {
            if (threadPool.isShutdown()) {
                return;
            }
            if (seedQueue.isEmpty() || flowValve.get() == 0) {
                sleep(spinInterval);
                continue;
            }
            flowValve.decrementAndGet();
            Seed seed = seedQueue.remove(0);
            if (enableRequestInterval) {
                sleep(config.getRequestInterval());
            }
            threadPool.execute(() -> {
                try {
                    Page page = doRequest(config.getRequester(), seed);
                    pageQueue.add(page);
                    log.info("request success,url:{}", seed.getUrl());
                } catch (Exception e) {
                    log.error("request fail,url:{}", seed.getUrl(), e);
                    pushBack(config, seedQueue, seed);
                }
                flowValve.incrementAndGet();
            });
        }
    }

    protected Page doRequest(Requester requester, Seed seed) throws IOException {
        seed.incrementExecuteCount();
        seed.setExecuteTime(System.currentTimeMillis());
        Page page = requester.request(seed);
        if (page == null) {
            return null;
        }
        page.setSeed(seed);
        return page;
    }

    protected void dispatchVisit(CrawlerConfig config, ExecutorService threadPool, ResourceQueue<Seed> seedQueue, ResourceQueue<Page> pageQueue) {
        while (true) {
            if (threadPool.isShutdown()) {
                return;
            }
            if (pageQueue.isEmpty()) {
                sleep(spinInterval);
                continue;
            }
            Page page = pageQueue.remove(0);
            threadPool.execute(() -> {
                Seed seed = page.getSeed();
                try {
                    List<Seed> nextList = doVisit(config.getVisitor(), page);
                    //handle next seed list
                    if (nextList != null && nextList.size() > 0) {
                        int pushNum = nextList.stream()
                                .mapToInt(s -> seedQueue.add(s) ? 1 : 0)
                                .sum();
                        counter.addAndGet(pushNum);
                        log.info("push seed num: {}", pushNum);
                    }
                    //handle seed-page-chain finish ,count--
                    counter.decrementAndGet();
                    log.info("visitor success,url:{}", seed.getUrl());
                } catch (Exception e) {
                    log.error("visit fail,url:{}", seed.getUrl(), e);
                    pushBack(config, seedQueue, seed);
                }
            });
        }
    }

    protected List<Seed> doVisit(Visitor visitor, Page page) throws Exception {
        visitor.visit(page);
        return page.getNextList();
    }

    protected void watch() {
        while (true) {
            int count = counter.get();
            log.info("size: {}", count);
            if (count == 0) {
                return;
            }
            sleep(config.getWatchInterval());
        }
    }

    protected boolean pushBack(CrawlerConfig config, ResourceQueue<Seed> queue, Seed seed) {
        if (config.isRetry() && config.getMaxExecuteCount() > seed.getExecuteCount()) {
            return queue.add(seed);
        }
        return false;
    }

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error("sleep error", e);
        }
    }

    protected ExecutorService initRequestThreadPool(CrawlerConfig config) {
        int thread = config.getThread();
        return new ThreadPoolExecutor(thread, thread,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new RequestThreadFactory());
    }

    protected ExecutorService initVisitThreadPool(CrawlerConfig config) {
        return new ThreadPoolExecutor(config.getThread(), Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new VisitThreadFactory());
    }

    protected ExecutorService initDispatchThreadPool(CrawlerConfig config) {
        int thread = 2;
        return new ThreadPoolExecutor(thread, thread,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new DispatchThreadFactory());
    }

    protected ResourceQueue<Seed> initSeedQueue(CrawlerConfig config) {
        return config.isDuplicate() ? new MemorySeedQueue() : new FilterableMemorySeedQueue(config);
    }

    protected ResourceQueue<Page> initPageQueue(CrawlerConfig config) {
        return new MemoryPageQueue();
    }

    static class RequestThreadFactory extends AbstractThreadFactory {
        RequestThreadFactory() {
            super("request");
        }
    }

    static class VisitThreadFactory extends AbstractThreadFactory {
        VisitThreadFactory() {
            super("visit");
        }
    }

    static class DispatchThreadFactory extends AbstractThreadFactory {
        DispatchThreadFactory() {
            super("dispatch");
        }
    }

    static abstract class AbstractThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(0);
        private final String namePrefix;

        AbstractThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    name +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
