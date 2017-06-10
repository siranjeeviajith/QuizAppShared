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
import com.googlecode.objectify.ObjectifyService;
import com.fullLearn.beans.LearningStats;
import com.googlecode.objectify.cmd.Query;


public class FullLearnService {



	long countDay;
	long countWeek;
	long countMonth;
	long countYear;
	/*public static String cursorStr=null;
	public static boolean fetchUserDetails() throws JsonParseException, JsonMappingException, IOException
	{

		Query<Contacts> query = ofy().load().type(Contacts.class).limit(30);
		if(cursorStr!=null) {

			query = query.startAt(Cursor.fromWebSafeString(cursorStr));

			QueryResultIterator<Contacts> contacts= query.iterator();
			if(contacts.size()==0) {
				return true;
			}else{

				fetchDataByBatch(contacts);
				cursorStr=contacts.getCursor().toWebSafeString();
				fetchUserDetails();
			}
		}
		else {
			QueryResultIterator<Contacts> contacts = query.iterator();
			if(iterator.size()==0)
			{
				return true;
			}
			else
			{
				fetchDataByBatch(contacts);
				cursorStr=contacts.getCursor().toWebSafeString();
				fetchUserDetails();
			}


		}



		// LearningStats class properties
		// id
		//userid
		//minutes
		//challenges
		//frequency
		//start date
		//end date




		*//*System.out.println("data is "+data);
		ObjectMapper obj=new ObjectMapper();

		Map<String,Object> details=new HashMap<String,Object>();
		details=obj.readValue(data,	new TypeReference<Map<String,Object>>(){});
		details=(Map<String, Object>) details.get("data");

		*//*
		// Creating map for useremail data
	*//*
		Map<String,Object> emailMap=new HashMap<String,Object>();
		emailMap=obj.readValue(obj.writeValueAsString(details), new  TypeReference<Map<String,Object>>(){});

		
		
		////  Minutes, challenges and challenges details
		
		Map<String,Object> userSpecificData=new HashMap<String,Object>();
		userSpecificData=(Map<String, Object>) emailMap.get(email);

		  UUID uuid = UUID.randomUUID();
	        String id = uuid.toString();
		// minutes, challenges, challenges details,
			LearningStats user =obj.readValue(obj.writeValueAsString(userSpecificData), new TypeReference<LearningStats>(){} );
			user.setUserId(userid);
			user.setId(id);
			Date d=new Date();
			long s=d.getTime();
			user.setStartTime(s);
			user.setEndTime(s);


			user.setFrequency(LearningStats.Frequency.DAY);
			ObjectifyService.register(LearningStats.class);
			ofy().save().entity(user).now();



		return "ok";
*//*
	}
	


	public static void fetchDataByBatch(QueryResultIterator contacts)
	{
		//To do for iterating and getting data for each user by Calling HTTP class in helper package

		///  To do for calling method to store data in datastore for each user
	}
*/
	
	/*
9:41 PM	Error running fl backend run: No task to execute is specified

9:48 PM	Error running fl backend run: No task to execute is specified

9:49 PM	Error running fl backend run: No task to execute is specified

11:00 PM	Error running fl backend run: No task to execute is specified
	{
		
		
		
	}*/
	
}
