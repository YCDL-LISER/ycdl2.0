package com.liser.socket.server.adapter;

import com.liser.common.exception.AppException;
import com.liser.common.service.ServiceLocator;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.DeviceLogWithBLOBs;
import com.liser.socket.bean.DeviceWithBLOBs;
import com.liser.socket.bean.LogonResponseDomain;
import com.liser.socket.bean.HolloDomain;
import com.liser.socket.constant.HollooConstant;
import com.liser.socket.constant.WebSocketConstant;
import com.liser.socket.dispose.EPReportDataDispose;
import com.liser.socket.dispose.EPLoginDispose;
import com.liser.socket.dispose.HolloDisposeAdapter;
import com.liser.socket.ehcache.HolloCacheFactory;
import com.liser.socket.service.DeviceService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

import java.util.Date;

// 标示一个ChannelHandler可以被多个Channel安全地共享
@ChannelHandler.Sharable
public class SocketServerHandler extends ChannelHandlerAdapter {

    private Logger logger = Logger.getLogger(SocketServerHandler.class);
    private DeviceService deviceService = (DeviceService) ServiceLocator.getService("deviceService");
    private int loss_connect_time = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("服务激活时间是：" + new Date());

        // 加入缓存
        String channel_ID = ctx.channel().id().asLongText();
        HolloCacheFactory.putSocketCache(channel_ID, ctx);

        // 获取ChannelPipeline
        /*ChannelPipeline pipeline = ctx.pipeline();

        // 表示该方法只是进行了透传，不做任何业务逻辑处理，让channelPipe中的下一个handler处理channelActive方法
        ctx.fireChannelActive(); // 调用后续的channelHandler的同名的方法*/
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        String channel_ID = ctx.channel().id().asLongText();
        String channelSuffix = channel_ID + WebSocketConstant.channelSuffix;

        // 更新数据库设备状态为离线
        String device_ID = (String) HolloCacheFactory.getSocketCache(channelSuffix);
        deviceService.updateDeviceOnline(device_ID,0);

        // 移除缓存
        HolloCacheFactory.removeSocketCache(channel_ID);
        HolloCacheFactory.removeSocketCache(channelSuffix);

        logger.info("服务停止时间是：" + new Date());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                logger.info("20秒没有接收到客户端的信息!currentTime：" + new Date());
                if (loss_connect_time > 1) {
                    logger.info("服务端关闭这个不活跃的channel");
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String channel_ID = ctx.channel().id().asLongText();

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

        // 加入缓存
        // 设备ID存通道ID
        HolloCacheFactory.putSocketCache(device_ID, channel_ID);
        // 通道ID拼接后缀 存设备ID
        HolloCacheFactory.putSocketCache(channel_ID + WebSocketConstant.channelSuffix, device_ID);

        if (functional_ID.equals(HollooConstant.heart_up_head)) {
            /* 【0002】终端心跳上报 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.login_up_head)) {
            /* 【0102】终端登录 */

            byte[] data_page_bytes = holloDomain.getData_page();  // 数据包
            String success = holloDomain.getSuccess(); // 校验是否正确

            // 成功接收客户端发送数据
            if (!ValidateUtil.isEmpty(data_page_bytes) && ValidateUtil.isEmpty(success)) {
                // 终端登录处理
                LogonResponseDomain versionDomain = new LogonResponseDomain();
                EPLoginDispose endPointDispose = new EPLoginDispose(versionDomain);
                DeviceWithBLOBs device = new DeviceWithBLOBs();
                device.setDeviceNumber(holloDomain.getDevice_ID()); // 设备ID
                try {
                    endPointDispose.dispose(data_page_bytes, device);
                } catch (Exception e) {
                    holloDomain.setSuccess("01");
                    logger.error("终端登陆信息保存出错!", e); // 保存出错日志
                    //new AppException("终端登陆信息保存出错!");
                }
                // 平台登陆应答
                endPointDispose.endpointLoginResponse(ctx, holloDomain, versionDomain);
            }

        } else if (functional_ID.equals(HollooConstant.data_up_head) || functional_ID.equals(HollooConstant.alarm_up_head)) {
            /* 【0250】/【0251】终端上报车辆/报警数据(全平台版) */

            byte[] data_page_bytes = holloDomain.getData_page();  // 数据包

            // 设备日志录入
            DeviceLogWithBLOBs deviceLog = new DeviceLogWithBLOBs();
            deviceLog.setDeviceNumber(holloDomain.getDevice_ID()); // 设备ID
            deviceLog.setFunId(functional_ID); // 功能ID
            deviceLog.setMessageId(holloDomain.getMessage_number()); // 消息流水号

            EPReportDataDispose carDataDispose = new EPReportDataDispose();
            String success = holloDomain.getSuccess(); // 校验是否正确
            if (!ValidateUtil.isEmpty(data_page_bytes) && ValidateUtil.isEmpty(success)) {
                carDataDispose.dispose(data_page_bytes, deviceLog);
            }
            // 平台通用应答
            carDataDispose.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.alarm_info_up_head)) {
            /* 【0205】终端上报报警描述信息 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.behavior_up_head)) {
            /* 【0204】终端上报行程数据 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);


        } else if (functional_ID.equals(HollooConstant.base_station2G_up_head)) {
            /* 【0207】终端上报基站定位数据 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.vehicle_trouble_up_head)) {
            /* 【0209】终端上报车辆故障 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.sleep_awaken_data_up_head)) {
            /* 【0212】终端上报睡眠唤醒 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.sleep_info_up_head)) {
            /* 【0213】终端上报睡眠进入 */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else if (functional_ID.equals(HollooConstant.blindspot_page_up)) {
            /* 【0750】盲点车辆数据包上报(全平台版) */

            // 平台通用应答
            HolloDisposeAdapter.systemGeneralResponse(ctx, holloDomain);

        } else {
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决消息冲刷到 远程节点，并且关 闭该 Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
        // .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); // 打印异常 栈跟踪

        // 移除缓存
        String channel_ID = ctx.channel().id().asLongText();
        HolloCacheFactory.removeSocketCache(channel_ID);

        ctx.close(); // 关闭该 Channel
    }
}
