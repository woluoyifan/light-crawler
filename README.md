# 超轻量级java爬虫脚手架<br><br>

## 一.前言<br>
本项目是受webcollector的启发,通过最简单的方式提供了一个超轻量级的java爬虫开发脚手架<br><br>

## 二.项目依赖<br>
<ul>
    <li>lombok</li>
    <li>slf4j-api</li>
    <li>log4j2</li>
    <li>okhttp(可选)</li>
</ul>

## 三.使用<br>
自定义````Requester````和````Visitor````的接口实现类<br><br>
一个快速开始的例子:<br>
````
    new Crawler()
        //执行请求实现
        .requester(requester)
        //结果解析实现
        .visitor(visitor)
        //执行线程数
        .thread(3)
        //请求间隔,毫秒
        .requestInterval(200L)
        //添加请求种子
        .add(new Seed("http://www.luoyifan.com"))
        //启动
        .start();
````
作为快速开始的例子的一部分,目前在````extend````模块中提供了2个基础的实现类<br>
````OkHttpRequester````和````ConsoleVisitor````<br>
它们分别实现了````Requester````接口和````Visitor````接口<br>
将使用OkHttp直接进行请求并将返回结果打印在控制台中