package com.sumer.safeho.sendNotificationPack;

public class NotificationSender {
    public Data data;
    public String to; //token

    public NotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSender() {

    }
}

