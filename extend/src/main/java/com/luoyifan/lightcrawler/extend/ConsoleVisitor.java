package com.luoyifan.lightcrawler.extend;

import com.luoyifan.lightcrawler.core.Page;
import com.luoyifan.lightcrawler.core.Visitor;

/**
 * @author EvanLuo
 * @date 2019/5/11 14:12
 */
public class ConsoleVisitor implements Visitor {
    @Override
    public void visit(Page page) throws Exception {
        byte[] content = page.getContent();
        System.out.println(new String(content));
    }
}
