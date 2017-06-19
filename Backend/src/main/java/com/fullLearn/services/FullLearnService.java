package com.fullLearn.services;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.*;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.Frequency;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.helpers.HTTP;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;

import com.fullLearn.beans.LearningStats;
import com.googlecode.objectify.cmd.Query;


public class FullLearnService {




    public static boolean fetchAllUserStats() throws IOException {
        System.out.println("fetchUserDetails ");
        int count=0;
String cursorStr=null;
        do {

            Query<Contacts> query = ofy().load().type(Contacts.class).limit(30);

            // String cursorStr = request.getParameter("cursor");
            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = query.iterator();

            List<Contacts> contactList = query.list();
                count=count+contactList.size();
            System.out.println("usercount : "+count);
            System.out.println("size :"+contactList.size());

            if (contactList.size() < 1) {
                return true;
            }

            fetchDataByBatch(iterator);
            cursorStr = iterator.getCursor().toWebSafeString();


        }while(cursorStr!=null);

        return true;
    } // end of fetchUserDetails

    public static void fetchDataByBatch(QueryResultIterator contactList) throws IOException {
        //To do for iterating and getting data for each user by Calling HTTP class in helper package





        while(contactList.hasNext()) {

Contacts contact= (Contacts) contactList.next();

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

            // email will be dynamic for contacts pojo
            ///// Start time will be dynamic and will be yesterdays date of event and endTime will also be dynamic and and will current time .

            String url = "https://mint4-dot-live-adaptivecourse.appspot.com/v1/completedMinutes?apiKey=b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d&email=" + contact.getLogin() + "&startTime=" + startDate + "&endTime=" + endDate;
            String methodType = "POST";
            String payLoad = "";
            String contentType = "application/json";

            Map<String, Object> dataMap = HTTP.request(url, methodType, payLoad, contentType);


           LearningStats dailyEntity= MapUserDataAfterFetch(dataMap, contact, startDate, endDate);
            // save daily entity to datastore
                 saveUserStats(dailyEntity);
        }


    } // end of fetchDataByBatch method


    public static void saveUserStats(Object entry) {

        ofy().save().entity(entry).now();
        // end of storeUserActivityDetail method
    }

    public static LearningStats MapUserDataAfterFetch(Map dataMap, Contacts contact, long startDate, long endDate) throws IOException {

        ObjectMapper objectmapper = new ObjectMapper();
        System.out.println("mapuser dataafer fetch");
        // properties of LearningStats pojo to be map
        // 1. id
        // 2. userid
        // 3. minutes
        // 4. challenges completed
        // 5 .frequency
        // 6. endTime
        // 7. startTime
        // 8. challenges details
        // 9. email


LearningStats dailyEntity = new LearningStats();
        if ((boolean) dataMap.get("response") && dataMap.get("status").equals("Success")) {



            // 1. unique id
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            System.out.println("id = " + id);
            dailyEntity.setId(id);
            System.out.println("id :" + dailyEntity.getId());
            //  2. userid

            dailyEntity.setUserId(contact.getId());
            System.out.println("userid :" + dailyEntity.getUserId());
            System.out.println("contact id " + contact.getId());

            // 6 and 7 startTime and endTime

            dailyEntity.setStartTime(startDate);
            System.out.println("start :" + dailyEntity.getStartTime());
            dailyEntity.setEndTime(endDate);

            //  5. frequency for daily entrys
            dailyEntity.setFrequency(Frequency.DAY);
            System.out.println("freq :" + dailyEntity.getFrequency());

            //9. email
            dailyEntity.setEmail(contact.getLogin());
            // 3,4,8 for minutes and challenges


            Map<String, Object> mapToLearningStats = (Map<String, Object>) dataMap.get("data");
            Map<String, Object> emailMap = (Map<String, Object>) mapToLearningStats.get(contact.getLogin());

            if (emailMap == null) {
                dailyEntity.setMinutes(0);
                dailyEntity.setChallenges_completed(0);
            } else {
                System.out.println("email " + contact.getLogin());
                System.out.println("emailmap " + emailMap);
                dailyEntity.setMinutes((int) emailMap.get("minutes"));
                dailyEntity.setChallenges_completed((int) emailMap.get("challenges_completed"));

                System.out.println("minutes :" + dailyEntity.getMinutes());


                //////  store entry object to datastore
                System.out.println("email id " + contact.getLogin());
                System.out.println("name " + contact.getFirstName());
                System.out.println(dailyEntity.getId() + " " + dailyEntity.getFrequency() + "" + dailyEntity.getMinutes());
            }



        }// end of if


        return dailyEntity;
    } // end of MapUserDataAfterFetch


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
        int usercount=0;
        //MailDispatcher.sendEmail();
        String cursorStr=null;
        do {

              Query<Contacts> contactQuery= ofy().load().type(Contacts.class).limit(30);

           // Query query=  ofy().load().type(LearningStats.class).filter("userId", keys).filter("startTime >=", startDate).filter("startTime <=",endDate).limit(30);

            // String cursorStr = request.getParameter("cursor");
            if (cursorStr != null)
                 contactQuery= contactQuery.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = contactQuery.iterator();

            List<Contacts> contactList=contactQuery.list();

            usercount = usercount+contactList.size();

            System.out.println("userscount: "+usercount);
            System.out.println("size :"+contactList.size());

            if (contactList.size() < 1) {
                return true;
            }

                generateWeeklyReportAllUserByBatch(iterator,startDate,endDate);
            cursorStr = iterator.getCursor().toWebSafeString();

        }while(cursorStr!=null);


        return true;
    }

    private static void generateWeeklyReportAllUserByBatch(QueryResultIterator<Contacts> iterator, long startDate, long endDate) throws JsonProcessingException {


        while (iterator.hasNext()) {

            Contacts contact = iterator.next();

            List<LearningStats> weeklyStateUser = ofy().load().type(LearningStats.class).filter("userId ==", contact.getId()).filter("startTime >=", startDate).filter("startTime <=", endDate).list();


//Query<LearningStats> weeklyLearningStats = ofy().load().type(LearningStats.class);
            Iterator weeklyStatsIterator = weeklyStateUser.iterator();
            int minutesAggregation = 0;
            int challengeCompletedAggregation = 0;
            while (weeklyStatsIterator.hasNext()) {

                    LearningStats userStats= (LearningStats) weeklyStatsIterator.next();
                minutesAggregation = minutesAggregation + userStats.getMinutes();
                challengeCompletedAggregation = challengeCompletedAggregation + userStats.getChallenges_completed();


                LearningStats weeklyEntity = mapUserWeeklyStats(userStats, minutesAggregation, challengeCompletedAggregation, startDate, endDate);

                System.out.println(new ObjectMapper().writeValueAsString(weeklyEntity));
                //// storing weekly entity to datastore

                saveUserStats(weeklyEntity);

                /// send email
                //MailDispatcher.sendEmail(contact,weeklyEntity);
            }
            System.out.println(new ObjectMapper().writeValueAsString(contact));

        }
    }

    private static LearningStats mapUserWeeklyStats(LearningStats userStats, int minutesAggregation, int challengeCompletedAggregation, long startDate, long endDate) {

        LearningStats weeklyEntity = new LearningStats();


        // unique id
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        System.out.println("id = " + id);
        weeklyEntity.setId(id);
        System.out.println("id :" + weeklyEntity.getId());
        //   userid

        weeklyEntity.setUserId(userStats.getId());
        System.out.println("userid :" + weeklyEntity.getUserId());
        System.out.println("contact id " + userStats.getId());

        // startTime and endTime

        weeklyEntity.setStartTime(startDate);
        System.out.println("start :" + weeklyEntity.getStartTime());
        weeklyEntity.setEndTime(endDate);

        //  frequency for daily entrys
        weeklyEntity.setFrequency(Frequency.WEEK);
        System.out.println("freq :" + weeklyEntity.getFrequency());

       // email
        weeklyEntity.setEmail(userStats.getEmail());


        // minutes and challenges

        weeklyEntity.setMinutes(minutesAggregation);

        weeklyEntity.setChallenges_completed(challengeCompletedAggregation);



        return weeklyEntity;
    }


    public static boolean calculateAllUserStatsAverage() {

        int usercount=0;
        //MailDispatcher.sendEmail();
        String cursorStr=null;
        do {

            Query<Contacts> contactQuery= ofy().load().type(Contacts.class).limit(30);


            if (cursorStr != null)
                contactQuery= contactQuery.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = contactQuery.iterator();

            List<Contacts> contactList=contactQuery.list();

            usercount = usercount+contactList.size();

            System.out.println("userscount: "+usercount);
            System.out.println("size :"+contactList.size());

            if (contactList.size() < 1) {
                return true;
            }

            calculateAverage(iterator);
            cursorStr = iterator.getCursor().toWebSafeString();

        }while(cursorStr!=null);


        return true;


    }

    private static void calculateAverage(QueryResultIterator<Contacts> iterator) {


int day=7*12;
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



        while(iterator.hasNext())
        {

            Contacts contact = iterator.next();

            List<LearningStats> StateUser = ofy().load().type(LearningStats.class).filter("userId ==", contact.getId()).filter("startTime >=", startDate).filter("startTime <=", endDate).filter("frequency ==",Frequency.WEEK).list();

            Iterator WeekAverageIterator =StateUser.iterator();
            int weekCount=1;
            int fourWeekAverage=0;
            int twelfthWeekAverage=0;
            while(WeekAverageIterator.hasNext()) {
                LearningStats userStats = (LearningStats) WeekAverageIterator.next();

                    if(weekCount<=8)
                    twelfthWeekAverage =  twelfthWeekAverage + userStats.getMinutes();

                    else{
                    twelfthWeekAverage = twelfthWeekAverage + userStats.getMinutes();
                    fourWeekAverage = fourWeekAverage + userStats.getMinutes();
                    }
                    weekCount++;
            }
            fourWeekAverage=fourWeekAverage/4;
            twelfthWeekAverage=twelfthWeekAverage/12;


            LearningStatsAverage averageEntity=mapUserDataAverage(fourWeekAverage,twelfthWeekAverage,contact);


            /////   save entity to datastore
                saveUserStats(averageEntity);
        }



    }

    private static LearningStatsAverage mapUserDataAverage(int fourWeekAverage, int twelfthWeekAverage, Contacts contact) {


        LearningStatsAverage averageEntity=new LearningStatsAverage();

        averageEntity.setUserid(contact.getId());
        averageEntity.setFourthWeek(fourWeekAverage);
        averageEntity.setTwelfthWeek(twelfthWeekAverage);
        return averageEntity;
    }


}

	/*
9:41 PM	Error running fl backend run: No task to execute is specified

9:48 PM	Error running fl backend run: No task to execute is specified

9:49 PM	Error running fl backend run: No task to execute is specified

11:00 PM	Error running fl backend run: No task to execute is specified
	{



	*/

