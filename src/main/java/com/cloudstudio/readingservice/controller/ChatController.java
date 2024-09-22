package com.cloudstudio.readingservice.controller;

import com.cloudstudio.readingservice.model.ClientMessage;
import com.cloudstudio.readingservice.model.ServerMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @ClassName ChatController
 * @Author Create By matrix
 * @Date 2024/9/21 15:34
 */
@Controller
public class ChatController {
    @MessageMapping("/chatPublic")
    @SendTo("/tcpTopic/public")
    public ServerMessage handleMessageFromClient(String message) throws Exception {
        // 这里可以添加一些业务逻辑，比如验证消息、记录日志等
        System.out.println("客户端消息:"+message);
        return new ServerMessage(message);
    }

    @MessageMapping("/clientRequest") // @MessageMapping 和 @RequestMapping 功能类似，浏览器向服务器发起消息，映射到该地址。
    @SendTo("/tcpTopic/getResponse") // 如果服务器接受到了消息，就会对订阅了 @SendTo 括号中的地址的浏览器发送消息。
    public ServerMessage sendMessage(ClientMessage message) throws Exception {
        System.out.println("前(客户)端消息:"+message.getRequestMessage());
        Thread.sleep(1000);//延时1S返回消息
        return new ServerMessage("服务器公告：" + message.getRequestMessage());
    }
}
