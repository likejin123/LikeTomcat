package com.likejin.mytomcat.servlet.impl;

import com.likejin.mytomcat.http.HttpRequest;
import com.likejin.mytomcat.http.HttpResponse;
import com.likejin.mytomcat.servlet.Servlet;

/**
 * @Author 李柯锦
 * @Date 2023/5/23 21:59
 * @Description
 */
public class UserServlet implements Servlet {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        doPost(request,response);
    }

    /*
     * @Description 返回处理结果
     * @param request
     * @param response
     * @return void
     **/
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        try {
            response.send("你好啊，访问了/user");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
