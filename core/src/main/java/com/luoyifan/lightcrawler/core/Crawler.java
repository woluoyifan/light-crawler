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
    private List<Seed> seedList = new ArrayList<>();
    private CrawlerConfig config = new CrawlerConfig();

    public Crawler add(Seed seed) {
        if (seed != null && seed.getUrl() != null && seed.getUrl().length() > 0) {
            seedList.add(seed);
        }
        return this;
    }

    public Crawler add(List<Seed> seedList) {
        seedList.forEach(this::add);
        return this;
    }

    public Crawler requester(Requester requester) {
        this.requester = requester;
        return this;
    }

    public Crawler visitor(Visitor visitor) {
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

    public void start() {
        Dispatcher dispatcher = new Dispatcher(this.requester, this.visitor);
        dispatcher.init(seedList, config);
        dispatcher.dispatch();
    }
}
