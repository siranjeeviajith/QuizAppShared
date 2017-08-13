package com.fullLearn.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class NotificationMessage implements Serializable {

    private String body;
    private String title;
    private String icon;

}
