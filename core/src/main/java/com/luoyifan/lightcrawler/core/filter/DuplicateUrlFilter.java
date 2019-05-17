package com.luoyifan.lightcrawler.core.filter;

/**
 * @author EvanLuo
 * @date 2019/5/16 15:07
 */
public interface DuplicateUrlFilter {
    boolean add(String url);

    boolean contains(String url);

    void clear();

    boolean isEmpty();

    int size();
}
