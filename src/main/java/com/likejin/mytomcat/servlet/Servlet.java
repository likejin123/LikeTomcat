package com.likejin.mytomcat.servlet;

import com.likejin.mytomcat.http.HttpRequest;
import com.likejin.mytomcat.http.HttpResponse;

/**
 * @Author 李柯锦
 * @Date 2023/5/23 21:45
 * @Description servlet接口
 */
public interface Servlet {

    default public void init(){};

    default public void service(HttpRequest request, HttpResponse response){
        if(request.getMethod().equals("get")){
            doGet( request,  response);
        }else {
            doPost( request,  response);
        }
    };

    public void doGet(HttpRequest request, HttpResponse response);
    public void doPost(HttpRequest request, HttpResponse response);

    default public void destroy(){};

}
