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
     * 请求器
     */
    private Requester requester;

    /**
     * 解析器
     */
    private Visitor visitor = new ConsoleVisitor();

    /**
     * 执行线程数
     */
    private int thread = 1;

    /**
     * 请求间隔
     */
    private long requestInterval = 200L;

    /**
     * 监听状态报送间隔
     */
    private long watchInterval = 1000L;

    /**
     * 允许重试
     */
    private boolean retry = true;

    /**
     * 最大执行次数
     */
    private int maxExecuteCount = 3;

    /**
     * url可重复
     */
    private boolean duplicate = true;

    /**
     * 使用布隆过滤器的最小种子数
     */
    private int minimumNumOfSeedsUsingBloomFilter = 1000000;

    /**
     * 布隆过滤器假阳性概率
     */
    private double bloomFilterFalsePositiveProbability = 0.0001D;

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
     * @return
     */
    public CrawlerConfig duplicate(boolean duplicate) {
        this.duplicate = duplicate;
        return this;
    }

    /**
     * if seed's count larger than <code>count</code>,bloom filter will be enabled
     *
     * @param num num
     * @return this
     */
    public CrawlerConfig minimumNumOfSeedsUsingBloomFilter(int num) {
        this.minimumNumOfSeedsUsingBloomFilter = num;
        return this;
    }

    /**
     * bloom filter false-positive probability
     *
     * @param probability probability
     * @return this
     */
    public CrawlerConfig bloomFilterFalsePositiveProbability(double probability) {
        this.bloomFilterFalsePositiveProbability = probability;
        return this;
    }
}
