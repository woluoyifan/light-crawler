package com.luoyifan.lightcrawler.core.model;

import lombok.Data;

import java.nio.charset.Charset;
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
    private Charset charset;
    private Seed seed;
    private List<Seed> nextList = new ArrayList<>();

    public Page() {

    }

    public Page(byte[] content) {
        this.content = content;
    }

    /**
     * response content
     * @param content content
     * @return this
     */
    public Page content(byte[] content){
        this.content = content;
        return this;
    }

    /**
     * response code
     * @param code code
     * @return this
     */
    public Page code(int code){
        this.code = code;
        return this;
    }

    /**
     * response charset
     * @param charset response charset
     * @return this
     */
    public Page charset(Charset charset){
        this.charset = charset;
        return this;
    }

    /**
     * append seed
     * @param seed seed
     * @return this
     */
    public Page next(Seed seed) {
        this.nextList.add(seed);
        return this;
    }

    /**
     * append seed
     * @param seedList seedList
     * @return this
     */
    public Page next(List<Seed> seedList) {
        this.nextList.addAll(seedList);
        return this;
    }

}
