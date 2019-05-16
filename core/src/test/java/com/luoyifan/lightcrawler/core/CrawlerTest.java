package com.luoyifan.lightcrawler.core;

import com.luoyifan.lightcrawler.core.model.Seed;
import com.luoyifan.lightcrawler.core.processor.DeepVisitor;
import com.luoyifan.lightcrawler.core.processor.MockRequester;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author EvanLuo
 * @date 2019/5/16 17:49
 */
public class CrawlerTest {
    private static final List<Seed> DEFAULT_SEED_LIST =
            Stream.of("http://www.luoyifan.com/0",
                    "http://www.luoyifan.com/0",
                    "http://www.luoyifan.com/1",
                    "http://www.luoyifan.com/2",
                    "http://www.luoyifan.com/3",
                    "http://www.luoyifan.com/4",
                    "http://www.luoyifan.com/5")
                    .map(Seed::new)
                    .collect(Collectors.toList());

    @Test
    public void testSimpleThreadCrawler() {
        new Crawler()
                .add(DEFAULT_SEED_LIST)
                .requester(new MockRequester())
                .start();
    }

    @Test
    public void testSingleThreadCrawler() {
        new Crawler()
                .add(DEFAULT_SEED_LIST)
                .requester(new MockRequester())
                .requestInterval(0L)
                .start();
    }

    @Test
    public void testMultiThreadCrawler() {
        new Crawler()
                .add(DEFAULT_SEED_LIST)
                .requester(new MockRequester())
                .requestInterval(0L)
                .thread(3)
                .start();
    }

    @Test
    public void testNotDuplicateUrlCrawler() {
        new Crawler()
                .add(DEFAULT_SEED_LIST)
                .requester(new MockRequester())
                .requestInterval(0L)
                .duplicate(false)
                .start();
    }


    @Test
    public void testDeepCrawler(){
        new Crawler()
                .add(DEFAULT_SEED_LIST)
                .requester(new MockRequester())
                .visitor(new DeepVisitor())
                .start();
    }
}
