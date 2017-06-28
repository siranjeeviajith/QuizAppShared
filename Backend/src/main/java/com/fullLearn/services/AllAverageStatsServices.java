package com.fullLearn.services;

import com.fullLearn.beans.*;
import java.util.*;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class AllAverageStatsServices {

    public List<LearningStatsAverage> getLearningStats(String type,String order,int limit)
    {
        List<LearningStatsAverage> userDatas = null;

            if(type.equals("4"))
            {
                if(order.equals("asce"))
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("fourWeekAvg").limit(limit).list();
                }
                else
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("-fourWeekAvg").limit(limit).list();
                }

                return userDatas;
            }
            else if(type.equals("12"))
            {
                if(order.equals("asce"))
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("twelveWeekAvg").limit(limit).list();
                }
                else
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("-twelveWeekAvg").limit(limit).list();
                }
                return userDatas;
            }

            return null;

    }
}
