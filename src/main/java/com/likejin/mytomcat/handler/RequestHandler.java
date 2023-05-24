package com.likejin.mytomcat.handler;

import com.likejin.mytomcat.MyTomcat;
import com.likejin.mytomcat.http.HttpRequest;
import com.likejin.mytomcat.http.HttpResponse;
import com.likejin.mytomcat.servlet.Servlet;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * @Author 李柯锦
 * @Date 2023/5/23 22:00
 * @Description
 */
public class RequestHandler implements Runnable {

    Socket socket;

    public RequestHandler(Socket socket){
        this.socket = socket;
    }

    /*
     * @Description 利用socket获取request的method和url，然后映射到对应的servlet上处理request
     * @param
     * @return void
     **/
    @Override
    public void run() {
        try {
            HttpRequest httpRequest = new HttpRequest(socket.getInputStream());
            HttpResponse httpResponse = new HttpResponse(socket.getOutputStream());
            HashMap<String, String> namePatternMapping = MyTomcat.namePatternMapping;
            HashMap<String,Object> nameClassMapping = MyTomcat.nameClassMapping;

            //获取对应的url映射到servlet中
            String servletName = namePatternMapping.get(httpRequest.getUrl());
            Servlet servlet = (Servlet)nameClassMapping.get(servletName);

            if(servlet!=null){
                //分发servlet
                servlet.service(httpRequest,httpResponse);

            }else if(httpRequest.getUrl().startsWith("/studyhttp")){
                //静态资源处理逻辑
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"GBK"));
                InputStream is = RequestHandler.class.getClassLoader().getResourceAsStream(httpRequest.getUrl().replaceFirst("/",""));
                if(is != null){
                    //浏览器端的编码为GBK显示，服务器端的txt文件编码为usf-8
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                    String msg = null;
                    while((msg = reader.readLine()) != null){
                        msg = HttpResponse.responseForm + msg;
                        writer.write(msg);
                        writer.flush();
                    }
                    reader.close();
                }else{
                    String msg = HttpResponse.responseForm + "访问的静态资源不存在";
                    writer.write(msg);
                    writer.flush();
                    writer.close();
                }
                is.close();
                writer.close();
            }
            else{
                //无地址
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"GBK"));
                String msg = HttpResponse.responseForm + "访问的servlet不存在";
                writer.write(msg);
                writer.flush();
                writer.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
