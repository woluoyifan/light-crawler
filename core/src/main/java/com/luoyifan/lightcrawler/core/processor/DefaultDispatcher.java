package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.model.Page;
import com.luoyifan.lightcrawler.core.model.Seed;
import com.luoyifan.lightcrawler.core.queue.MemoryPageQueue;
import com.luoyifan.lightcrawler.core.queue.MemorySeedQueue;
import com.luoyifan.lightcrawler.core.queue.ResourceQueue;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private long spinInterval = 10L;
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
    public void init(List<Seed> seedList, CrawlerConfig config) {
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
        this.seedQueue = initSeedQueue();
        int pushNum = pushToSeedQueue(seedList);
        this.counter = new AtomicInteger(pushNum);
        this.pageQueue = initPageQueue();
        this.requestThreadPool = initRequestThreadPool(config.getThread());
        this.dispatchThreadPool = initDispatchThreadPool();
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
        dispatchThreadPool.execute(this::handleSeed);
        log.info("start page processor");
        dispatchThreadPool.execute(this::handlePage);
        log.info("start watch dog");
        watch();
        log.info("try to shutdown request thread pool");
        requestThreadPool.shutdown();
        log.info("try to shutdown dispatch thread pool");
        dispatchThreadPool.shutdown();
        log.info("stop");
    }

    protected void handleSeed() {
        while (true) {
            if (counter.get() == 0) {
                return;
            }
            if (seedQueue.isEmpty()) {
                sleep(spinInterval);
                continue;
            }
            Seed seed = seedQueue.remove(0);
            requestThreadPool.execute(() -> {
                if (config.getRequestInterval() > 0) {
                    synchronized (DefaultDispatcher.class) {
                        sleep(config.getRequestInterval());
                    }
                }
                seed.increaseExecuteCount();
                seed.setExecuteTime(System.currentTimeMillis());
                String url = seed.getUrl();
                Page page;
                try {
                    page = config.getRequester().request(seed);
                } catch (IOException e) {
                    log.error("request fail,url:{}", url, e);
                    pushBack(seed);
                    return;
                }
                if (page == null) {
                    return;
                }
                page.setSeed(seed);
                pushToPageQueue(page);
                log.info("request success,url:{}", url);
            });

        }
    }

    protected void handlePage() {
        while (true) {
            if (counter.get() == 0) {
                return;
            }
            if (pageQueue.isEmpty()) {
                sleep(spinInterval);
                continue;
            }
            Page page = pageQueue.remove(0);
            dispatchThreadPool.execute(() -> {
                Seed seed = page.getSeed();
                try {
                    config.getVisitor().visit(page);
                } catch (Exception e) {
                    log.error("visit fail,url:{}", seed.getUrl(), e);
                    pushBack(seed);
                    return;
                }
                //handle next seed list
                List<Seed> nextList = page.getNextList();
                if (nextList != null && nextList.size() > 0) {
                    int pushNum = pushToSeedQueue(nextList);
                    counter.addAndGet(pushNum);
                    log.info("push seed num: {}",pushNum);
                }
                //handle seed-page-chain finish ,count--
                counter.addAndGet(-1);
                log.info("visitor success,url:{}", seed.getUrl());
            });
        }
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

    protected void pushBack(Seed seed) {
        if (config.isRetry() && config.getMaxExecuteCount() > seed.getExecuteCount()) {
            pushToSeedQueue(seed);
            return;
        }
        counter.addAndGet(-1);
    }

    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error("sleep error", e);
        }
    }

    protected ExecutorService initRequestThreadPool(int thread) {
        return Executors.newFixedThreadPool(thread);
    }

    protected ExecutorService initDispatchThreadPool() {
        return Executors.newCachedThreadPool();
    }

    protected ResourceQueue<Seed> initSeedQueue() {
        return new MemorySeedQueue();
    }

    protected ResourceQueue<Page> initPageQueue() {
        return new MemoryPageQueue();
    }

    protected int pushToSeedQueue(Seed seed) {
        if (seed != null) {
            seedQueue.add(seed);
            return 1;
        } else {
            return 0;
        }
    }

    protected int pushToSeedQueue(List<Seed> seedList) {
        if (seedList != null && seedList.size() > 0) {
            return seedList.stream()
                    .mapToInt(this::pushToSeedQueue)
                    .sum();
        } else {
            return 0;
        }
    }

    protected int pushToPageQueue(Page page) {
        if (page != null) {
            pageQueue.add(page);
            return 1;
        } else {
            return 0;
        }
    }

    protected int pushToPageQueue(List<Page> pageList) {
        if (pageList != null) {
            return pageList.stream()
                    .mapToInt(this::pushToPageQueue)
                    .sum();
        } else {
            return 0;
        }
    }
}
