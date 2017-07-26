package com.fullLearn.helpers;

import com.fullLearn.endpoints.api.LearningStatsEndpoint;
import com.fullLearn.endpoints.api.UserDevicesEndpoint;
import com.fullLearn.endpoints.cron.ContactSyncCronEndpoint;
import com.fullLearn.endpoints.cron.UserStatsCronEndpoint;
import com.fullLearn.endpoints.cron.NotificationCronEndpoint;
import com.fullLearn.filter.AccessTokenFilter;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Created by amandeep on 7/12/2017.
 */
public class ApiApplication extends Application {


    private Set<Object> singletons = new HashSet<Object>();
    AccessTokenFilter accessTokenFilter=new AccessTokenFilter();
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(LearningStatsEndpoint.class);
        classes.add(ContactSyncCronEndpoint.class);
        classes.add(UserStatsCronEndpoint.class);
        classes.add(UserDevicesEndpoint.class);
        classes.add(NotificationCronEndpoint.class);
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        singletons.add(accessTokenFilter);
        return singletons;


    }
}