package com.fullLearn.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.*;
import com.fullLearn.helpers.UserStats;
import com.googlecode.objectify.ObjectifyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class LearningAvgStats {
    /*static {
        ObjectifyService.register(WeeksStats.class);
    }*/

    LearningStatsAverage ws = new LearningStatsAverage();
    UserStats us = new UserStats();

    public Map<String, Object> getLearningStats(String type,String order)
    {
        List<LearningStatsAverage> userDatas = null;
        ObjectMapper obj = new ObjectMapper();
        try
        {
            if(type.equals("4"))
            {
                if(order.equals("asc"))
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("fourthWeek").list();
                    System.out.println(userDatas);
                }
                else
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("-fourthWeek").list();
                    System.out.println(userDatas);
                }
            }
            else if(type.equals("12"))
            {
                if(order.equals("asc"))
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("twelfthWeek").list();
                    System.out.println(userDatas);
                }
                else
                {
                    userDatas = ofy().load().type(LearningStatsAverage.class).order("-twelfthWeek").list();
                    System.out.println(userDatas);
                }
            }
            else
            {
                userDatas.add(null);
            }

            if(userDatas.size() != 0)
            {
                    Map<String,Object> userDetails = new HashMap<String,Object>();
                    userDetails.put("status","Succes");

                    userDetails.put("error",null);
                    userDetails.put("code","200");
                    userDetails.put("response","true");
                    String datas = obj.writeValueAsString(userDatas);
                    userDetails.put("datas",datas);
                    /*for(int i=0;i<userDatas.size();i++) {
                      //  userData = obj.readValue(obj.writeValueAsString(userDatas.get(i)), new TypeReference<Map<String, Object>>() {});
                        //String number = "No"+i;
                      //  userDetails.put("datas",userData);
                    }*/
                return userDetails;
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
