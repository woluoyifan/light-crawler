package com.luoyifan.lightcrawler.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:12
 */
public class Crawler {
    /**
     * 请求器
     */
    @Getter
    private Requester requester;
    /**
     * 结果解析器
     */
    @Getter
    private Visitor visitor;
    /**
     * 种子列表
     */
    @Getter
    private List<Seed> seedList = new ArrayList<>();
    /**
     * 爬虫配置
     */
    @Getter
    private CrawlerConfig config = new CrawlerConfig();

    public void start() {
        Dispatcher dispatcher = new Dispatcher(this.requester, this.visitor);
        dispatcher.init(seedList, config);
        dispatcher.dispatch();
    }

    public Crawler add(Seed seed) {
        if (seed == null) {
            throw new IllegalArgumentException("seed can not be null");
        }
        if (seed.getUrl() == null) {
            throw new IllegalArgumentException("seed's url can not be null");
        }
        if (seed.getUrl().isEmpty()) {
            throw new IllegalArgumentException("seed's url can not be empty");
        }
        seedList.add(seed);
        return this;
    }

    public Crawler add(List<Seed> seedList) {
        if (seedList == null) {
            throw new IllegalArgumentException("seedList can not be null");
        }
        seedList.forEach(this::add);
        return this;
    }

    public Crawler requester(Requester requester) {
        if (requester == null) {
            throw new IllegalArgumentException("requester can not be null");
        }
        this.requester = requester;
        return this;
    }

    public Crawler visitor(Visitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("visitor can not be null");
        }
        this.visitor = visitor;
        return this;
    }

    public Crawler thread(int num) {
        this.config.setThread(num);
        return this;
    }

    public Crawler requestInterval(long millisecond) {
        this.config.setRequestInterval(millisecond);
        return this;
    }

    public Crawler retry(boolean retry) {
        this.config.setRetry(retry);
        return this;
    }

    public Crawler maxExecuteCount(int count) {
        this.config.setMaxExecuteCount(count);
        return this;
    }

    public Crawler config(CrawlerConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config can not be null");
        }
        this.config = config;
        return this;
    }

}
