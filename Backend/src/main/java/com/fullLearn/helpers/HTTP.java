package com.fullLearn.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HTTP {

	
	public static Map<String,Object> request(String url, String methodType,String contentType) throws IOException
	{
int tryCount=1;
	Map<String, Object> dataMap = new HashMap<String, Object>();

	URL _url = new URL(url);

	ObjectMapper objectmapper = new ObjectMapper();
	HttpURLConnection con = (HttpURLConnection) _url.openConnection();
	con.setRequestMethod(methodType);
	con.setRequestProperty("content-type", contentType);
	con.setConnectTimeout(30000);
	System.out.println("timeout time in " + 3 + " sec");
	BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

	StringBuffer sb = new StringBuffer();
	String line;
	try {

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	String data = sb.toString();
	dataMap = objectmapper.readValue(data, new TypeReference<Map<String, Object>>() {
	});
	System.out.println("datamap " + dataMap);
	return dataMap;




	}


}
