package com.fullLearn.helpers;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.TrendingChallenges;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 6/28/2017.
 */
public class ObjectifyRegistrationListener implements ServletContextListener {

    private final static Logger logger=Logger.getLogger(ObjectifyRegistrationListener.class.getName());
    public void contextInitialized(ServletContextEvent arg0) {
        logger.setLevel(Level.INFO);
        logger.info("Registering Ofy Entities");
       // System.out.println("Registering Ofy Entities");
        ObjectifyService.register(Contacts.class);
        ObjectifyService.register(LearningStats.class);
        ObjectifyService.register(LearningStatsAverage.class);
        ObjectifyService.register(TrendingChallenges.class);
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("ServletContextListener destroyed");
        //System.out.println("ServletContextListener destroyed");
    }

}
