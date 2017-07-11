package com.fullLearn.services;

import com.fullLearn.beans.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.googlecode.objectify.cmd.Query;

import java.util.*;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AllAverageStatsServices {

    public Map<String, Object> getLearningStats(int type, String order, int limit, String cursorStr, int minAvg, int maxAvg) {


        String orderFilter = (type == 4) ? "fourWeekAvg" : "twelveWeekAvg";


        Query<LearningStatsAverage> userDataQuery = ofy().load().type(LearningStatsAverage.class).limit(limit);

        if (cursorStr != null) {
            userDataQuery = userDataQuery.startAt(Cursor.fromWebSafeString(cursorStr));
        }

        if (minAvg > 0) {
            userDataQuery = userDataQuery.filter(orderFilter + " >=", minAvg);

        }
        if (maxAvg > 0) {
            userDataQuery = userDataQuery.filter(orderFilter + " <=", maxAvg);

        }

        if (order.equals("desc"))
            orderFilter = "-" + orderFilter;

        userDataQuery = userDataQuery.order(orderFilter);

        QueryResultIterator<LearningStatsAverage> iterator = userDataQuery.iterator();

        List<LearningStatsAverage> userDataList = Lists.newArrayList(iterator);
        cursorStr = iterator.getCursor().toWebSafeString();
        Map<String, Object> response = new HashMap();
        System.out.println("userstats " + userDataList);
        response.put("stats", userDataList);
        response.put("cursor", cursorStr);

        return response;

    }


}