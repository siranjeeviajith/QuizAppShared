package com.fullLearn.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.ContactJson;
import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.TokenAccess;
import com.fullLearn.helpers.Constants;
import com.fullLearn.helpers.HTTPUrl;
import com.fullLearn.helpers.SaveContactsHelper;
import com.fullauth.api.http.HttpMethod;
import com.fullauth.api.http.HttpRequest;
import com.fullauth.api.http.HttpResponse;
import com.fullauth.api.http.UrlFetcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class ContactServices {


    private final static Logger logger = Logger.getLogger(ContactServices.class.getName());

    // helpers
    HTTPUrl listOfDatas = new HTTPUrl();
    SaveContactsHelper saveContactsHelper = new SaveContactsHelper();


    public String getAccessToken() throws IOException {


        String url = Constants.FULL_AUTH_URL + "/o/oauth2/v1/token";
        HttpRequest httpRequest = new HttpRequest(url, HttpMethod.POST);
        httpRequest.setContentType("application/x-www-form-urlencoded");
        String params = "refresh_token=" + Constants.REFRESH_TOKEN + "&client_id=" + Constants.CLIENT_ID + "&client_secret=" + Constants.CLIENT_SECRET + "&grant_type=refresh_token";
        httpRequest.setPayload(params.getBytes("UTF-8"));

        HttpResponse httpResponse = UrlFetcher.makeRequest(httpRequest);
        if (httpResponse.getStatusCode() == 200) {

            String tokens = httpResponse.getResponseContent();
            logger.info(tokens);

            ObjectMapper obj = new ObjectMapper();
            TokenAccess pojo = obj.readValue(tokens, TokenAccess.class);
            String accesstoken = pojo.getAccess_token();
            return accesstoken;
        }
        System.out.println("Error occured "+httpResponse.getResponseContent());
        return null;
    }

    public Long getLastModifiedContacts() {

        try {
            Contacts contacts = ofy().load().type(Contacts.class).order("-modifiedAt").first().now();
            if (contacts == null)
                return null;
            return contacts.getModifiedAt();
        }catch( Exception e){
            logger.severe("Exception: " + e);
            return null;
        }

    }

    // syncContacts
    public int syncContacts() throws Exception {

        int limit = 30;
        int count = 0;
        String cursor = null;
        String baseUrl = Constants.AW_API_URL + "/api/v1/account/SEN42/user?limit="+limit;
        Long lastModified = getLastModifiedContacts();
        String accesstoken = getAccessToken();
        if (lastModified != null)
            baseUrl = baseUrl + "&since=" + lastModified;
        do{
            if (lastModified != null)
                baseUrl = baseUrl + "&cursor=" + cursor;
            HttpRequest httpRequest = new HttpRequest(baseUrl, HttpMethod.GET);
            httpRequest.setContentType("application/json");
            httpRequest.setConnectionTimeOut(60000);
            httpRequest.addHeader("Authorization", "Bearer " + accesstoken);

            HttpResponse httpResponse = UrlFetcher.makeGetRequest(baseUrl);
            if (httpResponse.getStatusCode() == 200) {

                String contacts = httpResponse.getResponseContent();
                ObjectMapper obj = new ObjectMapper();
                ContactJson contactJson = obj.readValue(contacts, ContactJson.class);

                if(contactJson.isOk()){
                    Map<String,Object> data = contactJson.getData();
                    cursor = (String) data.get("cursor");
                    String usersDataAsString =  (String) data.get("users");
                    ArrayList<Contacts> userData = obj.readValue(usersDataAsString ,new TypeReference<ArrayList<Contacts>>(){});

                    SaveContactsHelper saveContactsHelper = new SaveContactsHelper();
                    saveContactsHelper.saveContacts(userData);
                    count = count + userData.size();
                    logger.info("fetched users : " + userData.size());
                    if (userData.size() < limit || userData.isEmpty())
                        return count;
                }
            }
            else {
                System.out.println("Error occured " + httpResponse.getResponseContent());
                throw new Exception(httpResponse.getResponseContent());
            }

        }while(cursor != null);

        return count;
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
