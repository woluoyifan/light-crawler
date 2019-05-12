package com.luoyifan.lightcrawler.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EvanLuo
 * @date 2019/5/11 13:49
 */
@Data
public class Page {
    private byte[] content;
    private int code;
    private Seed seed;
    private List<Seed> nextList = new ArrayList<>();

    public Page() {

    }

    public Page(byte[] content) {
        this.content = content;
    }

    public void next(Seed seed) {
        nextList.add(seed);
    }

    public void next(List<Seed> seedList) {
        nextList.addAll(seedList);
    }

}
