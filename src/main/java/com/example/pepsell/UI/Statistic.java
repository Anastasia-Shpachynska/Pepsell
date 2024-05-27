package com.example.pepsell.UI;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Statistic {
    private long messageId;
    private UUID clientId;
    private String message;
    private String status;
    private long deliveredTime;

    public Statistic(long messageId, UUID clientId, String message, String status, long deliveredTime) {
        this.messageId = messageId;
        this.clientId = clientId;
        this.message = message;
        this.status = status;
        this.deliveredTime = deliveredTime;
    }
}
