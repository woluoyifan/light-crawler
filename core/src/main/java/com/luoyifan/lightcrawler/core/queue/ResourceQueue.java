package com.luoyifan.lightcrawler.core.queue;

import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:17
 */
public interface ResourceQueue<T> {
    List<T> getAll();

    void add(T obj);

    void addAll(List<T> objList);

    T remove(int index);

    int size();

    boolean isEmpty();

}
