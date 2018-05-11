package com.liser.socket;

import com.liser.socket.server.SpringMVCLoader;
import com.liser.socket.server.SocketServer;
import com.liser.socket.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务入口类
 */
public class MainServer {

    public static void main(String[] args) throws InterruptedException {
        // 读取spring配置文件
        ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"classpath:/app-context.xml"});
        //ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("app-context.xml");
        // 从spring中获取对象
        SocketServer socketServer = appContext.getBean(SocketServer.class);
        //WebSocketServer webSocketServer = appContext.getBean(WebSocketServer.class);
        WebServer webServer = appContext.getBean(WebServer.class);
        new SpringMVCLoader(appContext);
        // 启动服务
        // socketServer.start(8066,webSocketServer);
        socketServer.start(webServer);

    }
}
