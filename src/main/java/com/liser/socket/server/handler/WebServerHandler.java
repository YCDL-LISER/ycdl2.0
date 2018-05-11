package com.liser.socket.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;

public class WebServerHandler extends SimpleChannelInboundHandler<Object> {

    private final String webSocketURL;
    private WebSocketServerHandshaker handshaker;
    private final String url_encoding = "UTF-8";

    public WebServerHandler(String webSocketURL) {
        this.webSocketURL = webSocketURL;
    }

    protected void messageReceived(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            // http请求
            handleWebSocketOrHttp(context, (FullHttpRequest) msg);

        } else if (msg instanceof WebSocketFrame) {
            // websocket请求
            /*ChannelPipeline pipeline = context.pipeline();
            pipeline.addLast("webSocketHandler", new WebSocketHandler());
            context.fireChannelRead(((WebSocketFrame) msg).retain());*/

            //context.pipeline().remove(HttpHandler.class);
            context.fireChannelRead(((WebSocketFrame) msg).retain());
        }

    }

    private void handleWebSocketOrHttp(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(httpRequest.getUri()).build();
        String url = uriComponents.getPath();
        url = URLDecoder.decode(url, url_encoding);
        System.out.println("请求路径：" + url);

        // websocket第一次请握手请求
        if (url.equals(webSocketURL)) {
            //upgradeCommunicationProtocol(ctx, httpRequest);

            ctx.pipeline().remove(HttpHandler.class);
            ctx.fireChannelRead(httpRequest.retain());

        } else {

            ctx.pipeline().remove(WebSocketServerProtocolHandler.class);
            ctx.pipeline().remove(WebSocketHandler.class);

            ctx.fireChannelRead(httpRequest.retain());
        }

    }

    private void upgradeCommunicationProtocol(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://"
                + httpRequest.headers().get(HttpHeaders.Names.HOST), null, false);
        handshaker = wsFactory.newHandshaker(httpRequest);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), httpRequest);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Web服务适配器异常发生");
        ctx.close();
    }

}

