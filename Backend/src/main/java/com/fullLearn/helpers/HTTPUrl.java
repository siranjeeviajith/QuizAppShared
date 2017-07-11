package com.fullLearn.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPUrl {

    public Map<String, String> request(String accesstoken, String urlString, String methodType, String contentType, String cursor) throws IOException {
        String cursorStr = cursor;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(60000);
            con.setRequestMethod(methodType);
            con.setRequestProperty("Content-Type", contentType);
            con.setRequestProperty("Authorization", "Bearer " + accesstoken);
            con.setDoOutput(true);

            String line, contacts = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            if ((line = reader.readLine()) != null) {
                contacts += line;

                ObjectMapper obj = new ObjectMapper();
                Map<String, String> map = obj.readValue(contacts.toString(), new TypeReference<Map<String, Object>>() {
                });
                Map<String, String> datas = obj.readValue(obj.writeValueAsString(map.get("data")), new TypeReference<Map<String, Object>>() {
                });
                return datas;
            } else {
                return null;
            }
        } catch (Exception ex) {
            System.out.println(cursorStr);
            HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
            con.setConnectTimeout(60000);
            request(accesstoken, urlString, methodType, contentType, cursorStr);
            return null;
        }
    }

}
