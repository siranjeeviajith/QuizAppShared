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
import com.googlecode.objectify.ObjectifyService;
import com.fullLearn.beans.LearningStats;


public class FullLearnService {

	
	long countDay;
	long countWeek;
	long countMonth;
	long countYear;
	
	public static String create(String data, String userid,String email) throws JsonParseException, JsonMappingException, IOException
	{
		
		

		ObjectMapper obj=new ObjectMapper();
		
		Map<String,Object> details=new HashMap<String,Object>();
		details=obj.readValue(data,	new TypeReference<Map<String,Object>>(){});
		details=(Map<String, Object>) details.get("data");

		
		// Creating map for useremail data
		
		Map<String,Object> emailMap=new HashMap<String,Object>();
		emailMap=obj.readValue(obj.writeValueAsString(details), new  TypeReference<Map<String,Object>>(){});

		
		
		////  Minutes, challenges and challenges details
		
		Map<String,Object> userSpecificData=new HashMap<String,Object>();
		userSpecificData=(Map<String, Object>) emailMap.get(email);

		  UUID uuid = UUID.randomUUID();
	        String id = uuid.toString();
		
			LearningStats user =obj.readValue(obj.writeValueAsString(userSpecificData), new TypeReference<LearningStats>(){} );
			user.setUserId(userid);
			user.setId(id);
			Date d=new Date();
			
			long date=d.getTime();
			System.out.println("date ="+date);

			user.setFrequency(LearningStats.Frequency.DAY);
			ObjectifyService.register(LearningStats.class);
			ofy().save().entity(user).now();
		
		return "ok";

	}
	
	
	
	///////////////////////////////////////  Get User Details

	
	/*public static List<User> getUserDetails()
	{
		
		
		
	}*/
	
}
