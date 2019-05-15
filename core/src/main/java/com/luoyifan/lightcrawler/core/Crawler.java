package com.luoyifan.lightcrawler.core;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.model.Seed;
import com.luoyifan.lightcrawler.core.processor.ConsoleVisitor;
import com.luoyifan.lightcrawler.core.processor.Dispatcher;
import com.luoyifan.lightcrawler.core.processor.Requester;
import com.luoyifan.lightcrawler.core.processor.Visitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:12
 */
public class Crawler {
    /**
     * requester
     */
    @Getter
    private Requester requester;
    /**
     * visitor
     */
    @Getter
    private Visitor visitor;
    /**
     * seedList
     */
    @Getter
    private List<Seed> seedList = new ArrayList<>();
    /**
     * config
     */
    @Getter
    private CrawlerConfig config = new CrawlerConfig();

    /**
     * start the crawler
     */
    public void start() {
        if(this.visitor == null){
            this.visitor = new ConsoleVisitor();
        }
        Dispatcher dispatcher = new Dispatcher(this.requester, this.visitor);
        dispatcher.init(this.seedList,config);
        dispatcher.dispatch();
    }

    /**
     * add url
     * @param url url
     * @return this
     */
    public Crawler add(String url){
        if(url == null){
            throw new IllegalArgumentException("url can not be null");
        }
        if(url.isEmpty()){
            throw new IllegalArgumentException("url can not be empty");
        }
        seedList.add(new Seed(url));
        return this;
    }

    /**
     * add urls
     * @param urls url array
     * @return this
     */
    public Crawler add(String...urls){
        if(urls == null){
            throw new IllegalArgumentException("urls can not be null");
        }
        for(String url:urls){
            this.add(url);
        }
        return this;
    }

    /**
     * add seed
     * @param seed seed
     * @return this
     */
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

    /**
     * add seed list
     * @param seedList seed list
     * @return this
     */
    public Crawler add(List<Seed> seedList) {
        if (seedList == null) {
            throw new IllegalArgumentException("seedList can not be null");
        }
        seedList.forEach(this::add);
        return this;
    }

    /**
     * requester,required
     * @param requester requester
     * @return this
     */
    public Crawler requester(Requester requester) {
        if (requester == null) {
            throw new IllegalArgumentException("requester can not be null");
        }
        this.requester = requester;
        return this;
    }

    /**
     * visitor,or use {@link com.luoyifan.lightcrawler.core.processor.ConsoleVisitor} by default
     * @param visitor visitor
     * @return this
     */
    public Crawler visitor(Visitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("visitor can not be null");
        }
        this.visitor = visitor;
        return this;
    }

    /**
     * thread num
     * @param num thread num
     * @return this
     */
    public Crawler thread(int num) {
        this.config.setThread(num);
        return this;
    }

    /**
     * request interval,or use <code>200</code> by default
     * @param millisecond interval
     * @return this
     */
    public Crawler requestInterval(long millisecond) {
        this.config.setRequestInterval(millisecond);
        return this;
    }

    /**
     * retry,or use <code>true</code> by default
     * @param retry allow retry
     * @return this
     */
    public Crawler retry(boolean retry) {
        this.config.setRetry(retry);
        return this;
    }

    /**
     * max execute count
     * when request or visit throw any exception,the <code>executeCount</code> inside the seed will be increased
     *
     * when the {@link #retry(boolean)}<code> is <code>true</code>
     * or executeCount</code> inside the seed equals <code>maxExecuteCount</code>
     * the seed will not be pushed back to the queue
     * @param count max execute count
     * @return this
     */
    public Crawler maxExecuteCount(int count) {
        this.config.setMaxExecuteCount(count);
        return this;
    }

    /**
     * replace internal config
     * @param config config
     * @return this
     */
    public Crawler config(CrawlerConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config can not be null");
        }
        this.config = config;
        return this;
    }

}
