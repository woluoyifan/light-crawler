package com.luoyifan.lightcrawler.core.queue;

import com.luoyifan.lightcrawler.core.model.Seed;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:17
 */
public class MemorySeedQueue implements ResourceQueue<Seed> {

    private List<Seed> seedList = Collections.synchronizedList(new LinkedList<>());

    @Override
    public List<Seed> getAll() {
        return seedList;
    }

    @Override
    public boolean add(Seed obj) {
        if (obj == null) {
            return false;
        }
        return seedList.add(obj);
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
