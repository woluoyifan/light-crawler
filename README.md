# 超轻量级java爬虫脚手架<br><br>

## 一.前言<br>
本项目是受webcollector的启发,通过最简单的方式提供了一个超轻量级的java爬虫开发脚手架,支持多线程\url去重\深度爬取\自定义扩展功能<br><br>
当前版本<code>2.0</code><br>

## 二.快速开始<br>
自定义````Requester````的接口实现类<br><br>
一个简单的例子:<br>
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
作为快速开始的例子的一部分,目前提供了2个基础的实现类<br>
位于````core````模块的````OkHttpRequester````,实现了````Requester````接口<br>
位于````extend````模块的````ConsoleVisitor````,实现了````Visitor````接口<br>

## 三.项目说明<br>
项目依赖
<ul>
    <li>lombok</li>
    <li>slf4j-api</li>
    <li>log4j2</li>
    <li>okhttp(可选)</li>
</ul>

已支持功能
<ul>
    <li>多线程</li>
    <li>深度爬取</li>
    <li>爬取间隔</li>
    <li>失败重试</li>
    <li>自定义访问器</li>
    <li>自定义解析器</li>
    <li>基于<code>HashSet</code>或<code>布隆过滤器(bloom filter)</code>的url去重</li>
</ul>

待完成功能
<ul>
    <li>完整的实时统计</li>
    <li>web/restapi控制支持(待定,与[超轻量级]的定位是否符合?)</li>
</ul>

版本历史<br>

<code>2.0</code>提供以下完整的支持
<ul>
    <li>多线程</li>
    <li>深度爬取</li>
    <li>基于<code>HashSet</code>或<code>布隆过滤器(bloom filter)</code>的url去重</li>
    <li>相对丰富的可拓展性</li>
</ul>

<code>1.x</code>
提供了以下基础的支持
<ul>
    <li>多线程</li>
    <li>深度爬取</li>
    <li>一定的可拓展性</li>
</ul>
建议进行开发历史参考和轻量使用使用.小版本API非常不稳定,建议停止使用<br><br>


## 四.结语<br>

脚手架使用过程中如遇到任何问题或无法使用的朋友,请联系我,我会提供一定的技术支持.

QQ: 345300490
WeChat: EvanLowe 
Email: woluoyifan@foxmail.com

如果觉得这个脚手架给你带来了一定的帮助,请帮忙点个赞(star),你的支持是我前进的动力,谢谢!
