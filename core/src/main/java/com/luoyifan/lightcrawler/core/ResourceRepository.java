package com.luoyifan.lightcrawler.core;

import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:17
 */
public interface ResourceRepository<T> {
    List<T> getAll();

    void add(T obj);

    void addAll(List<T> objList);

    T remove(int index);

    int size();

    boolean isEmpty();

}
