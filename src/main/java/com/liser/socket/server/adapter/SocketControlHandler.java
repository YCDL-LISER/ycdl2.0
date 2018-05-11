package com.liser.socket.server.adapter;

import com.liser.common.exception.AppException;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.ControlAnswerDomain;
import com.liser.socket.bean.HolloDomain;
import com.liser.socket.constant.HollooConstant;
import com.liser.socket.dispose.EPControlAnswerDispose;
import com.liser.socket.dispose.HolloDisposeAdapter;
import com.liser.socket.ehcache.HolloCacheFactory;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

import javax.management.relation.Relation;

public class SocketControlHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 接收客户端消息
        HolloDomain holloDomain = null;

        try {
            holloDomain = (HolloDomain) msg;
            // 内存释放
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
            throw new AppException("客户端消息转换为java对象失败!");
        }

        // 功能ID
        String functional_ID = holloDomain.getFunctional_ID();
        // 设备id
        String device_ID = holloDomain.getDevice_ID();
        // 数据包
        byte[] data_page_bytes = holloDomain.getData_page();
        // 校验是否正确
        String success = holloDomain.getSuccess();

        if (functional_ID.equals(HollooConstant.endpoint_common_response)) {
            /* 【0001】终端通用应答 */

        } else if (functional_ID.equals(HollooConstant.remote_call)) {
            /* 【0255】远程点名_上行 */

        } else if (functional_ID.equals(HollooConstant.remote_query)) {
            /* 【0300】远程查询_上行 */

        } else if (functional_ID.equals(HollooConstant.remote_update)) {
            /* 【0502】远程升级_上行 */

        } else if (functional_ID.equals(HollooConstant.remote_control_answer)) {
            /* 【0600】终端远程控制应答_上行 */

            EPControlAnswerDispose epControlAnswerDispose = new EPControlAnswerDispose();
            ControlAnswerDomain controlAnswerDomain = new ControlAnswerDomain();
            if (!ValidateUtil.isEmpty(data_page_bytes) && ValidateUtil.isEmpty(success)) {
                controlAnswerDomain.setDeviceID(holloDomain.getDevice_ID());
                epControlAnswerDispose.dispose(data_page_bytes, controlAnswerDomain);
            }

            // 回复平台终端应答结果
            /*String channel_ID = (String) HolloCacheFactory.getWebSocketCache(device_ID);
            ChannelHandlerContext platformCtx = (ChannelHandlerContext) HolloCacheFactory.getWebSocketCache(channel_ID);*/

            // 平台通用应答
            epControlAnswerDispose.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.remote_textCommand_answer)) {
            /* 【0700】远程文本指令回复_上行 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.up_varnish_transmission)) {
            /* 【8900】上行透传_上行 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else {

        }

        // 释放资源
        ReferenceCountUtil.release(msg);

    }
}
