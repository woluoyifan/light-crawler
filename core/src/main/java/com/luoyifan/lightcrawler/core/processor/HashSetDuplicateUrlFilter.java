package com.luoyifan.lightcrawler.core.processor;

import java.util.HashSet;
import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/16 15:46
 */
public class HashSetDuplicateUrlFilter implements DuplicateUrlFilter {

    private HashSet<String> store = new HashSet<>();

    @Override
    public void add(String url) {
        store.add(url);
    }

    @Override
    public void addAll(List<String> urlList) {
        store.addAll(urlList);
    }

    @Override
    public boolean contains(String url) {
        return store.contains(url);
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public boolean isEmpty() {
        return store.isEmpty();
    }
}
