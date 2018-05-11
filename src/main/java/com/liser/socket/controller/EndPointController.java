package com.liser.socket.controller;

import com.alibaba.fastjson.JSON;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.AppMessage;
import com.liser.socket.bean.HolloDomain;
import com.liser.socket.ehcache.HolloMessageForward;
import com.liser.socket.util.ToHexTool;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("ycdl")
public class EndPointController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("message","欢迎光临!");
        return "login";
    }

    @RequestMapping(value = "/command", method = RequestMethod.POST)
    @ResponseBody
    public AppMessage command(HolloDomain holloDomain) {
        AppMessage appMessage = new AppMessage();
        if (ValidateUtil.isEmpty(holloDomain)){
            appMessage.setCode(0);
            appMessage.setInfo("命令格式错误");
            return appMessage;
        }
        String functional_ID = holloDomain.getFunctional_ID();
        int message_body_length = holloDomain.getMessage_body_length();
        String device_ID = holloDomain.getDevice_ID();
        String message_number = holloDomain.getMessage_number();
        String data_page_hex = holloDomain.getData_page_hex();

        if (ValidateUtil.isEmpty(functional_ID)){
            appMessage.setCode(1);
            appMessage.setInfo("功能ID为空");
            return appMessage;
        }
        if (ValidateUtil.isEmpty(message_body_length)){
            appMessage.setCode(2);
            appMessage.setInfo("消息体长度为空");
            return appMessage;
        }
        if (ValidateUtil.isEmpty(device_ID)){
            appMessage.setCode(3);
            appMessage.setInfo("设备ID为空");
            return appMessage;
        }
        if (ValidateUtil.isEmpty(message_number)){
            appMessage.setCode(4);
            appMessage.setInfo("消息流水号为空");
            return appMessage;
        }
        if (ValidateUtil.isEmpty(data_page_hex)){
            appMessage.setCode(5);
            appMessage.setInfo("消息体为空");
            return appMessage;
        }

        byte[] data_page = ToHexTool.hexString2Bytes(data_page_hex);
        holloDomain.setData_page(data_page);

        // 发送控制命令给终端
        boolean success = HolloMessageForward.sendData2Endpoint(holloDomain.getDevice_ID(), holloDomain);
        if (success){
            appMessage.setCode(20);
            appMessage.setInfo("设置成功");
        }else {
            appMessage.setCode(10);
            appMessage.setInfo("设备离线");
        }

        return appMessage;
    }
}
