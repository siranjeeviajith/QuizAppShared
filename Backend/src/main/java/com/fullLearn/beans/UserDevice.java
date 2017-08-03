package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import lombok.Data;

/**
 * Created by amandeep on 20/07/17.
 */
@Entity
@Data
public class UserDevice {
    @Index
    @Id
    private String id;
    @Index
    private DeviceType type;
    @Unindex
    private String pushToken;
    @Index
    private String userId;

}
