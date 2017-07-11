package com.fullLearn.services;


import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.Frequency;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.helpers.Constants;
import com.fullLearn.helpers.HTTP;
import com.googlecode.objectify.cmd.Query;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static com.googlecode.objectify.ObjectifyService.ofy;


public class FullLearnService {

    public static boolean fetchAllUserStats() throws IOException {

        MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));

        int count = 0;

        String key = "dailyStatsCursor";
        String cursorStr = (String) syncCache.get(key);
        do {

            Query<Contacts> query = ofy().load().type(Contacts.class).limit(50);

            // String cursorStr = request.getParameter("cursor");
            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = query.iterator();

            List<Contacts> contactList = query.list();
            count = count + contactList.size();
            System.out.println("usercount : " + count);
            System.out.println("size :" + contactList.size());

            if (contactList.size() < 1) {
                return true;
            }

            fetchUserDailyStats(iterator);
            cursorStr = iterator.getCursor().toWebSafeString();
            syncCache.put(key, cursorStr, Expiration.byDeltaSeconds(300));

        } while (cursorStr != null);

        syncCache.delete(key);

        return true;
    } // end of fetchUserDetails

    public static void fetchUserDailyStats(QueryResultIterator contactList) throws IOException {
        //To do for iterating and getting data for each user by Calling HTTP class in helper package

        while (contactList.hasNext()) {
            int i = 1;
            Contacts contact = (Contacts) contactList.next();
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

                methodType = "POST";
                contentType = "application/json";
                Map<String, Object> dataMap;
                try {
                    dataMap = HTTP.request(url, methodType, contentType);
                } catch (Exception e) {
                    i++;
                    System.out.println(e.getMessage());
                    continue;
                }

                System.out.println("user : " + contact.getLogin() + " => " + dataMap);

                LearningStats dailyEntity = MapUserDataAfterFetch(dataMap, contact.getLogin(), contact.getId(), startDate, endDate, Frequency.DAY);
                //  save daily entity to datastore
                saveUserStats(dailyEntity);
                break;
            }
        }

    } // end of fetchUserDailyStats method


    public static void saveUserStats(Object entry) {

        ofy().save().entity(entry).now();
        // end of storeUserActivityDetail method
    }

    /////////////////////////     WEEKLY REPORTS


    public static boolean generateWeeklyReport() throws JsonProcessingException {


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


        System.out.println("Weekly stats  ");

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

    private static void generateWeeklyReportAllUserByBatch(QueryResultIterator<Contacts> iterator, long startDate, long endDate) throws JsonProcessingException {


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

                System.out.println("minutes for day " + day + " is " + userStats.getMinutes() + " email " + userStats.getEmail() + "in Time is " + userStats.getStartTime() + " - " + userStats.getEndTime());

                day++;
            }

            System.out.println("total minutes for week is " + minutesAggregation + " email " + contact.getLogin() + "in Time is " + startDate + " - " + endDate);


            LearningStats weeklyEntity = mapUserWeeklyStats(contact, minutesAggregation, challengeCompletedAggregation, startDate, endDate);

            System.out.println(new ObjectMapper().writeValueAsString(weeklyEntity));
            //// storing weekly entity to datastore

            saveUserStats(weeklyEntity);


            System.out.println(new ObjectMapper().writeValueAsString(contact));
        }


    }


    private static LearningStats mapUserWeeklyStats(Contacts contact, int minutesAggregation, int challengeCompletedAggregation, long startDate, long endDate) {

        LearningStats weeklyEntity = new LearningStats();


        // unique id
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        System.out.println("id = " + id);
        weeklyEntity.setId(id);
        System.out.println("id :" + weeklyEntity.getId());
        //   userid

        weeklyEntity.setUserId(contact.getId());
        System.out.println("userid :" + weeklyEntity.getUserId());
        System.out.println("contact id " + contact.getId());

        // startTime and endTime

        weeklyEntity.setStartTime(startDate);
        System.out.println("start :" + weeklyEntity.getStartTime());
        weeklyEntity.setEndTime(endDate);

        //  frequency for daily entrys
        weeklyEntity.setFrequency(Frequency.WEEK);
        System.out.println("freq :" + weeklyEntity.getFrequency());

        // email
        weeklyEntity.setEmail(contact.getLogin());


        // minutes and challenges

        weeklyEntity.setMinutes(minutesAggregation);

        weeklyEntity.setChallenges_completed(challengeCompletedAggregation);


        return weeklyEntity;
    }


    public static void calculateAverage(String userId, String email) {

        System.out.println("email " + email);
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
        System.out.println("size of no of weeks " + StateUser.size());

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
            if (email.equals("ramesh.lingappa@a-cti.com") || email.equals("shaikanjavali.mastan@a-cti.com") || email.equals("naresh.talluri@a-cti.com"))
                System.out.println("minutes for week " + weekCount + " is " + userStats.getMinutes() + " email " + email + "in Time is " + userStats.getStartTime() + " - " + userStats.getEndTime());
            weekCount++;


        }

        float fourWeekFloat = (float) fourWeekAverage / 4;
        float twelfthWeekFloat = (float) twelfthWeekAverage / 12;

        if (email.equals("ramesh.lingappa@a-cti.com") || email.equals("shaikanjavali.mastan@a-cti.com") || email.equals("naresh.talluri@a-cti.com")) {
            System.out.println("float values " + fourWeekFloat + " email " + email + "in Time is " + startDate + " - " + endDate);
            System.out.println("float values " + twelfthWeekFloat + " email " + email + "in Time is " + startDate + " - " + endDate);
        }

        fourWeekAverage = (int) Math.round(fourWeekFloat);
        twelfthWeekAverage = (int) Math.round(twelfthWeekFloat);
        if (email.equals("ramesh.lingappa@a-cti.com") || email.equals("shaikanjavali.mastan@a-cti.com") || email.equals("naresh.talluri@a-cti.com")) {
            System.out.println("total for 4 weeks " + fourWeekAverage + " email " + email + "in Time is " + startDate + " - " + endDate);
            System.out.println("total for 12 weeks " + twelfthWeekAverage + " email " + email + "in Time is " + startDate + " - " + endDate);
        }

        System.out.println("Twelve weeks timestamps is startime : " + startDate + " and endTime is : " + endDate);
        LearningStatsAverage averageEntity = mapUserDataAverage(fourWeekAverage, twelfthWeekAverage, userId, email);


        /////   save entity to datastore
        saveUserStats(averageEntity);

    }

    private static LearningStatsAverage mapUserDataAverage(int fourWeekAverage, int twelfthWeekAverage, String userId, String email) {


        LearningStatsAverage averageEntity = new LearningStatsAverage();

        averageEntity.setUserId(userId);
        averageEntity.setFourWeekAvg(fourWeekAverage);
        averageEntity.setTwelveWeekAvg(twelfthWeekAverage);
        averageEntity.setEmail(email);
        return averageEntity;
    }


    ////////////////////////////////            fetch data for 12 weeks for every user

    public static boolean fetchAllUserTwelveWeekStats() throws IOException {

        int count = 0;
        String cursorStr = null;
        do {

            Query<Contacts> query = ofy().load().type(Contacts.class).limit(30);

            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = query.iterator();

            List<Contacts> contactList = query.list();
            count = count + contactList.size();
            System.out.println("usercount : " + count);
            System.out.println("size :" + contactList.size());

            if (contactList.size() < 1) {
                return true;
            }

            fetchTwelveWeeksDataByBatch(iterator);
            cursorStr = iterator.getCursor().toWebSafeString();


        } while (cursorStr != null);

        return true;

    }

    private static void fetchTwelveWeeksDataByBatch(QueryResultIterator<Contacts> contactList) throws IOException {


        while (contactList.hasNext()) {

            Contacts contact = contactList.next();


            // Add the task to the default queue.
            Queue queue = QueueFactory.getQueue("StatsFetchingQueue");
            queue.add(TaskOptions.Builder.withUrl("/api/fetch/user/stats").param("email", contact.getLogin()).param("userId", contact.getId()));


        }
    }

    public static LearningStats MapUserDataAfterFetch(Map<String, Object> dataMap, String email, String userId, long startDate, long endDate, Frequency frequency) {


        ObjectMapper objectmapper = new ObjectMapper();


        LearningStats twelveWeeksEntity = new LearningStats();

        if ((boolean) dataMap.get("response") && dataMap.get("status").equals("Success")) {


            // set id

            twelveWeeksEntity.setId(userId + ":" + startDate + ":" + endDate);
            //System.out.println("id :" + twelveWeeksEntity.getId());
            //  2. userid

            twelveWeeksEntity.setUserId(userId);
            //System.out.println("userid :" + twelveWeeksEntity.getUserId());
            //System.out.println("contact id " + userId);

            // 6 and 7 startTime and endTime

            twelveWeeksEntity.setStartTime(startDate);
            //System.out.println("start :" + twelveWeeksEntity.getStartTime());
            twelveWeeksEntity.setEndTime(endDate);

            //  5. frequency for daily entrys
            twelveWeeksEntity.setFrequency(frequency);
            // System.out.println("freq :" + twelveWeeksEntity.getFrequency());

            //9. email
            twelveWeeksEntity.setEmail(email);
            // 3,4,8 for minutes and challenges


            Map<String, Object> mapToLearningStats = (Map<String, Object>) dataMap.get("data");
            Map<String, Object> emailMap = (Map<String, Object>) mapToLearningStats.get(email);

            if (emailMap == null) {
                twelveWeeksEntity.setMinutes(0);
                twelveWeeksEntity.setChallenges_completed(0);
            } else {
                twelveWeeksEntity.setMinutes((int) emailMap.get("minutes"));
                twelveWeeksEntity.setChallenges_completed((int) emailMap.get("challenges_completed"));


            }


        }// end of if


        return twelveWeeksEntity;

    }

    public static boolean calculateAllUserStatsAverage() {


        int usercount = 0;

        String cursorStr = null;
        do {

            Query<Contacts> contactQuery = ofy().load().type(Contacts.class).limit(30);

            if (cursorStr != null)
                contactQuery = contactQuery.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = contactQuery.iterator();

            List<Contacts> contactList = contactQuery.list();

            usercount = usercount + contactList.size();

            System.out.println("userscount: " + usercount);
            System.out.println("size :" + contactList.size());

            if (contactList.size() < 1) {
                return true;
            }

            calculateAverageWeek(iterator);
            cursorStr = iterator.getCursor().toWebSafeString();

        } while (cursorStr != null);


        return true;

    }

    private static void calculateAverageWeek(QueryResultIterator<Contacts> iterator) {


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
                System.out.println("minutes for week " + weekCount + " is " + userStats.getMinutes() + " email " + contact.getLogin() + "in Time is " + userStats.getStartTime() + " - " + userStats.getEndTime());
                weekCount++;


            }


            float fourWeekFloat = (float) fourWeekAverage / 4;
            float twelfthWeekFloat = (float) twelfthWeekAverage / 12;
            System.out.println("week is" + weekCount + "  and float minutes is for four weeks " + fourWeekFloat + "for email " + contact.getLogin() + "Time in " + startDate + " " + endDate);
            System.out.println("week is" + weekCount + "  and float minutes is for twelve weeks " + twelfthWeekFloat + "for email " + contact.getLogin() + "Time in " + startDate + " " + endDate);


            fourWeekAverage = (int) Math.round(fourWeekFloat);
            twelfthWeekAverage = (int) Math.round(twelfthWeekFloat);


            System.out.println("week is" + weekCount + " and minutes is for four weeks " + fourWeekAverage + "for email " + contact.getLogin() + "Time in " + startDate + " " + endDate);
            System.out.println("week is" + weekCount + " and minutes is for twelve weeks " + twelfthWeekAverage + "for email " + contact.getLogin() + "Time in " + startDate + " " + endDate);
            LearningStatsAverage averageEntity = mapUserDataAverageWeek(fourWeekAverage, twelfthWeekAverage, contact);


            /////   save entity to datastore
            saveUserStats(averageEntity);
        }
    }

    private static LearningStatsAverage mapUserDataAverageWeek(int fourWeekAverage, int twelfthWeekAverage, Contacts contact) {

        LearningStatsAverage averageEntity = new LearningStatsAverage();

        averageEntity.setUserId(contact.getId());
        averageEntity.setFourWeekAvg(fourWeekAverage);
        averageEntity.setTwelveWeekAvg(twelfthWeekAverage);
        averageEntity.setEmail(contact.getLogin());
        return averageEntity;
    }
}