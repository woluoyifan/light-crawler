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
}
