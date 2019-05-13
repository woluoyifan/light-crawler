package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.model.Page;
import com.luoyifan.lightcrawler.core.model.Seed;
import com.luoyifan.lightcrawler.core.repository.PageRepository;
import com.luoyifan.lightcrawler.core.repository.ResourceRepository;
import com.luoyifan.lightcrawler.core.repository.SeedRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author EvanLuo
 * @date 2019/5/11 13:36
 */
@Slf4j
public class Dispatcher {
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
     * 看门狗间隔
     */
    private long watchInterval = 1000L;
    /**
     * 执行器自旋间隔
     */
    private long spinInterval = 10L;
    /**
     * 请求器
     */
    private Requester requester;
    /**
     * 结果解析器
     */
    private Visitor visitor;
    /**
     * 爬取配置
     */
    private CrawlerConfig config;

    private ResourceRepository<Seed> seedRepository;
    private ResourceRepository<Page> pageRepository;
    /**
     * 种子源
     */
    private List<Seed> seedList;

    public Dispatcher(Requester requester, Visitor visitor) {
        this.requester = requester;
        this.visitor = visitor;
    }

    public void init(List<Seed> seedList, CrawlerConfig config) {
        if (seedList == null || seedList.isEmpty()) {
            return;
        }
        this.config = config;
        if (this.visitor == null) {
            throw new RuntimeException("visitor not set");
        }
        if (this.requester == null) {
            throw new RuntimeException("requester not set");
        }
        this.seedList = new ArrayList<>(seedList);
        this.seedRepository = new SeedRepository();
        this.seedRepository.addAll(seedList);
        this.pageRepository = new PageRepository();
        if (this.requestThreadPool == null) {
            this.requestThreadPool = initRequestThreadPool(config.getThread());
        }
        this.dispatchThreadPool = initDispatchThreadPool();
        this.counter = new AtomicInteger(seedRepository.size());
    }

    public void dispatch() {
        log.info("start...");
        if (seedList == null || seedList.isEmpty()) {
            return;
        }
        dispatchThreadPool.execute(this::handleSeed);
        dispatchThreadPool.execute(this::handlePage);
        watch();
        requestThreadPool.shutdown();
        dispatchThreadPool.shutdown();
    }

    protected void handleSeed() {
        while (true) {
            if (counter.get() == 0) {
                return;
            }
            if (seedRepository.isEmpty()) {
                sleep(spinInterval);
                continue;
            }
            Seed seed = seedRepository.remove(0);
            requestThreadPool.execute(() -> {
                long requestInterval = config.getRequestInterval();
                if (requestInterval > 0) {
                    synchronized (Dispatcher.class) {
                        sleep(requestInterval);
                    }
                }
                seed.increaseExecuteCount();
                seed.setExecuteTime(LocalDateTime.now());
                String url = seed.getUrl();
                try {
                    Page page = requester.request(seed);
                    if (page == null) {
                        return;
                    }
                    page.setSeed(seed);
                    pageRepository.add(page);
                    List<Seed> nextList = page.getNextList();
                    if (nextList != null) {
                        seedList.addAll(nextList);
                        seedRepository.addAll(seedList);
                    }
                    log.info("request success,url:{}", url);
                } catch (IOException e) {
                    log.error("request fail,url:{}", url, e);
                    pushBack(seed);
                }
            });

        }
    }

    protected void handlePage() {
        while (true) {
            if (counter.get() == 0) {
                return;
            }
            if (pageRepository.isEmpty()) {
                sleep(spinInterval);
                continue;
            }
            Page page = pageRepository.remove(0);
            dispatchThreadPool.execute(() -> {
                Seed seed = page.getSeed();
                try {
                    visitor.visit(page);
                    counter.addAndGet(-1);
                    log.info("visitor success,url:{}", seed.getUrl());
                } catch (Exception e) {
                    log.error("visit fail,url:{}", seed.getUrl(), e);
                    pushBack(seed);
                }
            });
        }
    }

    protected void watch() {
        while (true) {
            int count = counter.get();
            int total = seedList.size();
            log.info("{}/{}", total - count, total);
            if (count == 0) {
                return;
            }
            sleep(watchInterval);
        }
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

    protected void pushBack(Seed seed) {
        if (config.isRetry() && config.getMaxExecuteCount() > seed.getExecuteCount()) {
            this.seedRepository.add(seed);
        }
        this.counter.addAndGet(-1);
    }
}
