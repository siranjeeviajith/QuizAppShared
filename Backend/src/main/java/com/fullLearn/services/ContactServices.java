package com.fullLearn.services;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.io.*;
import java.net.*;
import java.util.*;
import com.fullLearn.beans.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.helpers.*;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

public class ContactServices {

	static {
		ObjectifyService.register(Contacts.class);
	}


	// helpers
	HTTPUrl listOfDatas = new HTTPUrl();
	SaveContactsHelper saveContactsHelper = new SaveContactsHelper();


	public String getAccessToken() throws IOException {

			URL url = new URL(Constants.FULL_AUTH_URL+"/o/oauth2/v1/token");
			String params = "refresh_token="+Constants.REFRESH_TOKEN+"&client_id="+Constants.CLIENT_ID+"&client_secret="+Constants.CLIENT_SECRET+"&grant_type=refresh_token";
		

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setDoOutput(true);

		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(params);
		wr.flush();

		// Get the response
		String tokens = new String();
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			tokens += line;
		}

		//Output the response
		System.out.println(tokens);

		// Mapping JSON
		ObjectMapper obj = new ObjectMapper();

		TokenAccess pojo = obj.readValue(tokens, TokenAccess.class);
		//System.out.println(pojo.getAccess_token());
		String accesstoken = pojo.getAccess_token();
		return accesstoken;
	}

	public Long getLastModifiedContacts() {
		try {
			Query<Contacts> all = ofy().load().type(Contacts.class).order("-modifiedAt").limit(1);
			List<Contacts> list = all.list();

			if (list.size() != 0) {
				Long lastModified = list.get(0).getModifiedAt();
				return lastModified;
			} else {
				return null;
			}
		} catch (Exception ex) {
			System.out.println("exception: " + ex);
			return null;
		}

	}


	public boolean saveAllContacts(Long lastModified, String accesstoken, int limit, String cursorStr) throws IOException {
		ObjectMapper obj = new ObjectMapper();
		String baseUrl = Constants.AW_API_URL+"/api/v1/account/SEN42/user?limit="+limit;
		ArrayList<Contacts> userData;
		String cursor = null;
		String url = null;

		try {
			if(lastModified != null)
			{
				System.out.println("Last Modified : "+lastModified);
				url = baseUrl+"&since="+lastModified;
			}
			else if (cursorStr == null || cursorStr.equals("")) {

				url = baseUrl;

			} else {
				url = baseUrl+"&cursor="+cursorStr;
			}


			System.out.println("url : "+url);
			String methodType = "GET";
			String contentType = "application/json";

			Map<String, String> datas = listOfDatas.request(accesstoken, url, methodType, contentType, cursorStr);

			cursor = obj.readValue(obj.writeValueAsString(datas.get("cursor")), new TypeReference<String>() {});
			userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Contacts>>() {});

			boolean status = saveContactsHelper.saveContacts(userData);

			System.out.println("fetched users : "+userData.size());
			if (userData.size() < limit || userData == null) {
				return true;
			}

			saveAllContacts(lastModified, accesstoken, limit, cursor);
			return true;
		}
		catch(Exception ex)
		{
			    System.out.println(ex.getMessage());
				String cursorValue = cursor;
				System.out.println(cursorValue);
				saveAllContacts(lastModified, accesstoken, limit, cursorValue);
				return true;

		}
	}



}
