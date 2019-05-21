package com.luoyifan.lightcrawler.core.queue;

import com.luoyifan.lightcrawler.core.model.Page;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 23:20
 */
public class MemoryPageQueue implements ResourceQueue<Page> {

    private List<Page> pageList = Collections.synchronizedList(new LinkedList<>());

    @Override
    public List<Page> getAll() {
        return pageList;
    }

    @Override
    public boolean add(Page obj) {
        if (obj == null) {
            return false;
        }
        return pageList.add(obj);
    }

    @Override
    public Page remove(int index) {
        return pageList.remove(index);
    }

    @Override
    public int size() {
        return pageList.size();
    }

    @Override
    public boolean isEmpty() {
        return pageList.isEmpty();
    }
}
