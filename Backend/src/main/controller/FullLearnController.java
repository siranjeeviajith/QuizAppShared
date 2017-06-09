package controller;


import helpers.PMF;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.googlecode.objectify.ObjectifyService;

import beans.StatsMatrixFullLearn;


public class FullLearnController {

	
	long count_day=1;
	long count_week;
	long count_month;
	long count_year;
	public static final PersistenceManager pm=PMF.get().getPersistenceManager();
	
	public static String create(String data, String userid,String email,HttpServletResponse resp) throws JsonParseException, JsonMappingException, IOException
	{
		
		
		PrintWriter out=resp.getWriter();
		ObjectMapper obj=new ObjectMapper();
		
		Map<String,Object> details=new HashMap<String,Object>();
		details=obj.readValue(data,	new TypeReference<Map<String,Object>>(){});
		details=(Map<String, Object>) details.get("data");
		out.println("map email  is :"+details);
		
		// Creating map for useremail data
		
		Map<String,Object> emailMap=new HashMap<String,Object>();
		emailMap=obj.readValue(obj.writeValueAsString(details), new  TypeReference<Map<String,Object>>(){});
		out.println("sdkjfhkdsjh "+emailMap.get(email));
		
		
		////  Minutes, challenges and challenges details
		
		Map<String,Object> userSpecificData=new HashMap<String,Object>();
		userSpecificData=(Map<String, Object>) emailMap.get(email);
		out.println("userspecifice data : "+userSpecificData);

		  UUID uuid = UUID.randomUUID();
	        String id = uuid.toString();
		
		StatsMatrixFullLearn user =obj.readValue(obj.writeValueAsString(userSpecificData), new TypeReference<StatsMatrixFullLearn>(){} );
			user.setUserId(userid);
			user.setId(id);
			Date d=new Date();
			
			long date=d.getTime();
			System.out.println("date ="+date);
			user.setDate(date);
			user.setFrequency("DAY");
		ObjectifyService.register(StatsMatrixFullLearn.class);
		ofy().save().entity(user).now();
		
		out.println("everything is mapped and fine");
		out.println("user details  "+user.getChallenges_completed()+" "+user.getMinutes()+" "+user.getChallenges_details());
		return "ok";

	}
	
	
	
	///////////////////////////////////////  Get User Details

	
	public static List<User> getUserDetails()
	{
		
		
		
	}
	
}
