package com.fullLearn.helpers;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by user on 6/28/2017.
 */
public class ObjectifyRegistrationListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("Registering Ofy Entities");
        ObjectifyService.register(Contacts.class);
        ObjectifyService.register(LearningStats.class);
        ObjectifyService.register(LearningStatsAverage.class);
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("ServletContextListener destroyed");
    }

}
