package com.fullLearn.services;

import com.fasterxml.jackson.core.type.TypeReference;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.*;
import com.fullLearn.helpers.*;

import java.util.*;

public class WeekStatsServices {

    LearningStatsAverage ws = new LearningStatsAverage();
    UserStatsHelper us = new UserStatsHelper();

    public Map<String, Object> getWeekStats(String userId)
    {
        ObjectMapper obj = new ObjectMapper();
        try
        {
           LearningStatsAverage userDatas =  ofy().load().type(LearningStatsAverage.class).id(userId).now();
           System.out.println(userDatas);
           if(userDatas != null)
           {
               Map<String,Object> userData = obj.readValue(obj.writeValueAsString(userDatas), new TypeReference<Map<String,Object>>(){});

               Map<String,Object> userStats = us.getResponse(userData);
               return userStats;

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
