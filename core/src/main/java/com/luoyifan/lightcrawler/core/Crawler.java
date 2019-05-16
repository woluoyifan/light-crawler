package com.luoyifan.lightcrawler.core;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.model.Seed;
import com.luoyifan.lightcrawler.core.processor.DefaultDispatcher;
import com.luoyifan.lightcrawler.core.processor.Dispatcher;
import com.luoyifan.lightcrawler.core.processor.FilterableDispatcher;
import com.luoyifan.lightcrawler.core.processor.Requester;
import com.luoyifan.lightcrawler.core.processor.Visitor;
import lombok.Getter;
import orestes.bloomfilter.FilterBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:12
 */
public class Crawler {
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

    @Getter
    private Dispatcher dispatcher = new DefaultDispatcher();

    /**
     * start the crawler
     */
    public void start() {
        if (!config.isDuplicate() && dispatcher.getClass().equals(DefaultDispatcher.class)) {
            dispatcher = new FilterableDispatcher();
        }
        dispatcher.init(this.seedList, config);
        dispatcher.dispatch();
    }

    /**
     * add url
     *
     * @param url url
     * @return this
     */
    public Crawler add(String url) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null");
        }
        if (url.isEmpty()) {
            throw new IllegalArgumentException("url can not be empty");
        }
        seedList.add(new Seed(url));
        return this;
    }

    /**
     * add urls
     *
     * @param urls url array
     * @return this
     */
    public Crawler add(String... urls) {
        if (urls == null) {
            throw new IllegalArgumentException("urls can not be null");
        }
        for (String url : urls) {
            this.add(url);
        }
        return this;
    }

    /**
     * add seed
     *
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
     *
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
     *
     * @param requester requester
     * @return this
     */
    public Crawler requester(Requester requester) {
        if (requester == null) {
            throw new IllegalArgumentException("requester can not be null");
        }
        this.config.setRequester(requester);
        return this;
    }

    /**
     * @param visitor visitor
     * @return this
     */
    public Crawler visitor(Visitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("visitor can not be null");
        }
        this.config.setVisitor(visitor);
        return this;
    }

    /**
     * thread num
     *
     * @param num thread num
     * @return this
     */
    public Crawler thread(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("thread must >0");
        }
        this.config.setThread(num);
        return this;
    }

    /**
     * request interval,or use <code>200</code> by default
     *
     * @param millisecond interval
     * @return this
     */
    public Crawler requestInterval(long millisecond) {
        if (millisecond < 0) {
            throw new IllegalArgumentException("millisecond must >= 0");
        }
        this.config.setRequestInterval(millisecond);
        return this;
    }

    /**
     * retry,or use <code>true</code> by default
     *
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
     * <p>
     * when the {@link #retry(boolean)}<code> is <code>true</code>
     * or executeCount</code> inside the seed equals <code>maxExecuteCount</code>
     * the seed will not be pushed back to the queue
     *
     * @param count max execute count
     * @return this
     */
    public Crawler maxExecuteCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must > 0");
        }
        this.config.setMaxExecuteCount(count);
        return this;
    }

    /**
     * replace internal dispatcher
     *
     * @param dispatcher dispatcher
     * @return this
     */
    public Crawler dispatcher(Dispatcher dispatcher) {
        if (dispatcher == null) {
            throw new IllegalArgumentException("dispatcher can not be null");
        }
        this.dispatcher = dispatcher;
        return this;
    }

    /**
     * allow duplicate url or <code>true</code> by default
     *
     * @param duplicate duplicate
     * @return this
     */
    public Crawler duplicate(boolean duplicate) {
        this.config.setDuplicate(duplicate);
        return this;
    }

    /**
     * minimum number of seeds using bloom filter
     *
     * @param num num
     * @return this
     */
    public Crawler minimumNumOfSeedsUsingBloomFilter(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("num must >=0");
        }
        this.config.setMinimumNumOfSeedsUsingBloomFilter(num);
        return this;
    }

    /**
     * bloom filter false positive probability
     * {@link FilterBuilder#falsePositiveProbability()}
     *
     * @param probability probability
     * @return this
     */
    public Crawler bloomFilterFalsePositiveProbability(double probability) {
        if (probability <= 0) {
            throw new IllegalArgumentException("probability must >0");
        }
        this.config.setBloomFilterFalsePositiveProbability(probability);
        return this;
    }

    /**
     * replace internal config
     *
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
