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
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.fullLearn.beans.LearningStats;
import com.googlecode.objectify.cmd.Query;


public class FullLearnService {


    long countDay;
    long countWeek;
    long countMonth;
    long countYear;

    /**/;
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

	public static void fetchDataByBatch(QueryResultIterator contacts)
	{
		//To do for iterating and getting data for each user by Calling HTTP class in helper package

		///  To do for calling method to store data in datastore for each user
	}


	/*
9:41 PM	Error running fl backend run: No task to execute is specified

9:48 PM	Error running fl backend run: No task to execute is specified

9:49 PM	Error running fl backend run: No task to execute is specified

11:00 PM	Error running fl backend run: No task to execute is specified
	{



	*/
}
