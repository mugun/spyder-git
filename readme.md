# 腾讯新闻爬虫工具
本工具为maven项目，需要使用jsoup和html unit   
其中：  
spyderRun.java中是单线程方法。  
APP.java中的是多线程方法，具体需要设置的线程数请在crawl.java中的ThreadPoolExecutor中进行调整  
APP.java需要设置的主参数为:  
baseUrl 此为搜索结果的主要链接，样式如下:  
https://www.sogou.com/sogou?site=news.qq.com&query=%E5%85%BB%E8%80%81%E6%8E%A8%E8%8D%90&pid=sogou-wsse-b58ac8403eb9cf17-0004&duppid=1&idx=f&  
page:此为需要爬的页数，现在只做了从1-page的页数进行爬去，或者以后会做从n-m的页数爬取  
charset:设定字符集，样式如：&ie=utf8  
tempPath:此为用于存放小文件的缓冲文件夹，filewrite的锅所以需要设定绝对路径并且需要保证文件存在  
outputPath：用于存放整合后的结果，同样需要保证文件路径存在
