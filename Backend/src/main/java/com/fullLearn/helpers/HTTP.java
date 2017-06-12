package com.fullLearn.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HTTP {

	
	public static Map<String,Object> getUserActivities(String email,long startTime,long endTime) throws IOException
	{
		URL url=new URL("https://mint4-dot-live-adaptivecourse.appspot.com/v1/completedMinutes?apiKey=b2739ff0eb7543e5a5c43e88f3cb2a0bd0d0247d&email="+email+"&startTime="+startTime+"&endTime="+endTime);

		ObjectMapper objectmapper =new ObjectMapper();
		HttpURLConnection con= (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		 
		BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		StringBuffer sb=new StringBuffer();
		String line;
		try
		{
		
	      while ((line = br.readLine()) != null)
	      {
	        sb.append(line);
	      }
	      br.close();
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
		String data=sb.toString();
		Map<String,Object> dataMap=objectmapper.readValue(data,new TypeReference<Map<String,Object>>(){});

		return dataMap;
		
	}
}
