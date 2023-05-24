package com.likejin.mytomcat;

import com.likejin.mytomcat.handler.RequestHandler;
import com.likejin.mytomcat.servlet.Servlet;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
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
        myTomcat.initXML();
        myTomcat.run();
    }
    
    /*
     * @Description 初始化两个hashmap容器 根据编写的properties
     * @param 
     * @return void
     **/
    private void initProperties() {
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
     * @Description 初始化两个hashmap容器 根据编写的XML（注册多个servlet）
     * @param
     * @return void
     **/
    private void initXML(){
        try {
            //实例化SAXReader对象
            SAXReader reader = new SAXReader();
            //读取web.xml的所有元素
            Document document = reader.read(MyTomcat.class.getClassLoader().getResourceAsStream("web.xml"));
            //获取根标签（servlet和servlet-mapping），放在一个List中
            Element rootelement = document.getRootElement();
            List<Element> elements = rootelement.elements();
            //循环将映射写进map映射里
            for (Element element : elements) {
                if ("servlet".equalsIgnoreCase(element.getName())) {
                    Element servletname = element.element("servlet-name");
                    Element servletclass = element.element("servlet-class");
                    System.out.println("tomcat获取servlet-name-class:" + servletname.getText() + "==>" + servletclass.getText());
                    //需要注意的是servletMapping映射的第二个参数，要通过反射的方式进行实例化
                    nameClassMapping.put(servletname.getText(),
                            (Servlet) Class.forName(servletclass.getText().trim()).newInstance());
                } else if ("servlet-mapping".equalsIgnoreCase(element.getName())) {
                    Element servletname = element.element("servlet-name");
                    Element urlpattern = element.element("url-pattern");
                    System.out.println("tomcat获取servlet-name-url:" +servletname.getText() + "==>" + urlpattern.getText());
                    namePatternMapping.put(urlpattern.getText(), servletname.getText());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
