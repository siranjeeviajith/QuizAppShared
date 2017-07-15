package com.fullLearn.helpers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;
import com.fullLearn.beans.*;

/**
 * Created by user on 6/28/2017.
 */
public class ObjectifyRegistrationListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent arg0) {
        ObjectifyService.register(Contacts.class);
        ObjectifyService.register(LearningStats.class);
        ObjectifyService.register(LearningStatsAverage.class);
        ObjectifyService.register(TrendingChallenges.class);
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("ServletContextListener destroyed");
    }

}
