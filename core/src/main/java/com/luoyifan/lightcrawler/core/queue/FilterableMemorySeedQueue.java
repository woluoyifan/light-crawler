package com.luoyifan.lightcrawler.core.queue;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.filter.BloomDuplicateUrlFilter;
import com.luoyifan.lightcrawler.core.filter.DuplicateUrlFilter;
import com.luoyifan.lightcrawler.core.filter.HashSetDuplicateUrlFilter;
import com.luoyifan.lightcrawler.core.model.Seed;

/**
 * @author EvanLuo
 * @date 2019/5/21 15:00
 */
public class FilterableMemorySeedQueue extends MemorySeedQueue{
    private DuplicateUrlFilter duplicateUrlFilter;

    public FilterableMemorySeedQueue(CrawlerConfig config){
        if (config.isUseBloomFilter()) {
            duplicateUrlFilter = new BloomDuplicateUrlFilter(config.getBloomFilterExpectedElements());
        } else {
            duplicateUrlFilter = new HashSetDuplicateUrlFilter();
        }
    }

    @Override
    public boolean add(Seed seed) {
        boolean add = duplicateUrlFilter.add(seed.getUrl());
        return add && super.add(seed);
    }
}
