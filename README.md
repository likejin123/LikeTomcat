# myTomcat
本文通过propeties文件模拟web.xml来配置servlet的地址映射和类映射，通过socket和多线程模拟tomcat处理http请求报文并且返回http响应报文。

## 1.流程

- mytomcat调用init方法初始化
初始化两个hashmap对应两个地址映射(xml和properties两种格式都可)

### mytomcat调用run方法开启serverSocket监听端口8080
监听到创建socket，并且利用RequestHandler implement runnable并传入socket方法开启多线程

## 2.RequestHandler中

### 初始化HttpRequest(创建socket输入流)
获取method和url
### 初始化HttpResponse(创建socket输出流)
封装结果并发送等待Servlet调用

### 调用对应request的url的servlet(Request,Response)
静态资源默认访问resources下的stufyhttp下的文件

## 3.Servlet中

### 有init，destroy方法(模拟tomcat，本文并未实现功能)

### 有service默认实现确定post还是get方法

### 有doPost和doGet方法供实现类实现

### 实现类UserServlet
调用response封装好的输出流直接返回数据给浏览器端即可。

## 4.testtomcat中

### 首次接触本项目可以先跑通testtomcat的TestTomcat(模拟tomcat处理http请求和http响应)
对照resource下的studyHtml学习


 
