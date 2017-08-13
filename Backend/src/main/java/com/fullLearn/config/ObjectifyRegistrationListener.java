package com.fullLearn.config;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.TrendingChallenges;
import com.fullLearn.beans.UserDevice;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by amandeep on 6/28/2017.
 */
@Slf4j
public class ObjectifyRegistrationListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {

        log.info("Registering Ofy Entities");
        ObjectifyService.register(Contacts.class);
        ObjectifyService.register(LearningStats.class);
        ObjectifyService.register(LearningStatsAverage.class);
        ObjectifyService.register(TrendingChallenges.class);
        ObjectifyService.register(UserDevice.class);
    }

    public void contextDestroyed(ServletContextEvent event) {
        log.info("ServletContextListener destroyed");
    }

}
