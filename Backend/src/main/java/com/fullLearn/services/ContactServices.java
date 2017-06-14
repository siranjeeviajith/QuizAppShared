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
	SaveContacts saveContacts = new SaveContacts();

	public String getAccessToken() throws IOException {

		String refresh_tkn = "32915f76aa3WgtVbAzss-26W1KMIdU7WqO4cLE5rRXwfl";
		String cid = "2a9ac-5ea21026a3973e4c3a56a294f2e47d88";
		String cscrt = "e7m9Gb9nUzLoaCWXy8OdBI6zlh7cx8OmUXMeXRMh";

		URL url = new URL("https://staging-fullcreative-dot-full-auth.appspot.com/o/oauth2/v1/token");
		String params = "refresh_token=" + refresh_tkn + "&client_id=" + cid + "&client_secret=" + cscrt + "&grant_type=refresh_token";
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


	public boolean syncContacts(Long lastModified, String accesstoken) throws IOException {
		ObjectMapper obj = new ObjectMapper();
		URL url = new URL("https://api-dot-staging-fullspectrum.appspot.com/api/v1/account/SEN42/user?since=" + lastModified);
		String methodType = "GET";
		String contentType = "application/json";

		Map<String, String> datas = listOfDatas.request(accesstoken, url, methodType, contentType);

		ArrayList<Contacts> userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Contacts>>() {
		});

		saveContacts.saveContacts(userData);
		if (userData.size() != 0) {
			boolean status = saveContacts.saveContacts(userData);
			return status;
		} else {
			return false;
		}


	}


	public boolean saveAllContacts(String accesstoken, int limit, String cursorStr) throws IOException {
		ObjectMapper obj = new ObjectMapper();
		URL url = null;
		ArrayList<Contacts> userData;
		if (cursorStr == null || cursorStr.equals("")) {

			url = new URL("https://api-dot-staging-fullspectrum.appspot.com/api/v1/account/SEN42/user?limit="+limit);

		} else {
			url = new URL("https://api-dot-staging-fullspectrum.appspot.com/api/v1/account/SEN42/user?limit="+limit+"&cursor="+cursorStr);
		}


		String methodType = "GET";
		String contentType = "application/json";

		Map<String, String> datas = listOfDatas.request(accesstoken, url, methodType, contentType);

		String cursor = obj.readValue(obj.writeValueAsString(datas.get("cursor")), new TypeReference<String>() {});
		userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Contacts>>() {});


		boolean status = saveContacts.saveContacts(userData);

		/*System.out.println("Cursor: "+cursor);
		System.out.println("size: "+userData.size());*/

		if (userData.size() < limit)
		{
			return true;
		}

		saveAllContacts(accesstoken, limit, cursor);
		return true;
	}



}
