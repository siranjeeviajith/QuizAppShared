package com.fullLearn.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.TokenAccess;
import com.fullLearn.helpers.Constants;
import com.fullLearn.helpers.HTTPUrl;
import com.fullLearn.helpers.SaveContactsHelper;
import com.googlecode.objectify.cmd.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ContactServices {


    private final static Logger logger = Logger.getLogger(ContactServices.class.getName());

    // helpers
    HTTPUrl listOfDatas = new HTTPUrl();
    SaveContactsHelper saveContactsHelper = new SaveContactsHelper();


    public String getAccessToken() throws IOException {


        URL url = new URL(Constants.FULL_AUTH_URL + "/o/oauth2/v1/token");

        String params = "refresh_token=" + Constants.REFRESH_TOKEN + "&client_id=" + Constants.CLIENT_ID + "&client_secret=" + Constants.CLIENT_SECRET + "&grant_type=refresh_token";

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
        logger.info(tokens);
        //System.out.println(tokens);

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
            logger.severe("exception: " + ex);
            //System.out.println("exception: " + ex);
            return null;
        }

    }


    public boolean saveAllContacts(Long lastModified, String accesstoken, int limit, String cursorStr) throws IOException {

        ObjectMapper obj = new ObjectMapper();

        obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String baseUrl = Constants.AW_API_URL + "/api/v1/account/SEN42/user?limit=" + limit;
        ArrayList<Contacts> userData;
        String cursor = null;

        try {
            if (lastModified != null) {
                System.out.println("Last Modified : " + lastModified);
                baseUrl = baseUrl + "&since=" + lastModified;
            }
            if (cursorStr != null) {

                baseUrl = baseUrl + "&cursor=" + cursorStr;

            }
            obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            logger.info("url : " + baseUrl);
            //System.out.println("url : " + baseUrl);

            String methodType = "GET";
            String contentType = "application/json";

            Map<String, String> datas = listOfDatas.request(accesstoken, baseUrl, methodType, contentType, cursorStr);

            cursor = obj.readValue(obj.writeValueAsString(datas.get("cursor")), new TypeReference<String>() {
            });

            userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Contacts>>() {
            });

            saveContactsHelper.saveContacts(userData);
            logger.info("fetched users : " + userData.size());
            //System.out.println("fetched users : " + userData.size());
            if (userData.size() < limit || userData == null) {
                return true;
            }

            saveAllContacts(lastModified, accesstoken, limit, cursor);
            return true;
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            //System.out.println(ex.getMessage());
            String cursorValue = cursor;
            logger.info(cursorValue);
            //System.out.println(cursorValue);
            saveAllContacts(lastModified, accesstoken, limit, cursorValue);
            return true;
        }
    }


}
