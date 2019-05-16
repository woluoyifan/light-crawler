package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.model.Seed;

import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/16 15:01
 */
public interface Dispatcher {
    void init(List<Seed> seedList, CrawlerConfig config);

    void dispatch();
}
