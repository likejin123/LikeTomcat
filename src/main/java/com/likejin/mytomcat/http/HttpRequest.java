package com.likejin.mytomcat.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author 李柯锦
 * @Date 2023/5/23 21:46
 * @Description 封装http请求
 */
public class HttpRequest {
    //请求方法
    private  String method;
    //请求url
    private  String url;

    /*
     * @Description 构造函数，利用socket创建的输入流初始化成员变量。注意流不能关闭，否则socket断开
     **/
    public HttpRequest(InputStream inputStream) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"GBK"));
        //空格分开请求行
        String[] split = reader.readLine().split(" ");
        setMethod(split[0]);
        setUrl(split[1]);
        String host = reader.readLine();
        System.out.println("主机：" + host + "访问了服务器");
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
