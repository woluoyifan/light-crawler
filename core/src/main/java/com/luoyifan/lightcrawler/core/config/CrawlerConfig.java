package com.luoyifan.lightcrawler.core.config;

import com.luoyifan.lightcrawler.core.processor.ConsoleVisitor;
import com.luoyifan.lightcrawler.core.processor.Requester;
import com.luoyifan.lightcrawler.core.processor.Visitor;
import lombok.Data;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:46
 */
@Data
public class CrawlerConfig {
    /**
     * requester
     */
    private Requester requester;

    /**
     * visitor
     */
    private Visitor visitor = new ConsoleVisitor();

    /**
     * thread num
     */
    private int thread = 1;

    /**
     * request interval,or use <code>200</code> by default
     */
    private long requestInterval = 200L;

    /**
     * watch interval,or use <code>1000</code> by default
     */
    private long watchInterval = 1000L;

    /**
     * retry,or use <code>true</code> by default
     */
    private boolean retry = true;

    /**
     * max execute count
     * when request or visit throw any exception,the <code>executeCount</code> inside the seed will be increased
     */
    private int maxExecuteCount = 3;

    /**
     * allow duplicate url or <code>true</code> by default
     */
    private boolean duplicate = true;

    /**
     * Use a Bloom filter, note that the Bloom filter has a slight false positive
     */
    private boolean useBloomFilter = false;

    /**
     * The expected number of elements in the Bloom filter
     */
    private int bloomFilterExpectedElements = 1000000;

    /**
     * request
     *
     * @param requester requester
     * @return this
     */
    public CrawlerConfig requester(Requester requester) {
        this.requester = requester;
        return this;
    }

    /**
     * visitor,or use {@link com.luoyifan.lightcrawler.core.processor.ConsoleVisitor} by default
     *
     * @param visitor visitor
     * @return this
     */
    public CrawlerConfig visitor(Visitor visitor) {
        this.visitor = visitor;
        return this;
    }

    /**
     * thread num
     *
     * @param num num
     * @return this
     */
    public CrawlerConfig thread(int num) {
        this.thread = num;
        return this;
    }

    /**
     * request interval
     *
     * @param millisecond interval
     * @return this
     */
    public CrawlerConfig requestInterval(long millisecond) {
        this.requestInterval = millisecond;
        return this;
    }

    /**
     * watch interval
     *
     * @param millisecond interval
     * @return this
     */
    public CrawlerConfig watchInterval(long millisecond) {
        this.watchInterval = millisecond;
        return this;
    }

    /**
     * allow retry
     *
     * @param retry allow
     * @return this
     */
    public CrawlerConfig retry(boolean retry) {
        this.retry = retry;
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
     * @param count count
     * @return this
     */
    public CrawlerConfig maxExecuteCount(int count) {
        this.maxExecuteCount = count;
        return this;
    }

    /**
     * allow duplicate url or <code>true</code> by default
     *
     * @param duplicate allow
     * @return this
     */
    public CrawlerConfig duplicate(boolean duplicate) {
        this.duplicate = duplicate;
        return this;
    }

    /**
     * Use a Bloom filter, note that the Bloom filter has a slight false positive
     * {@link orestes.bloomfilter.BloomFilter}
     * {@link orestes.bloomfilter.memory.BloomFilterMemory}
     *
     * @param use use
     * @return this
     */
    public CrawlerConfig useBloomFilter(boolean use) {
        this.useBloomFilter = use;
        return this;
    }

    /**
     * The expected number of elements in the Bloom filter
     *
     * @param num num
     * @return this
     */
    public CrawlerConfig bloomFilterExpectedElements(int num) {
        this.bloomFilterExpectedElements = num;
        return this;
    }
}
