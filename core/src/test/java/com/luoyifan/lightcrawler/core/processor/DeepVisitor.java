package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.model.Page;
import com.luoyifan.lightcrawler.core.model.Seed;

import java.util.stream.IntStream;

/**
 * @author EvanLuo
 * @date 2019/5/16 18:07
 */
public class DeepVisitor extends ConsoleVisitor {
    @Override
    public void visit(Page page) throws Exception {
        super.visit(page);
        Seed seed = page.getSeed();
        String deepKey = "deep";
        String deep = seed.extra(deepKey);
        if (deep == null) {
            String url = seed.getUrl();
            IntStream.range(0, 3)
                    .forEach(i -> page.next(new Seed(url + "/" + i).extra(deepKey, deepKey)));
        }
    }
}
