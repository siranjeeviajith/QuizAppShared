package controller;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.ObjectifyService;

import beans.*;

public class ContactsController {
   
	public String getAccessToken() throws IOException
	{
		
		String refresh_tkn = "32915f76aa3WgtVbAzss-26W1KMIdU7WqO4cLE5rRXwfl";
        String cid = "2a9ac-5ea21026a3973e4c3a56a294f2e47d88";
        String cscrt = "e7m9Gb9nUzLoaCWXy8OdBI6zlh7cx8OmUXMeXRMh";
        
	        URL url = new URL("https://staging-fullcreative.fullauth.com/o/oauth2/v1/token");
	        String params = "refresh_token="+refresh_tkn+"&client_id="+cid+"&client_secret="+cscrt+"&grant_type=refresh_token";
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        
	        con.setRequestMethod("POST");
	        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        con.setDoOutput(true);
            
	        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
	        wr.write(params);
	        wr.flush();
	      
	        System.out.println(con.getResponseCode());
	        System.out.println(con.getResponseMessage()); 
		
		// Get the response
        String tokens = new String();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            tokens+=line;
        }

        //Output the response
        System.out.println(tokens);
        
        // Mapping JSON
        ObjectMapper obj = new ObjectMapper();
        
        TokenAccess pojo= obj.readValue(tokens, TokenAccess.class);
        System.out.println(pojo.getAccess_token());
		String accesstoken = pojo.getAccess_token();		
		return accesstoken;
	}
	
	
	public String getContacts(String accesstoken) throws IOException
	{
        URL url = new URL("https://api-dot-staging-fullspectrum.appspot.com/api/v1/account/SEN42/user?limit=1");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization","Bearer " +accesstoken);
		con.setDoOutput(true);
		String line, datas = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		while ((line = reader.readLine()) != null) {
		datas += line;
		}
		return datas;
	}
	
	public Contacts saveContacts(String contacts) throws IOException
	{
		// Mapping JSON
		ObjectMapper obj = new ObjectMapper();
		Map<String,String> map = obj.readValue(contacts.toString(),new TypeReference<Map<String,Object>>(){});
		Map<String,String> datas = obj.readValue(obj.writeValueAsString(map.get("data")), new TypeReference<Map<String,Object>>(){});
		ArrayList<String> userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Object>>(){});
	
	//	Map<String,String> cursorData = obj.readValue(obj.writeValueAsString(datas.get("cursor")), new TypeReference<Map<String,String>>(){});
		
		
		Contacts users = obj.readValue(obj.writeValueAsString(userData.get(0)),new TypeReference<Contacts>(){});
		
		
		
		ObjectifyService.register(Contacts.class);
		ofy().save().entity(users).now();
		
		return users;
	}
	
}
