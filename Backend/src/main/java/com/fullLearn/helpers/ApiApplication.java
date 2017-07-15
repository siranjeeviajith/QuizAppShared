package com.fullLearn.helpers;

import com.fullLearn.endpoints.api.LearningStatsEndpoint;
import com.fullLearn.endpoints.cron.ContactSyncCronEndpoint;
import com.fullLearn.endpoints.cron.UserStatsCronEndpoint;


import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 7/12/2017.
 */
public class ApiApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(LearningStatsEndpoint.class);
        classes.add(ContactSyncCronEndpoint.class);
        classes.add(UserStatsCronEndpoint.class);

        return classes;
    }
}