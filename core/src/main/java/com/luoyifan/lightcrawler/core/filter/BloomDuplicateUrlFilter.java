package com.luoyifan.lightcrawler.core.filter;

import orestes.bloomfilter.BloomFilter;
import orestes.bloomfilter.FilterBuilder;

/**
 * @author EvanLuo
 * @date 2019/5/17 14:42
 */
public class BloomDuplicateUrlFilter implements DuplicateUrlFilter {

    private BloomFilter<String> bloomFilter;

    public BloomDuplicateUrlFilter(int expectedElements) {
        this.bloomFilter = createBloomFilter(expectedElements);
    }

    public static BloomFilter<String> createBloomFilter(int expectedElements) {
        double falsePositiveProbability = 0.5D / expectedElements;
        return new FilterBuilder()
                .falsePositiveProbability(falsePositiveProbability)
                .expectedElements(expectedElements)
                .buildBloomFilter();
    }

    @Override
    public synchronized boolean add(String url) {
        return bloomFilter.add(url);
    }

    @Override
    public synchronized boolean contains(String url) {
        return bloomFilter.contains(url);
    }

    @Override
    public synchronized void clear() {
        bloomFilter.clear();
    }

    @Override
    public synchronized boolean isEmpty() {
        return bloomFilter.isEmpty();
    }

    @Override
    public int size() {
        return bloomFilter.getEstimatedPopulation().intValue();
    }
}
