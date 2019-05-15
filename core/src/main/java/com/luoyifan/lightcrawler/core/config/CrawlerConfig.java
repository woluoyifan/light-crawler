package com.luoyifan.lightcrawler.core.config;

import lombok.Data;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:46
 */
@Data
public class CrawlerConfig {
    /**
     * 执行线程数
     */
    private int thread = 1;
    /**
     * 请求间隔
     */
    private long requestInterval = 200L;

    /**
     * 允许重试
     */
    private boolean retry = true;

    /**
     * 最大执行次数
     */
    private long maxExecuteCount = 3;

    /**
     * 使用布隆过滤器的最小种子数
     */
    private long useBloomFilterSeedCount = 1000000;

    /**
     * 布隆过滤器假阳性概率
     */
    private double bloomFilterFalsePositiveProbability = 0.0001D;

    /**
     * thread num
     * @param num num
     * @return this
     */
    public CrawlerConfig thread(int num) {
        this.thread = num;
        return this;
    }

    /**
     * request interval
     * @param millisecond interval
     * @return this
     */
    public CrawlerConfig requestInterval(long millisecond) {
        this.requestInterval = millisecond;
        return this;
    }

    /**
     * allow retry
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
     *
     * when the {@link #retry(boolean)}<code> is <code>true</code>
     * or executeCount</code> inside the seed equals <code>maxExecuteCount</code>
     * the seed will not be pushed back to the queue
     * @param count count
     * @return this
     */
    public CrawlerConfig maxExecuteCount(int count) {
        this.maxExecuteCount = count;
        return this;
    }

    /**
     * if seed's count larger than <code>count</code>,bloom filter will be enabled
     * @param count count
     * @return this
     */
    public CrawlerConfig useBloomFilterSeedCount(int count){
        this.useBloomFilterSeedCount = count;
        return this;
    }

    /**
     * bloom filter false-positive probability
     * @param probability probability
     * @return this
     */
    public CrawlerConfig bloomFilterFalsePositiveProbability(double probability){
        this.bloomFilterFalsePositiveProbability = probability;
        return this;
    }
}
