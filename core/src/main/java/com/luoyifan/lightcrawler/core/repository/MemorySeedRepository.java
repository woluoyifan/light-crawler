package com.luoyifan.lightcrawler.core.repository;

import com.luoyifan.lightcrawler.core.model.Seed;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:17
 */
public class MemorySeedRepository implements ResourceRepository<Seed> {

    private List<Seed> seedList = Collections.synchronizedList(new LinkedList<>());

    @Override
    public List<Seed> getAll() {
        return seedList;
    }

    @Override
    public void add(Seed obj) {
        seedList.add(obj);
    }

    @Override
    public void addAll(List<Seed> objList) {
        seedList.addAll(objList);
    }

    @Override
    public Seed remove(int index) {
        return seedList.remove(index);
    }

    @Override
    public int size() {
        return seedList.size();
    }

    @Override
    public boolean isEmpty() {
        return seedList.isEmpty();
    }
}
