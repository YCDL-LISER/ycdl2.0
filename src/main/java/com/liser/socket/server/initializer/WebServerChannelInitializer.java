package com.liser.socket.server.initializer;

import com.liser.socket.server.SpringMVCLoader;
import com.liser.socket.server.handler.HttpHandler;
import com.liser.socket.server.handler.WebServerHandler;
import com.liser.socket.server.handler.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


public class WebServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    /*    public WebServerChannelInitializer() throws ServletException {

     *//*MockServletContext servletContext = new MockServletContext();
    	MockServletConfig servletConfig = new MockServletConfig(servletContext);
    	servletConfig.addInitParameter("contextConfigLocation","classpath:/META-INF/spring/root-context.xml");  
        servletContext.addInitParameter("contextConfigLocation","classpath:/META-INF/spring/root-context.xml"); 

        XmlWebApplicationContext wac = new XmlWebApplicationContext();

		wac.setServletContext(servletContext);
		wac.setServletConfig(servletConfig);
        wac.setConfigLocation("classpath:/spring/spring-mvc.xml");
    	wac.refresh();

    	this.dispatcherServlet = new DispatcherServlet(wac);
    	this.dispatcherServlet.init(servletConfig);*//*
	}*/

    @Override
    public void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        //pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast(new HttpObjectAggregator(65536));
        //pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new WebServerHandler("/ycdl/websocket"));
        pipeline.addLast(new HttpHandler(SpringMVCLoader.getDispatcherServlet()));

        pipeline.addLast(new WebSocketServerProtocolHandler("/ycdl/websocket"));
        pipeline.addLast(new WebSocketHandler());


    }

    /*@Configuration
    @EnableWebMvc
    @ComponentScan(basePackages="org.springframework.sandbox.mvc")
    static class WebConfig extends WebMvcConfigurerAdapter {
    }*/

}
