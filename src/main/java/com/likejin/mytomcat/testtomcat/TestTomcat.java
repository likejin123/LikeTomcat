package com.likejin.mytomcat.testtomcat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author 李柯锦
 * @Date 2023/5/23 22:41
 * @Description 模拟浏览器访问tomcat并且返回响应数据
 */
public class TestTomcat {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(8080);
            System.out.println("服务器启动成功：8080的端口");
            //当服务器没有关闭时
            while(!serverSocket.isClosed()){
                Socket socket = null;
                BufferedReader reader = null;
                BufferedWriter writer = null;
                try {
                    socket = serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //http请求报文
                    String request = null;
                    //http请求报文按行读取
                    while((request = reader.readLine()) != null){
                        //即reader.readLine是阻塞io，buffered有缓冲区
                        //如果缓冲区没有读满，则阻塞等待输入
                        //读取数据到请求体,此时出现空行，结束循环
                        if(request.length() == 0){
                            break;
                        }
                        System.out.println(request);
                    }
                    //http响应报文（postman无法访问，格式不同）
                    String responsebody="HTTP/1.1 200+\r\n"+"Content-Type：text/html+\r\n"+"\r\n";;
                    String response = responsebody + "OK";
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(response);
                    writer.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    reader.close();
                    writer.close();
                    socket.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
