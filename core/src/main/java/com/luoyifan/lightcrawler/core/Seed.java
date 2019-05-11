package com.luoyifan.lightcrawler.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EvanLuo
 * @date 2019/5/11 13:37
 */
@Data
public class Seed {
    private String url;
    private int retry;
    private Map<String, String> extraMap;

    public Seed(){}

    public Seed(String url){
        this.url = url;
    }

    public void extra(String key, String value) {
        if (this.extraMap == null) {
            synchronized (this) {
                if (this.extraMap == null) {
                    this.extraMap = new HashMap<>();
                }
            }
        }
        this.extraMap.put(key, value);
    }

    public String extra(String key) {
        return this.extraMap == null ? null : this.extraMap.get(key);
    }
}
