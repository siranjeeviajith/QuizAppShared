package com.fullLearn.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.*;
import com.fullLearn.helpers.UserStatsHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AllAverageStatsServices {
    LearningStatsAverage ws = new LearningStatsAverage();
    UserStatsHelper us = new UserStatsHelper();

    public Map<String, Object> getLearningStats(String type,String order,int limit)
    {
        List<LearningStatsAverage> userDatas = null;
        ObjectMapper obj = new ObjectMapper();
        try
        {
            if(type.equals("4"))
            {
                if(order.equals("aesc"))
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("fourthWeek").limit(limit).list();
                }
                else
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("-fourthWeek").limit(limit).list();
                }
            }
            else if(type.equals("12"))
            {
                if(order.equals("aesc"))
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("twelfthWeek").limit(limit).list();
                }
                else
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("-twelfthWeek").limit(limit).list();
                }
            }

            if(userDatas.size() != 0)
            {
                    Map<String,Object> userDetails = new HashMap<String,Object>();

                    userDetails.put("error",null);
                    userDetails.put("response",true);
                    userDetails.put("datas",userDatas);
                    return userDetails;
            }

            else
            {
                Map<String,Object> userData = new HashMap<>();

                Map<String,Object> userStats = us.getResponse(userData);
                return userStats;
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Map<String,Object> userData = new HashMap<>();

            Map<String,Object> userStats = us.getResponse(userData);
            return userStats;

        }
    }
}
