package com.fullLearn.services;

import com.fullLearn.beans.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.*;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class AllAverageStatsServices {

    public Map<String, Object> getLearningStats(String type, String order, int limit, String cursorStr)
    {

        Query<LearningStatsAverage> userDataQuery =ofy().load().type(LearningStatsAverage.class);
        Map <String, Object> response = null;

        if(type.equals("4") && order.equals("asce"))
        {
            userDataQuery =ofy().load().type(LearningStatsAverage.class).order("fourWeekAvg").limit(limit);

        }
        else if(type.equals("4") && order.equals("desc"))
        {
            userDataQuery =ofy().load().type(LearningStatsAverage.class).order("-fourWeekAvg").limit(limit);
        }
        else if(type.equals("12") && order.equals("asce"))
        {

            userDataQuery =ofy().load().type(LearningStatsAverage.class).order("twelveWeekAvg").limit(limit);
        }

        else if(type.equals("12") && order.equals("desc"))
        {
            userDataQuery =ofy().load().type(LearningStatsAverage.class).order("-twelveWeekAvg").limit(limit);

        }
           response= getUserDataBylimit(userDataQuery,cursorStr);


        return response;

    }

    private Map<String, Object> getUserDataBylimit(Query<LearningStatsAverage> userDataQuery, String cursorStr) {



        if (cursorStr != null)
            userDataQuery = userDataQuery.startAt(Cursor.fromWebSafeString(cursorStr));

        QueryResultIterator<LearningStatsAverage> iterator = userDataQuery.iterator();

        List<LearningStatsAverage> userDataList = userDataQuery.list();

        while(iterator.hasNext())
        {
            iterator.next();
        }

        cursorStr = iterator.getCursor().toWebSafeString();

        Map<String,Object> response = new HashMap();
        response.put("data",userDataList);
        response.put("error", null);
        response.put("response", true);
        response.put("cursor",cursorStr);
        return response;

    }



  /*  private Map<String, Object> getUserDataBylimit(String cursorStr, String order, int limit) {




            Query<LearningStatsAverage> userDataQuery =ofy().load().type(LearningStatsAverage.class).order(order).limit(limit);





    }*/
}