package com.liser.socket.server.handler;

import com.liser.socket.server.SpringMVCLoader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.handler.stream.ChunkedStream;
import org.apache.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static Logger logger = Logger.getLogger(SpringMVCLoader.class);
    private final DispatcherServlet dispatcherServlet;
    private final String url_encoding = "UTF-8";

    public HttpHandler(DispatcherServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
    }

    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        boolean flag = HttpMethod.POST.equals(fullHttpRequest.getMethod())
                || HttpMethod.GET.equals(fullHttpRequest.getMethod());

        Map<String, String> parammap = getRequestParams(channelHandlerContext, fullHttpRequest);
        logger.info("平台控制命令："+ parammap);

        if (flag) {

            //HTTP请求、GET/POST
            if (channelHandlerContext.channel().isActive()) {
                MockHttpServletRequest servletRequest = new MockHttpServletRequest(
                        SpringMVCLoader.getMvcContext().getServletContext());
                MockHttpServletResponse servletResponse = new MockHttpServletResponse();

                // headers
                for (String name : fullHttpRequest.headers().names()) {
                    for (String value : fullHttpRequest.headers().getAll(name)) {
                        servletRequest.addHeader(name, value);
                    }
                }

                String uri = fullHttpRequest.getUri();
                uri = new String(uri.getBytes("ISO8859-1"), url_encoding);
                uri = URLDecoder.decode(uri, url_encoding);
                UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
                String path = uriComponents.getPath();
                path = URLDecoder.decode(path, url_encoding);
                servletRequest.setRequestURI(path);
                servletRequest.setServletPath(path);
                servletRequest.setMethod(fullHttpRequest.getMethod().name());

                if (uriComponents.getScheme() != null) {
                    servletRequest.setScheme(uriComponents.getScheme());
                }
                if (uriComponents.getHost() != null) {
                    servletRequest.setServerName(uriComponents.getHost());
                }
                if (uriComponents.getPort() != -1) {
                    servletRequest.setServerPort(uriComponents.getPort());
                }

                ByteBuf content = fullHttpRequest.content();
                content.readerIndex(0);
                byte[] data = new byte[content.readableBytes()];
                content.readBytes(data);
                servletRequest.setContent(data);

                try {
                    if (uriComponents.getQuery() != null) {
                        String query = UriUtils.decode(uriComponents.getQuery(),
                                url_encoding);
                        servletRequest.setQueryString(query);
                    }

                    if (parammap != null && parammap.size() > 0) {
                        for (String key : parammap.keySet()) {
                            servletRequest.addParameter(UriUtils.decode(key, url_encoding)
                                    , UriUtils.decode(parammap.get(key) == null ? ""
                                            : parammap.get(key), url_encoding));
                        }
                    }

                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }

                this.dispatcherServlet.service(servletRequest, servletResponse);

                HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
                HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);

                channelHandlerContext.write(response);
                InputStream contentStream = new ByteArrayInputStream(servletResponse.getContentAsByteArray());
                ChannelFuture writeFuture = channelHandlerContext.writeAndFlush(new ChunkedStream(contentStream));
                writeFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private Map<String, String> getRequestParams(ChannelHandlerContext ctx, HttpRequest req) {
        Map<String, String> requestParams = new HashMap<String, String>();

        // 处理get请求
        if (req.getMethod() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
            Map<String, List<String>> parame = decoder.parameters();
            Iterator<Map.Entry<String, List<String>>> iterator = parame.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> next = iterator.next();
                requestParams.put(next.getKey(), next.getValue().get(0));
            }
        }

        // 处理POST请求
        if (req.getMethod() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : postData) {
                if (data.getHttpDataType() == HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    requestParams.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        return requestParams;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Http服务异常发生");
        ctx.close();
    }

}
