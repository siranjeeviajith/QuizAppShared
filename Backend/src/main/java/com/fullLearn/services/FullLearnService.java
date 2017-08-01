package com.fullLearn.services;


import com.fullLearn.beans.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.collect.Lists;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fullLearn.helpers.Constants;
import com.fullLearn.helpers.HTTP;
import com.fullLearn.model.ChallengesInfo;
import com.googlecode.objectify.cmd.Query;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import java.util.logging.Level;
import java.util.logging.Logger;


import static com.googlecode.objectify.ObjectifyService.ofy;


public class FullLearnService {
    private final static Logger logger = Logger.getLogger(FullLearnService.class.getName());


    final static MemcacheService cache = MemcacheServiceFactory.getMemcacheService();

    public FullLearnService() {
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    }

    public Map<String, ChallengesInfo> challengesCountMap = new HashMap();


    public boolean fetchAllUserStats() throws IOException {
        logger.setLevel(Level.ALL);


        int count = 0;

        String key = "dailyStatsCursor";
        String cursorStr = (String) cache.get(key);
        do {


            Query<Contacts> query = ofy().load().type(Contacts.class).limit(50);
            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = query.iterator();

            List<Contacts> contactList = Lists.newArrayList(iterator);

            count = count + contactList.size();
            logger.info("total : " + count);
            // System.out.println("total :" + count);

            if (contactList.size() < 1) {
                break;
            }

            fetchUserDailyStats(contactList);

            cursorStr = iterator.getCursor().toWebSafeString();
            cache.put(key, cursorStr, Expiration.byDeltaSeconds(300));

        } while (cursorStr != null);

        cache.delete(key);

        // saving trending challenges
        TrendingChallenges latestTrends = mapTredingChallenges(challengesCountMap);

        // saving trends
        saveUserStats(latestTrends);

        return true;

    } // end of fetchUserDetails

    public void fetchUserDailyStats(List<Contacts> contactList) throws IOException {
        //To do for iterating and getting data for each user by Calling HTTP class in helper package

        for (Contacts contact : contactList) {
            int i = 1;

            while (i <= 3) {


                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);

                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);


                Date start = cal.getTime();
                long startDate = start.getTime();
                Calendar cal1 = Calendar.getInstance();
                cal1.add(Calendar.DATE, -1);

                cal1.set(Calendar.HOUR_OF_DAY, 23);
                cal1.set(Calendar.MINUTE, 59);
                cal1.set(Calendar.SECOND, 59);
                cal1.set(Calendar.MILLISECOND, 0);


                Date end = cal1.getTime();// current date
                long endDate = end.getTime();// endDate for fetching user data
                String url = "";
                String methodType = "";
                String contentType = "";

                // email will be dynamic for contacts pojo
                ///// Start time will be dynamic and will be yesterdays date of event and endTime will also be dynamic and and will current time .

                url = Constants.AU_API_URL + "/v1/completedMinutes?apiKey=" + Constants.AU_APIKEY + "&email=" + contact.getLogin() + "&startTime=" + startDate + "&endTime=" + endDate;
               //url= "https://adaptivecourse.appspot.com/v1/completedMinutes?apiKey=b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d&email=amandeep.santokh@full.co&startTime=1200854400000&endTime=1601459199000";
                logger.info("url : " + url);
                //System.out.println("url : "+url);
                methodType = "POST";
                contentType = "application/json";
                Map<String, Object> dataMap;
                try {
                    dataMap = HTTP.request(url, methodType, contentType);

                    logger.info("user : " + contact.getLogin() + " => " + dataMap);
                    //System.out.println("user : " + contact.getLogin() + " => " + dataMap);

                    LearningStats dailyEntity = MapUserDataAfterFetch(dataMap, contact.getLogin(), contact.getId(), startDate, endDate, Frequency.DAY);
                    //  save daily entity to datastore
                    saveUserStats(dailyEntity);

                    /// calculating trends
                    calculateLearningTrends(dailyEntity.getChallenges_details());

                } catch (Exception e) {
                    e.printStackTrace();
                    i++;
                    continue;
                }
                break;
            }
        }

    } // end of fetchUserDailyStats method

    private void calculateLearningTrends(Map<String, Object> challenges) {


        try {

            if (challenges == null || challenges.isEmpty()) {
                return;
            }

            for (Entry mapEntry : challenges.entrySet()) {

                String title = (String) mapEntry.getKey();

                ChallengesInfo info = challengesCountMap.get(title);
                if (info == null) {
                    info = new ChallengesInfo();
                    Map<String,Object> titleDetails= (Map<String, Object>) mapEntry.getValue();
                    info.setDuration((int) titleDetails.get("minutes"));
                    info.setImage((String) titleDetails.get("image"));
                    info.setUrl((String) titleDetails.get("link"));
                }

                info.setViews(info.getViews() + 1);

                challengesCountMap.put(title, info);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveUserStats(Object entry) {

        ofy().save().entity(entry).now();
        // end of storeUserActivityDetail method
    }

    /////////////////////////     WEEKLY REPORTS


    public boolean generateWeeklyReport() throws JsonProcessingException {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        Date start = cal.getTime();
        long startDate = start.getTime();
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -1);

        cal1.set(Calendar.HOUR_OF_DAY, 23);
        cal1.set(Calendar.MINUTE, 59);
        cal1.set(Calendar.SECOND, 59);
        cal1.set(Calendar.MILLISECOND, 0);


        Date end = cal1.getTime();// current date
        long endDate = end.getTime();// endDate for fetching user data

        logger.info("Weekly stats ");
        //System.out.println("Weekly stats  ");

        int userCount = 0;
        //MailDispatcher.sendEmail();
        String cursorStr = null;
        do {

            Query<Contacts> contactQuery = ofy().load().type(Contacts.class).limit(30);

            if (cursorStr != null)
                contactQuery = contactQuery.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = contactQuery.iterator();

            List<Contacts> contactList = contactQuery.list();

            userCount = userCount + contactList.size();

            if (contactList.size() < 1) {
                return true;
            }

            generateWeeklyReportAllUserByBatch(iterator, startDate, endDate);
            cursorStr = iterator.getCursor().toWebSafeString();
        } while (cursorStr != null);


        return true;
    }

    private void generateWeeklyReportAllUserByBatch(QueryResultIterator<Contacts> iterator, long startDate, long endDate) throws JsonProcessingException {


        while (iterator.hasNext()) {

            Contacts contact = iterator.next();

            List<LearningStats> weeklyStateUser = ofy().load().type(LearningStats.class).filter("userId ==", contact.getId()).filter("startTime >=", startDate).filter("startTime <", endDate).list();


            Iterator weeklyStatsIterator = weeklyStateUser.iterator();
            int minutesAggregation = 0;
            int challengeCompletedAggregation = 0;
            int day = 1;

            LearningStats userStats = null;
            while (weeklyStatsIterator.hasNext()) {

                userStats = (LearningStats) weeklyStatsIterator.next();
                minutesAggregation = minutesAggregation + userStats.getMinutes();
                challengeCompletedAggregation = challengeCompletedAggregation + userStats.getChallenges_completed();
                logger.info("minutes for day " + day + " is " + userStats.getMinutes() + " email " + userStats.getEmail() + "in Time is " + userStats.getStartTime() + " - " + userStats.getEndTime());
                //System.out.println("minutes for day " + day + " is " + userStats.getMinutes() + " email " + userStats.getEmail() + "in Time is " + userStats.getStartTime() + " - " + userStats.getEndTime());

                day++;
            }
            logger.info("total minutes for week is " + minutesAggregation + " email " + contact.getLogin() + "in Time is " + startDate + " - " + endDate);
            // System.out.println("total minutes for week is " + minutesAggregation + " email " + contact.getLogin() + "in Time is " + startDate + " - " + endDate);


            LearningStats weeklyEntity = mapUserWeeklyStats(contact, minutesAggregation, challengeCompletedAggregation, startDate, endDate);

            //System.out.println(new ObjectMapper().writeValueAsString(weeklyEntity));
            //// storing weekly entity to datastore

            saveUserStats(weeklyEntity);


            //System.out.println(new ObjectMapper().writeValueAsString(contact));
        }


    }


    private LearningStats mapUserWeeklyStats(Contacts contact, int minutesAggregation, int challengeCompletedAggregation, long startDate, long endDate) {

        LearningStats weeklyEntity = new LearningStats();


        // unique id
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        weeklyEntity.setId(id);
        System.out.println("id :" + weeklyEntity.getId());
        //   userid

        weeklyEntity.setUserId(contact.getId());
        logger.info("contact id " + contact.getId());

        // startTime and endTime

        weeklyEntity.setStartTime(startDate);
        logger.info("start :" + weeklyEntity.getStartTime());
        weeklyEntity.setEndTime(endDate);

        //  frequency for daily entrys
        weeklyEntity.setFrequency(Frequency.WEEK);

        // email
        weeklyEntity.setEmail(contact.getLogin());


        // minutes and challenges

        weeklyEntity.setMinutes(minutesAggregation);

        weeklyEntity.setChallenges_completed(challengeCompletedAggregation);


        return weeklyEntity;
    }


    public void calculateAverage(String userId, String email) {
        logger.info("email " + email);
        //System.out.println("email " + email);
        int day = 7 * 11;

////    Fixing two dates

        Date startdate = null;
        Date enddate = null;
        Calendar today = Calendar.getInstance();

        if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

            System.out.println("saturday");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -7);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 0);

            enddate = cal.getTime();
            long endDate = enddate.getTime();


            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.DATE, -13);
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);


            startdate = cal1.getTime();

        } else {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 0);

            enddate = cal.getTime();


            Calendar cal1 = Calendar.getInstance();
            cal1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            cal1.add(Calendar.DATE, -6);

            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);
            startdate = cal1.getTime();


        }


        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(startdate);
        cal3.add(Calendar.DATE, -day);
        Date startTime = cal3.getTime();

        long startDate = startTime.getTime();

        Calendar cal4 = Calendar.getInstance();
        cal4.setTime(enddate);
        cal4.add(Calendar.DATE, -0);
        Date endTime = cal4.getTime();

        long endDate = endTime.getTime();


        List<LearningStats> StateUser = ofy().load().type(LearningStats.class).filter("userId ==", userId).filter("startTime >=", startDate).filter("startTime <", endDate).filter("frequency ==", Frequency.WEEK).order("startTime").list();
        logger.info("size of no of weeks " + StateUser.size());

        Iterator WeekAverageIterator = StateUser.iterator();
        int weekCount = 1;

        int fourWeekAverage = 0;
        int twelfthWeekAverage = 0;
        while (WeekAverageIterator.hasNext()) {
            LearningStats userStats = (LearningStats) WeekAverageIterator.next();

            if (weekCount <= 8)
                twelfthWeekAverage = twelfthWeekAverage + userStats.getMinutes();

            else {
                twelfthWeekAverage = twelfthWeekAverage + userStats.getMinutes();
                fourWeekAverage = fourWeekAverage + userStats.getMinutes();

            }
            //logger.info("minutes for week " + weekCount + " is " + userStats.getMinutes() + " email " + email + "in Time is " + userStats.getStartTime() + " - " + userStats.getEndTime());
            //System.out.println("minutes for week " + weekCount + " is " + userStats.getMinutes() + " email " + email + "in Time is " + userStats.getStartTime() + " - " + userStats.getEndTime());
            weekCount++;


        }

        float fourWeekFloat = (float) fourWeekAverage / 4;
        float twelfthWeekFloat = (float) twelfthWeekAverage / 12;



        fourWeekAverage = (int) Math.round(fourWeekFloat);
        twelfthWeekAverage = (int) Math.round(twelfthWeekFloat);

        logger.info("Twelve weeks timestamps is startime : " + startDate + " and endTime is : " + endDate);
        LearningStatsAverage averageEntity = mapUserDataAverage(fourWeekAverage, twelfthWeekAverage, userId, email);


        /////   save entity to datastore
        saveUserStats(averageEntity);

    }

    private LearningStatsAverage mapUserDataAverage(int fourWeekAverage, int twelfthWeekAverage, String userId, String email) {


        LearningStatsAverage averageEntity = new LearningStatsAverage();

        averageEntity.setUserId(userId);
        averageEntity.setFourWeekAvg(fourWeekAverage);
        averageEntity.setTwelveWeekAvg(twelfthWeekAverage);
        averageEntity.setEmail(email);
        return averageEntity;
    }


    public LearningStats MapUserDataAfterFetch(Map<String, Object> dataMap, String email, String userId, long startDate, long endDate, Frequency frequency) throws JsonProcessingException {


        LearningStats daily = new LearningStats();

        if ((boolean) dataMap.get("response") && dataMap.get("status").equals("Success")) {


            // set id

            daily.setId(userId + ":" + startDate + ":" + endDate);
            //System.out.println("id :" + twelveWeeksEntity.getId());
            //  2. userid

            daily.setUserId(userId);

            // 6 and 7 startTime and endTime

            daily.setStartTime(startDate);
            daily.setEndTime(endDate);

            //  5. frequency for daily entrys
            daily.setFrequency(frequency);

            //9. email
            daily.setEmail(email);
            // 3,4,8 for minutes and challenges


            Map<String, Object> mapToLearningStats = (Map<String, Object>) dataMap.get("data");
            Map<String, Object> emailMap = (Map<String, Object>) mapToLearningStats.get(email);
               if (emailMap == null) {
                daily.setMinutes(0);
                daily.setChallenges_completed(0);
            } else {
                daily.setMinutes((int) emailMap.get("minutes"));
                daily.setChallenges_completed((int) emailMap.get("challenges_completed"));
                Map<String, Object> challenges = new HashMap();
                challenges = (Map<String, Object>) emailMap.get("challenges_details");
                if (challenges != null) {
                    daily.setChallenges_details(challenges);
                }


            }


        }// end of if


        return daily;

    }

    public boolean calculateAllUserStatsAverage() {


        int usercount = 0;

        String cursorStr = null;
        do {

            Query<Contacts> contactQuery = ofy().load().type(Contacts.class).limit(30);

            if (cursorStr != null)
                contactQuery = contactQuery.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = contactQuery.iterator();

            List<Contacts> contactList = contactQuery.list();

            usercount = usercount + contactList.size();
            logger.info("userscount: " + usercount);

            logger.info("size :" + contactList.size());

            if (contactList.size() < 1) {
                return true;
            }

            calculateAverageWeek(iterator);
            cursorStr = iterator.getCursor().toWebSafeString();

        } while (cursorStr != null);


        return true;

    }

    private void calculateAverageWeek(QueryResultIterator<Contacts> iterator) {


        int day = (7 * 12);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -day);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        Date start = cal.getTime();
        long startDate = start.getTime();
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -1);

        cal1.set(Calendar.HOUR_OF_DAY, 23);
        cal1.set(Calendar.MINUTE, 59);
        cal1.set(Calendar.SECOND, 59);
        cal1.set(Calendar.MILLISECOND, 0);


        Date end = cal1.getTime();// current date
        long endDate = end.getTime();// endDate for fetching user data


        while (iterator.hasNext()) {

            Contacts contact = iterator.next();

            List<LearningStats> StateUser = ofy().load().type(LearningStats.class).filter("userId ==", contact.getId()).filter("startTime >=", startDate).filter("startTime <", endDate).filter("frequency ==", Frequency.WEEK).order("startTime").list();

            Iterator WeekAverageIterator = StateUser.iterator();
            int weekCount = 1;

            int fourWeekAverage = 0;
            int twelfthWeekAverage = 0;
            while (WeekAverageIterator.hasNext()) {
                LearningStats userStats = (LearningStats) WeekAverageIterator.next();

                if (weekCount <= 8)
                    twelfthWeekAverage = twelfthWeekAverage + userStats.getMinutes();

                else {
                    twelfthWeekAverage = twelfthWeekAverage + userStats.getMinutes();
                    fourWeekAverage = fourWeekAverage + userStats.getMinutes();
                }
                weekCount++;


            }


            float fourWeekFloat = (float) fourWeekAverage / 4;
            float twelfthWeekFloat = (float) twelfthWeekAverage / 12;

            fourWeekAverage = (int) Math.round(fourWeekFloat);
            twelfthWeekAverage = (int) Math.round(twelfthWeekFloat);

            FullLearnService fullLearnService = new FullLearnService();
          LearningStatsAverage averageEntity = fullLearnService.mapUserDataAverageWeek(fourWeekAverage, twelfthWeekAverage, contact);


            /////   save entity to datastore
            fullLearnService.saveUserStats(averageEntity);
        }
    }

    private LearningStatsAverage mapUserDataAverageWeek(int fourWeekAverage, int twelfthWeekAverage, Contacts contact) {

        LearningStatsAverage averageEntity = new LearningStatsAverage();

        averageEntity.setUserId(contact.getId());
        averageEntity.setFourWeekAvg(fourWeekAverage);
        averageEntity.setTwelveWeekAvg(twelfthWeekAverage);
        averageEntity.setEmail(contact.getLogin());
        return averageEntity;
    }

    public TrendingChallenges mapTredingChallenges(Map<String, ChallengesInfo> challengeViewCount) throws JsonProcessingException {

        //// startTime and endTime

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        Date start = cal.getTime();
        long date = start.getTime();


        ////  sorting of map by values

        Set<Entry<String, ChallengesInfo>> set = challengeViewCount.entrySet();
        List<Entry<String, ChallengesInfo>> list = new ArrayList(set);

        Collections.sort(list, new Comparator<Map.Entry<String, ChallengesInfo>>() {
            public int compare(Map.Entry<String, ChallengesInfo> o1, Map.Entry<String, ChallengesInfo> o2) {

                if (o1.getValue().getViews() > o2.getValue().getViews())
                    return -1;
                else if (o1.getValue().getViews() < o2.getValue().getViews())
                    return 1;
                else
                    return 0;
            }
        });
        logger.info("all list of trends: " + new ObjectMapper().writeValueAsString(list));
        //System.out.println("all list of trends: " + new ObjectMapper().writeValueAsString(list));

        LinkedHashMap<String, ChallengesInfo> mapOfTrends = new LinkedHashMap<>();
        TrendingChallenges latestTrendsDaily = new TrendingChallenges();

        int rowCount = 1;
        for (Map.Entry<String, ChallengesInfo> entry : list) {
            String title = entry.getKey();
            ChallengesInfo challengesInfo = entry.getValue();
            int viewCount = challengesInfo.getViews();

            if (rowCount > 10 || viewCount < 2)
                break;
            mapOfTrends.put(title, challengesInfo);

            rowCount++;
        }

        System.out.println("10 trends :" + new ObjectMapper().writeValueAsString(mapOfTrends));
        latestTrendsDaily.setTrends(mapOfTrends);
        latestTrendsDaily.setId(date);
        latestTrendsDaily.setTime(date);

        return latestTrendsDaily;
    }

    public TrendingChallenges getLatestTrends(long date) {


        TrendingChallenges latestTrendQuery = ofy().load().type(TrendingChallenges.class).id(date).now();


        Set<Entry<String, ChallengesInfo>> set = latestTrendQuery.getTrends().entrySet();
        List<Map.Entry<String, ChallengesInfo>> list = new ArrayList(set);

        Collections.sort(list, new Comparator<Map.Entry<String, ChallengesInfo>>() {
            public int compare(Map.Entry<String, ChallengesInfo> o1, Map.Entry<String, ChallengesInfo> o2) {

                if (o1.getValue().getViews() > o2.getValue().getViews())
                    return -1;
                else if (o1.getValue().getViews() < o2.getValue().getViews())
                    return 1;
                else
                    return 0;
            }
        });

        LinkedHashMap<String, ChallengesInfo> trends = new LinkedHashMap<>();
        for (Map.Entry<String, ChallengesInfo> entry : list) {
            trends.put(entry.getKey(), entry.getValue());
        }

        latestTrendQuery.setTrends(trends);
        return latestTrendQuery;

    }
}