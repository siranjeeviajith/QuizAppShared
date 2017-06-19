package com.fullLearn.services;

import com.fasterxml.jackson.core.type.TypeReference;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.*;
import com.fullLearn.helpers.*;
import com.googlecode.objectify.ObjectifyService;
import java.util.*;

public class WeekStats {

    static {
        ObjectifyService.register(WeeksStats.class);
    }

    WeeksStats ws = new WeeksStats();
    UserStats us = new UserStats();

    public Map<String, Object> getWeekStats(String userId)
    {
        ObjectMapper obj = new ObjectMapper();
        try
        {
           List<WeeksStats> userDatas = ofy().load().type(WeeksStats.class).filter("userId ==", userId).list();
            System.out.println(userDatas);
           if(userDatas.size() != 0)
           {
               Map<String,Object> userData = obj.readValue(obj.writeValueAsString(userDatas.get(0)), new TypeReference<Map<String,Object>>(){});
               String status = "success";
               String response = "true";
               String error = null;
               String code =  "200";
               Map<String,Object> userStats = us.getResponse(status,response,error,code,userData);
               return userStats;

           }

           else
           {
               Map<String,Object> userData = new HashMap<>();
               userData.put("reason","No data or Request Failed");
               String status = "failure";
               String response = "false";
               String error = "Request Failed";
               String code =  "500";
               Map<String,Object> userStats = us.getResponse(status,response,error,code,userData);
               return userStats;
           }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Map<String,Object> userData = new HashMap<>();
            userData.put("reason","Request Failed");
            String status = "failure";
            String response = "false";
            String error = "Request Failed";
            String code =  "400";
            Map<String,Object> userStats = us.getResponse(status,response,error,code,userData);
            return userStats;

        }
    }

}
