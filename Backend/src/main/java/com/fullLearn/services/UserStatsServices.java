package com.fullLearn.services;

import static com.googlecode.objectify.ObjectifyService.ofy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.*;

import java.util.*;

public class UserStatsServices {

    LearningStatsAverage ws = new LearningStatsAverage();

    public LearningStatsAverage getWeekStats(String str)
    {
        ObjectMapper obj = new ObjectMapper();


                String userId = str.substring(1);
                System.out.println("fetching avg for user: "+userId);
                LearningStatsAverage userDatas =  ofy().load().type(LearningStatsAverage.class).id(userId).now();
                System.out.println(userDatas);

                return userDatas;
    }

}
