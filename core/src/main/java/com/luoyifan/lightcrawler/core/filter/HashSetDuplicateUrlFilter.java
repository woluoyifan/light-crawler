package com.luoyifan.lightcrawler.core.filter;

import java.util.HashSet;

/**
 * @author EvanLuo
 * @date 2019/5/16 15:46
 */
public class HashSetDuplicateUrlFilter implements DuplicateUrlFilter {

    private HashSet<String> store = new HashSet<>();

    @Override
    public synchronized boolean add(String url) {
        return store.add(url);
    }

    @Override
    public synchronized boolean contains(String url) {
        return store.contains(url);
    }

    @Override
    public synchronized void clear() {
        store.clear();
    }

    @Override
    public synchronized boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public int size() {
        return store.size();
    }
}
