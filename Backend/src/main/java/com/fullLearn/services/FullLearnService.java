package com.fullLearn.services;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.Frequency;
import com.fullLearn.helpers.HTTP;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;

import com.fullLearn.beans.LearningStats;
import com.google.appengine.api.datastore.QueryResultList;
import com.googlecode.objectify.cmd.Query;

import javax.servlet.http.HttpServletResponse;


public class FullLearnService {




    public static boolean fetchAllUserStats() throws IOException {
        System.out.println("fetchUserDetails ");
String cursorStr=null;
        do {

            Query<Contacts> query = ofy().load().type(Contacts.class).limit(10);

            // String cursorStr = request.getParameter("cursor");
            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = query.iterator();

            List<Contacts> contactList = query.list();

            System.out.println("size :"+contactList.size());

            if (contactList.size() < 1) {
                return true;
            }

            fetchDataByBatch(iterator);
            cursorStr = iterator.getCursor().toWebSafeString();


        }while(cursorStr!=null);
        /*
        while (iterator.hasNext()) {
            Contacts contact = iterator.next();
            fetchDataByBatch(contact);
            continu = true;
        }

        if (continu) {
            cursorStr = iterator.getCursor().toWebSafeString();
            fetchAllUserStats(cursorStr);
        }
        else
        {
            return true;
        }
*/
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
            cal1.set(Calendar.SECOND, 0);
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

            MapUserDataAfterFetch(dataMap, contact, startDate, endDate);


        }


    } // end of fetchDataByBatch method


    public static void storeUserActivityDetail(LearningStats entry) {


    }// end of storeUserActivityDetail method


    public static void MapUserDataAfterFetch(Map dataMap, Contacts contact, long startDate, long endDate) throws IOException {

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


        if ((boolean) dataMap.get("response") && dataMap.get("status").equals("Success")) {

            LearningStats entry = new LearningStats();

            // 1. unique id
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            System.out.println("id = " + id);
            entry.setId(id);
            System.out.println("id :" + entry.getId());
            //  2. userid

            entry.setUserId(contact.getId());
            System.out.println("userid :" + entry.getUserId());
            System.out.println("contact id " + contact.getId());

            // 6 and 7 startTime and endTime

            entry.setStartTime(startDate);
            System.out.println("start :" + entry.getStartTime());
            entry.setEndTime(endDate);

            //  5. frequency for daily entrys
            entry.setFrequency(Frequency.DAY);
            System.out.println("freq :" + entry.getFrequency());
            entry.setEmail(contact.getLogin());
            // 3,4,8 for minutes and challenges
            Map<String, Object> mapToLearningStats = (Map<String, Object>) dataMap.get("data");
            Map<String, Object> emailMap = (Map<String, Object>) mapToLearningStats.get(contact.getLogin());

            if (emailMap == null) {
                entry.setMinutes(0);
                entry.setChallenges_completed(0);
            } else {
                System.out.println("email " + contact.getLogin());
                System.out.println("emailmap " + emailMap);
                entry.setMinutes((int) emailMap.get("minutes"));
                entry.setChallenges_completed((int) emailMap.get("challenges_completed"));

                System.out.println("minutes :" + entry.getMinutes());


                //////  store entry object to datastore
                System.out.println("email id " + contact.getLogin());
                System.out.println("name " + contact.getFirstName());
                System.out.println(entry.getId() + " " + entry.getFrequency() + "" + entry.getMinutes());
            }
            ofy().save().entity(entry).now();


        }// end of if


    } // end of MapUserDataAfterFetch


    /////////////////////////     WEEKLY REPORTS


    public static boolean generateWeeklyReport() {

            sendMailUser();
        return true;
    }


    public static void  sendMailUser()
    {

        String fromEmail="amandeep.pannu8233@gmail.com";
        String userName="amandeep.pannu8233";
        String password="Chandela8859@#";

    }

	/*
9:41 PM	Error running fl backend run: No task to execute is specified

9:48 PM	Error running fl backend run: No task to execute is specified

9:49 PM	Error running fl backend run: No task to execute is specified

11:00 PM	Error running fl backend run: No task to execute is specified
	{



	*/
}
