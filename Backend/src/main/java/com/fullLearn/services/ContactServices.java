package com.fullLearn.services;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.*;
import com.fullLearn.beans.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.helpers.HTTPUrl;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

public class ContactServices{

	static
	{
		ObjectifyService.register(Contacts.class);
	}


	HTTPUrl listOfDatas = new HTTPUrl();
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

	public Long getLastModifiedContacts()throws IOException
	{
		Query<Contacts> all = ofy().load().type(Contacts.class).order("-modifiedAt").limit(1);
		List<Contacts> list = all.list();
		Long lastModified;
		if(list != null)
		{
			lastModified = list.get(0).getModifiedAt();
			return lastModified;
		}
		else
		{
			return null;
		}

	}


	public ArrayList<Contacts> syncContacts(Long lastModified, String accesstoken)throws IOException
	{
		ObjectMapper obj = new ObjectMapper();
		URL url = new URL("https://api-dot-staging-fullspectrum.appspot.com/api/v1/account/SEN42/user?since="+lastModified);
		String methodType = "GET";
		String contentType = "application/json";

		Map<String, String> datas = listOfDatas.request(accesstoken, url,methodType,contentType);

		ArrayList<Contacts> userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Contacts>>(){});
		return userData;


	}



	public ArrayList<Contacts> saveAllContacts(String accesstoken,int limit) throws IOException
	{
		String cursorStr = null;
		do
		{
			ObjectMapper obj = new ObjectMapper();
			URL url = new URL("https://api-dot-staging-fullspectrum.appspot.com/api/v1/account/SEN42/user?limit="+limit+"&cursor"+cursorStr);
			String methodType = "GET";
			String contentType = "application/json";

			Map<String, String> datas = listOfDatas.request(accesstoken, url,methodType,contentType);

			cursorStr = obj.readValue(obj.writeValueAsString(datas.get("cursor")), new TypeReference<Map<String,String>>(){});
			ArrayList<Contacts> userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Contacts>>(){});
			if(userData.size() < limit)
				cursorStr = null;
			return userData;
		}while(cursorStr != null);

	}






	public boolean saveContacts(ArrayList<Contacts> contacts) throws IOException
	{
		ObjectMapper obj = new ObjectMapper();
		boolean status = false;
		for (int i=0;i<contacts.size();i++)
		{
			Contacts users = obj.readValue(obj.writeValueAsString(contacts.get(i)),new TypeReference<Contacts>(){});
			status = ofy().save().entity(users).now() != null;
		}
		return status;
	}
}
