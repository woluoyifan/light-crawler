package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.model.Page;
import com.luoyifan.lightcrawler.core.model.Seed;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author EvanLuo
 * @date 2019/5/16 17:50
 */
public class MockRequester implements Requester {
    @Override
    public Page request(Seed seed) throws IOException {
        return new Page(seed.getUrl().getBytes(StandardCharsets.UTF_8));
    }
}
