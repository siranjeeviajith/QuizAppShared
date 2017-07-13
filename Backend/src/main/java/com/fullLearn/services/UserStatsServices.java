package com.fullLearn.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.*;

import java.util.*;

@Deprecated
public class UserStatsServices {

    LearningStatsAverage ws = new LearningStatsAverage();

    public Map<String, Object> getWeekStats(String userId) {
        ObjectMapper obj = new ObjectMapper();

        System.out.println("fetching avg for user: " + userId);
        LearningStatsAverage userDatas = ofy().load().type(LearningStatsAverage.class).id(userId).now();


        Map<String,Object> response=new HashMap();
        response.put("stats",userDatas);

        return response;
    }

}
