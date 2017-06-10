package com.fullLearn.services;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import com.fullLearn.beans.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;



public class ContactServices {

	static
	{
		ObjectifyService.register(Contacts.class);
	}


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

//	public Long getLastModifiedContacts()
//	{
//		Long st = null;
//		Query<Contacts> all = ofy().load().type(Contacts.class).order("-modifiedAt");
//		for(Contacts cc : all)
//	    st = cc.getModifiedAt();
//		return st;
//
//	}




	public ArrayList<String> getURLContacts(String accesstoken) throws IOException
	{

		int limit = 30;
		URL url = new URL("https://api-dot-staging-fullspectrum.appspot.com/api/v1/account/SEN42/user?limit="+limit);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization","Bearer " +accesstoken);
		con.setDoOutput(true);

		String line, contacts = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		if ((line = reader.readLine()) != null) {
			contacts += line;
			// Mapping JSON

			ObjectMapper obj = new ObjectMapper();
			Map<String,String> map = obj.readValue(contacts.toString(),new TypeReference<Map<String,Object>>(){});
			Map<String,String> datas = obj.readValue(obj.writeValueAsString(map.get("data")), new TypeReference<Map<String,Object>>(){});
			ArrayList<String> userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Object>>(){});

			// Map<String,String> cursorData = obj.readValue(obj.writeValueAsString(datas.get("cursor")), new TypeReference<Map<String,String>>(){});

			return userData;

		}
		else{
			return null;
		}
	}



	public String saveContacts(ArrayList<String> contacts) throws IOException
	{


		ObjectMapper obj = new ObjectMapper();
		for (int i=0;i<contacts.size();i++)
		{
			Contacts users = obj.readValue(obj.writeValueAsString(contacts.get(i)),new TypeReference<Contacts>(){});

			ofy().save().entity(users).now();
		}
		return "Saved successfully";
	}

}
