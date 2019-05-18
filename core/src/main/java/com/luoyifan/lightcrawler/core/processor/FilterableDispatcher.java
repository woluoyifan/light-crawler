package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.filter.BloomDuplicateUrlFilter;
import com.luoyifan.lightcrawler.core.filter.DuplicateUrlFilter;
import com.luoyifan.lightcrawler.core.filter.HashSetDuplicateUrlFilter;
import com.luoyifan.lightcrawler.core.model.Seed;

import java.util.Collection;

/**
 * @author EvanLuo
 * @date 2019/5/16 16:42
 */
public class FilterableDispatcher extends DefaultDispatcher {

    private DuplicateUrlFilter duplicateUrlFilter;

    @Override
    public void init(Collection<Seed> seedList, CrawlerConfig config) {
        if (config.isUseBloomFilter()) {
            duplicateUrlFilter = new BloomDuplicateUrlFilter(config.getBloomFilterExpectedElements());
        } else {
            duplicateUrlFilter = new HashSetDuplicateUrlFilter();
        }
        super.init(seedList, config);
    }

    @Override
    protected int pushToSeedQueue(Seed seed) {
        if (seed == null) {
            return 0;
        }
        String url = seed.getUrl();
        if (!duplicateUrlFilter.add(url)) {
            return 0;
        }
        return super.pushToSeedQueue(seed);
    }

}
