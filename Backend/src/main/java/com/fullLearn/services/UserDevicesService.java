package com.fullLearn.services;

import com.fullLearn.beans.UserDevice;


import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by amandeep on 20/07/17.
 */
public class UserDevicesService {

private static Logger logger=Logger.getLogger(UserDevicesService.class.getName());

    public UserDevice saveUserDevice(UserDevice device) {




        ofy().save().entity(device).now();

        return device;
    }

    public UserDevice deleteUserDevice(String id) {


        UserDevice deletedDevice=ofy().load().type(UserDevice.class).id(id).now();

        ofy().delete().type(UserDevice.class).id(id).now();
        logger.info("userDevice deleted succesffuly");
return deletedDevice;
    }

}
