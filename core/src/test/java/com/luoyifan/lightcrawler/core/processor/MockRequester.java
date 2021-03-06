package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.model.Page;
import com.luoyifan.lightcrawler.core.model.Seed;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author EvanLuo
 * @date 2019/5/16 17:50
 */
public class MockRequester implements Requester {
    private Random random = new Random();

    @Override
    public Page request(Seed seed) throws IOException {
        try {
            Thread.sleep(500 + random.nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Page(seed.getUrl().getBytes(StandardCharsets.UTF_8));
    }
}
