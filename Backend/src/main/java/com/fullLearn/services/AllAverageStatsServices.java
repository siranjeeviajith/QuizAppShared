package com.fullLearn.services;

import com.fullLearn.beans.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.*;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class AllAverageStatsServices {

    public Map<String, Object> getLearningStats(int type, String order, int limit, String cursorStr,int minAvg, int maxAvg) {




        String orderFilter = (type == 4) ? "fourWeekAvg" : "twelveWeekAvg";


        Query<LearningStatsAverage> userDataQuery = ofy().load().type(LearningStatsAverage.class).limit(limit);
        System.out.println("checkpoint 1 "+userDataQuery.list().size());
        if(cursorStr!=null){
            userDataQuery = userDataQuery.startAt(Cursor.fromWebSafeString(cursorStr));
        }

        if(minAvg>0){
          userDataQuery=  userDataQuery.filter(orderFilter+" >=",minAvg);
            System.out.println("check point 2 "+userDataQuery.list().size());
        }
        if(maxAvg>0){
        userDataQuery= userDataQuery.filter(orderFilter+" <=",maxAvg);
            System.out.println("check point 3 "+userDataQuery.list().size());
        }

        if (order.equals("desc"))
            orderFilter = "-" + orderFilter;

        userDataQuery=userDataQuery.order(orderFilter);
        System.out.println("check point 4 "+userDataQuery.list().size());
        List<LearningStatsAverage> userDataList = userDataQuery.list();

        QueryResultIterator<LearningStatsAverage> iterator = userDataQuery.iterator();

        while (iterator.hasNext()) {
            iterator.next();
        }

        cursorStr = iterator.getCursor().toWebSafeString();
Map<String,Object> response=new HashMap<String,Object>();
System.out.println("userstats "+userDataList);
        response.put("stats", userDataList);
        response.put("cursor", cursorStr);

        return response;

    }



}