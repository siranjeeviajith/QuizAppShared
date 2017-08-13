package com.fullLearn.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushNotification implements Serializable {

    private String to;
    private NotificationMessage notification;

}
