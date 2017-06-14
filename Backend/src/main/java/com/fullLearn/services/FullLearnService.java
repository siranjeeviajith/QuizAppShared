package com.fullLearn.services;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.*;






import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.Frequency;
import com.fullLearn.helpers.HTTP;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Index;
import com.google.appengine.api.datastore.QueryResultIterator;

import com.fullLearn.beans.LearningStats;
import com.googlecode.objectify.cmd.Query;


public class FullLearnService {

    public static boolean fetchUserDetails() throws  IOException {

        System.out.println("fetchUserDetails ");
        List<LearningStats> contacts=ofy().load().type(LearningStats.class).list();
        String cursorStr = null;

        QueryResultIterator<LearningStats> contactsByBatch=null;
        int limit=30;
        do {
            ////// LearningStates is for Demo after getting all merged it would be Contacts.class
            //////  this cursor is getting limit of 30 per batch

            Query<LearningStats> query = ofy().load().type(LearningStats.class).limit(limit);
            if (cursorStr != null) {
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));
                contactsByBatch= query.iterator();
                //cursorStr = contactsByBatch.getCursor().toWebSafeString();
            }

            else
            {
                contactsByBatch= query.iterator();
            }

            List<Index> index=contactsByBatch.getIndexList();
            int size=index.size();
            System.out.println("size = " +size);
                    if(size<limit)
                    {
                            cursorStr =null;
                    }
                    else
                    {
                        cursorStr = contactsByBatch.getCursor().toWebSafeString();
                    }
            fetchDataByBatch(contactsByBatch);

        } while (cursorStr != null);
        return true;
    } // end of fetchUserDetails method


	public static void fetchDataByBatch(QueryResultIterator contacts) throws IOException {
		//To do for iterating and getting data for each user by Calling HTTP class in helper package
  System.out.println("fetchdataby bactch");
        Date d = new Date();// current date
        long endDate=d.getTime();// endDate for fetching user data
        Date dateBefore = new Date(d.getTime() - 1 * 24 * 3600 * 1000  );
        long startDate=dateBefore.getTime();// start date for fetching user data

        while(contacts.hasNext())
        {
            LearningStats contact= (LearningStats) contacts.next();
                // email will be dynamic for contacts pojo
            ///// Start time will be dynamic and will be yesterdays date of event and endTime will also be dynamic and and will current time .

            String url="https://mint4-dot-live-adaptivecourse.appspot.com/v1/completedMinutes?apiKey=b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d&email=shaikanjavali.mastan@a-cti.com"+"&startTime="+startDate+"&endTime="+endDate;
            String methodType="POST";
            String payLoad="";
            String contentType="application/json";

            Map<String,Object> dataMap= HTTP.request(url,methodType,payLoad,contentType);

            MapUserDataAfterFetch(dataMap,contact,startDate,endDate);






        } // end of while



	} // end of fetchDataByBatch method


	public static void storeUserActivityDetail(LearningStats entry)
    {


        System.out.println("store data");
        ofy().save().entity(entry).now();
    }// end of storeUserActivityDetail method


	public static void MapUserDataAfterFetch(Map dataMap,LearningStats contact, long startDate,long endDate) throws IOException {

        ObjectMapper objectmapper=new ObjectMapper();
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

        LearningStats entry=new LearningStats();
        if((boolean)dataMap.get("response") && dataMap.get("status").equals("Success"))
        {



            // 1. unique id
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            entry.setId(id);

            //  2. userid

            entry.setUserId(contact.getUserId());


            // 6 and 7 startTime and endTime

            entry.setStartTime(startDate);
            entry.setEndTime(endDate);

            //  5. frequency for daily entry
            entry.setFrequency(Frequency.DAY);


            // 3,4,8 for minutes and challenges
            Map<String,Object> mapToLearningStats= (Map<String, Object>) dataMap.get("data");
            entry= objectmapper.readValue(objectmapper.writeValueAsString(mapToLearningStats),new TypeReference<LearningStats>(){});

            //////  store entry object to datastore
            storeUserActivityDetail(entry);

            ///  Current Month Max days
/*
            Calendar c = Calendar.getInstance();
            int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

            /// current day of month
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);




            ////  current day of year

            int currentDayOfYear=c.get(Calendar.DAY_OF_YEAR);

            /////  Max days in current year

            int maxDaysCurrentYear=c.getActualMaximum(Calendar.DAY_OF_YEAR);



            if (dayOfMonth % 7 == 0 && dayOfMonth != monthMaxDays && currentDayOfYear != maxDaysCurrentYear )
            {
                /// new Entity will be created
                LearningStats weeklyCalculation = new LearningStats();

                weeklyCalculation.setFrequency(LearningStats.Frequency.WEEK);



                // 1. unique id
                UUID uuidCalculation = UUID.randomUUID();
                String idCalculation = uuidCalculation.toString();
                weeklyCalculation.setId(idCalculation);

                //// To do calculation for week will done here.............



            }

            else if(dayOfMonth == monthMaxDays && currentDayOfYear != maxDaysCurrentYear)
            {
                LearningStats monthlyCalculation = new LearningStats();

                monthlyCalculation.setFrequency(LearningStats.Frequency.MONTH);

                ///  To do calculation for month will done here ......................
            }

            else if(currentDayOfYear != maxDaysCurrentYear)
            {
                LearningStats yearlyCalculation = new LearningStats();
                yearlyCalculation.setFrequency(LearningStats.Frequency.YEAR);

                ////  To do calculation for year will be done year .........................

            }*/
        }// end of if

    } // end of MapUserDataAfterFetch



    /////////////////////////     WEEKLY REPORTS



    public static boolean generateWeeklyReport()
    {


        return true;
    }

	/*
9:41 PM	Error running fl backend run: No task to execute is specified

9:48 PM	Error running fl backend run: No task to execute is specified

9:49 PM	Error running fl backend run: No task to execute is specified

11:00 PM	Error running fl backend run: No task to execute is specified
	{



	*/
}
