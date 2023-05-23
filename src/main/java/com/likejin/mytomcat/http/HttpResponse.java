package com.likejin.mytomcat.http;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @Author 李柯锦
 * @Date 2023/5/23 21:46
 * @Description 封装http响应
 */
public class HttpResponse {
    //http响应报文的响应行和响应头
    public static String responseForm ="HTTP/1.1 200+\r\n" +"Content-Type：text/html+\r\n" +"\r\n";

    public OutputStream outputStream;

    public HttpResponse(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    /*
     * @Description 传入返回的信息，具体执行。抛出异常，中断流导致socket结束
     * @param msg
     * @return void
     **/
    public void send(String msg) throws Exception{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"GBK"));
        String respone = responseForm + msg;
        writer.write(respone);
        writer.flush();
        writer.close();
    }
}
