package com.luoyifan.lightcrawler.core.filter;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * @author EvanLuo
 * @date 2019/5/17 17:43
 */
@Slf4j
public class BloomUrlFilterTest {
    @Test
    public void test() {
        BloomDuplicateUrlFilter bloomDuplicateUrlFilter = new BloomDuplicateUrlFilter(10000);
        IntStream.range(0,10000)
                .mapToObj(String::valueOf)
                .forEach(i->{
                    if(!bloomDuplicateUrlFilter.add(i)){
                        log.warn("duplicate key: {}",i);
                    }
                });
        log.info("estimated population: {}",bloomDuplicateUrlFilter.size());
    }
}
