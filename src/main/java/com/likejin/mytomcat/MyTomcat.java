package com.likejin.mytomcat;

import com.likejin.mytomcat.handler.RequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

/**
 * @Author 李柯锦
 * @Date 2023/5/23 21:40
 * @Description tomcat的主程序，设置端口号，初始化容器，运行
 */
public class MyTomcat {
    //设置端口号
    public static int port = 8080;
    //设置servlet-name和servlet-class的映射
    public static HashMap<String,Object> nameClassMapping = new HashMap<>();
    //设置servlet-name和url-pattern的映射
    public static HashMap<String,String> namePatternMapping = new HashMap();
    
    
    /*
     * @Description 主运行程序
     * @param args
     * @return void
     **/
    public static void main(String[] args) {
        MyTomcat myTomcat = new MyTomcat();
        myTomcat.init();
        myTomcat.run();
    }
    
    /*
     * @Description 初始化两个hashmap容器
     * @param 
     * @return void
     **/
    private void init() {
        InputStream is = null;
        try {
            is = MyTomcat.class.getClassLoader().getResourceAsStream("web.properties");
            Properties properties = new Properties();
            properties.load(is);
            String servletNameClass = properties.getProperty("servlet-nameclass");
            String servletClassName = properties.getProperty("servlet-class");
            Object servletClass = Class.forName(servletClassName).newInstance();
            String servletNameUrl = properties.getProperty("servlet-nameurl");
            String urlPattern = properties.getProperty("url-pattern");
            nameClassMapping.put(servletNameClass,servletClass);
            namePatternMapping.put(urlPattern,servletNameUrl);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    

    /*
     * @Description 用socket和多线程来处理请求
     * @param 
     * @return void
     **/
    public void run(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("服务器启动:" + port);
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(socket);
                new Thread(requestHandler).start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    


}
