package com.luoyifan.lightcrawler.core;

import java.io.IOException;

/**
 * @author EvanLuo
 * @date 2019/5/11 13:50
 */
public interface Requester {
    Page request(Seed seed) throws IOException;
}
