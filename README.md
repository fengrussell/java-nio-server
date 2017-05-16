# java-nio-server  
从最早的ServerSokcet到Seletor方法，Tomcat也不断升级自己的Connector。但是nio实现的复杂度较高，选择更为成熟的Netty可以带来更好的稳定性，让研发更专注于业务功能开发。
实现一个基于nio的server一直是自己想做的事情，正好在这个时间点又可以抽出一部分时间重新写代码。  

### 计划  
* 实现一个简单的Server端和Client端，从网上找一些代码修改，熟悉nio的一些实现方法。
* 实现一个简单的HttpServer，可以根据url返回特定的页面。
* 待定。。。（可能考虑性能方面的事情）  

### Code 
* [v1](https://github.com/fengrussell/java-nio-server/tree/v1)  简单实现Server端和Client端
* [v2](https://github.com/fengrussell/java-nio-server/tree/v2)  实现一个简化版HttpServer 
