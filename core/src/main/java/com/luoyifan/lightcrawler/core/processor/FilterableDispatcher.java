package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.config.CrawlerConfig;
import com.luoyifan.lightcrawler.core.model.Seed;

import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/16 16:42
 */
public class FilterableDispatcher extends DefaultDispatcher {

    private DuplicateUrlFilter duplicateUrlFilter;

    @Override
    public void init(List<Seed> seedList, CrawlerConfig config) {
        if (seedList != null && seedList.size() < config.getMinimumNumOfSeedsUsingBloomFilter()) {
            duplicateUrlFilter = new HashSetDuplicateUrlFilter();
        }else{
            //TODO use bloom filter
            throw new UnsupportedOperationException("bloom filter not support");
        }
        super.init(seedList, config);
    }

    @Override
    protected int pushToSeedQueue(Seed seed) {
        if (seed == null) {
            return 0;
        }
        String url = seed.getUrl();
        if(duplicateUrlFilter.contains(url)){
            return 0;
        }
        duplicateUrlFilter.add(url);
        return super.pushToSeedQueue(seed);
    }

}
