package com.fullLearn.services;

import com.fullLearn.beans.LearningStatsAverage;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.collect.Lists;
import com.googlecode.objectify.cmd.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by user on 7/12/2017.
 */
public class LearningStatsService {

    public LearningStatsAverage getStatsByUserId(String id) {
        LearningStatsAverage userDatas = ofy().load().type(LearningStatsAverage.class).id(id).now();

        return userDatas;
    }


    public Map<String, Object> getAllUserStats(int type, String order, int limit, String cursorStr, int minAvg, int maxAvg) {
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
        if (userDataList.size() < limit)
            cursorStr = null;

        Map<String,Object> response=new HashMap<>();
        Map<String, Object> data = new HashMap();

            data.put("stats",userDataList);
            response.put("data", data);
        response.put("cursor", cursorStr);

        return response;

    }

}
