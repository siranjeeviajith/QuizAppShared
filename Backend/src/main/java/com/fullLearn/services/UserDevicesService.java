package com.fullLearn.services;

import com.fullLearn.beans.UserDevice;
import com.googlecode.objectify.Key;

import lombok.extern.slf4j.Slf4j;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by amandeep on 20/07/17.
 */
@Slf4j
public class UserDevicesService {

    public UserDevice saveUserDevice(UserDevice device) {
        ofy().save().entity(device).now();
        return device;
    }

    public void deleteUserDevice(String id) {
        ofy().delete().key(Key.create(UserDevice.class, id)).now();
    }
}
