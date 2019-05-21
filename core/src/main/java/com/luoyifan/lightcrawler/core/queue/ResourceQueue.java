package com.luoyifan.lightcrawler.core.queue;

import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:17
 */
public interface ResourceQueue<T> {
    List<T> getAll();

    boolean add(T obj);

    T remove(int index);

    int size();

    boolean isEmpty();

}
