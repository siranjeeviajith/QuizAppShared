package com.fullLearn.services;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.UserDevice;
import com.fullLearn.helpers.Constants;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.collect.Lists;
import com.googlecode.objectify.cmd.Query;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by amandeep on 20/07/17.
 */
public class UserDevicesService {

    private static Logger logger = Logger.getLogger(UserDevicesService.class.getName());

    public UserDevice saveUserDevice(UserDevice device) {


        ofy().save().entity(device).now();

        return device;
    }

    public UserDevice deleteUserDevice(String id) {


        UserDevice deletedDevice = ofy().load().type(UserDevice.class).id(id).now();

        ofy().delete().type(UserDevice.class).id(id).now();
        logger.info("userDevice deleted succesffuly");
        return deletedDevice;
    }


}
