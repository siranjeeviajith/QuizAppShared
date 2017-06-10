package com.fullLearn.service;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.helpers.HTTP;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.googlecode.objectify.ObjectifyService;
import com.fullLearn.beans.LearningStats;
import com.googlecode.objectify.cmd.Query;


public class FullLearnService {


    long countDay;
    long countWeek;
    long countMonth;
    long countYear;


    public static boolean fetchUserDetails() throws  IOException {
        String cursorStr = null;
        do {
            ////// LearningStates is for Demo after getting all merged it would be Contacts.class
            //////  this cursor is getting limit of 30 per batch
            Query<LearningStats> query = ofy().load().type(LearningStats.class).limit(30);
            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<LearningStats> contacts = query.iterator();
            fetchDataByBatch(contacts);
            cursorStr = contacts.getCursor().toWebSafeString();
        } while (cursorStr != null);
        return true;
    }


	public static void fetchDataByBatch(QueryResultIterator contacts) throws IOException {
		//To do for iterating and getting data for each user by Calling HTTP class in helper package

        Date d = new Date();// current date
        long endDate=d.getTime();// endDate for fetching user data
        Date dateBefore = new Date(d.getTime() - 1 * 24 * 3600 * 1000  );
        long startDate=dateBefore.getTime();// start date for fetching user data

        while(contacts.hasNext())
        {
            LearningStats contact= (LearningStats) contacts.next();
                // email will be dynamic for contacts pojo
            ///// Start time will be dynamic and will be yesterdays date of event and endTime will also be dynamic and and will current time .

            String data=   HTTP.getUserActivities("shaikanjavali.mastan@a-cti.com",startDate,endDate);
            ObjectMapper objectmapper=new ObjectMapper();
            Map<String,Object> dataMap=new HashMap<String,Object>();
            dataMap=objectmapper.readValue(data,new TypeReference<Map<String,Object>>(){});

         System.out.println("data is :"+data);
           LearningStats entry= MapUserDataAfterFetch(dataMap);

            ///  To do for calling method to store data in datastore for each user
        }



	}


	public static LearningStats MapUserDataAfterFetch(Map dataMap){
        // properties of LearningStats pojo
        //id
        //userid
        //minutes
        //challenges completed
        //frequency
        //endTime
        // startTime
        LearningStats entry=new LearningStats();
        if((boolean)dataMap.get("response") && dataMap.get("status").equals("Success"))
        {

            //// To do mapping is to be done here to Learning Stats

        }
return entry;
    }

	/*
9:41 PM	Error running fl backend run: No task to execute is specified

9:48 PM	Error running fl backend run: No task to execute is specified

9:49 PM	Error running fl backend run: No task to execute is specified

11:00 PM	Error running fl backend run: No task to execute is specified
	{



	*/
}
