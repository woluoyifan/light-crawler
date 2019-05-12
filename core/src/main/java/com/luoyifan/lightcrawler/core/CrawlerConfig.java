package com.luoyifan.lightcrawler.core;

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

    public CrawlerConfig thread(int num) {
        this.setThread(num);
        return this;
    }

    public CrawlerConfig requestInterval(long millisecond) {
        this.setRequestInterval(millisecond);
        return this;
    }

    public CrawlerConfig retry(boolean retry) {
        this.setRetry(retry);
        return this;
    }

    public CrawlerConfig maxExecuteCount(int count) {
        this.setMaxExecuteCount(count);
        return this;
    }
}
