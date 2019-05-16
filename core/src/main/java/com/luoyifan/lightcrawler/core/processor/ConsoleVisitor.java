package com.luoyifan.lightcrawler.core.processor;

import com.luoyifan.lightcrawler.core.model.Page;

import java.nio.charset.StandardCharsets;

/**
 * @author EvanLuo
 * @date 2019/5/11 14:12
 */
public class ConsoleVisitor implements Visitor {
    @Override
    public void visit(Page page) throws Exception {
        byte[] content = page.getContent();
        System.out.println(new String(content, StandardCharsets.UTF_8));
    }
}
