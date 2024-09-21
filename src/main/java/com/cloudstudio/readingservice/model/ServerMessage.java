package com.cloudstudio.readingservice.model;

import lombok.Data;

/**
 * @ClassName ServerSendMessage
 * @Author Create By matrix
 * @Date 2024/9/21 16:08
 */
@Data
public class ServerMessage {
    private String responseMessage;

    public ServerMessage(String message) {
        this.responseMessage = message;
    }
}
