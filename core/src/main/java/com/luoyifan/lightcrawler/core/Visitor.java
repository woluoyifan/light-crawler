package com.luoyifan.lightcrawler.core;

/**
 * @author EvanLuo
 * @date 2019/5/11 13:51
 */
public interface Visitor {
    void visit(Page page) throws Exception;
}
