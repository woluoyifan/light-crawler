package com.luoyifan.lightcrawler.core.model;

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
    private Long createTime = System.currentTimeMillis();
    private int executeCount = 0;
    private Long executeTime;
    private Map<String, String> extraMap = new HashMap<>();

    public Seed() {
    }

    public Seed(String url) {
        this.url = url;
    }

    /**
     * url
     * @return this
     */
    public Seed url(String url){
        this.url = url;
        return this;
    }

    /**
     * extra data
     * @param extraMap data
     * @param replace if<code>true</code>,then the internal <code>extraMap</code> will be replaced,or append
     * @return this
     */
    public Seed extra(Map<String,String> extraMap,boolean replace){
        if(replace){
            this.extraMap = extraMap;
        }else{
            this.extraMap.putAll(extraMap);
        }
        return this;
    }

    /**
     * extra data
     * {@link HashMap}
     *
     * @param key key
     * @param value value
     * @return this
     */
    public Seed extra(String key, String value) {
        this.extraMap.put(key, value);
        return this;
    }

    /**
     * extra data
     * {@link HashMap}
     *
     * @param key key
     * @return value
     */
    public String extra(String key) {
        return this.extraMap.get(key);
    }

    /**
     * increase internal executeCount
     * @return executeCount
     */
    public int incrementExecuteCount() {
        this.executeCount++;
        return this.executeCount;
    }
}
