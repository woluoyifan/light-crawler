package com.luoyifan.lightcrawler.core.processor;

import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/16 15:07
 */
public interface DuplicateUrlFilter {
    void add(String url);

    void addAll(List<String> urlList);

    boolean contains(String url);

    void clear();

    boolean isEmpty();
}
