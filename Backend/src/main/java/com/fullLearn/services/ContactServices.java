package com.fullLearn.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.Contacts;
import com.fullLearn.helpers.Constants;
import com.fullLearn.helpers.SaveContactsHelper;
import com.fullLearn.model.ContactJson;
import com.fullauth.api.enums.OauthExpiryType;
import com.fullauth.api.exception.TokenResponseException;
import com.fullauth.api.http.HttpMethod;
import com.fullauth.api.http.HttpRequest;
import com.fullauth.api.http.HttpResponse;
import com.fullauth.api.http.UrlFetcher;
import com.fullauth.api.model.oauth.OauthAccessToken;
import com.fullauth.api.service.FullAuthOauthService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Slf4j
public class ContactServices {

    public String getAccessToken() throws IOException {

        FullAuthOauthService authService = FullAuthOauthService.builder()
                .authDomain(Constants.FULLAUTH_DOMAIN)
                .clientId(Constants.CLIENT_ID)
                .clientSecret(Constants.CLIENT_SECRET)
                .devServer(Constants.devServer)
                .build();

        try {
            OauthAccessToken token = authService.refreshAccessToken(Constants.REFRESH_TOKEN, OauthExpiryType.SHORT);
            return token.getAccessToken();

        } catch (TokenResponseException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public Long getLastModifiedContacts() {

        try {
            Contacts contacts = ofy().load().type(Contacts.class).order("-modifiedAt").first().now();
            if (contacts != null)
                return contacts.getModifiedAt();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    // syncContacts
    public int syncContacts() throws Exception {

        int limit = 30;
        int count = 0;
        String cursor = null;

        Long lastModified = getLastModifiedContacts();

        String accessToken = getAccessToken();
        if (accessToken == null || accessToken.isEmpty())
            throw new Exception("acesss token is null, please check the refresh token validity");

        String baseUrl = Constants.AW_API_URL + "/api/v1/account/SEN42/user?limit=" + limit;
        if (lastModified != null)
            baseUrl = baseUrl + "&since=" + lastModified;

        String url = baseUrl;
        do {

            if (cursor != null)
                url = baseUrl + "&cursor=" + cursor;

            HttpRequest httpRequest = new HttpRequest(url, HttpMethod.GET);
            httpRequest.setContentType("application/json");
            httpRequest.setConnectionTimeOut(60000);
            httpRequest.addHeader("Authorization", "Bearer " + accessToken);

            HttpResponse httpResponse = UrlFetcher.makeRequest(httpRequest);
            if (httpResponse.getStatusCode() == 200) {

                String contacts = httpResponse.getResponseContent();
                ObjectMapper obj = new ObjectMapper();

                ContactJson contactJson = obj.readValue(contacts, ContactJson.class);
                if (contactJson.isOk()) {
                    Map<String, Object> data = contactJson.getData();
                    cursor = (String) data.get("cursor");

                    ArrayList<Contacts> userData = obj.readValue(obj.writeValueAsString(data.get("users")), new TypeReference<ArrayList<Contacts>>() {
                    });

                    SaveContactsHelper saveContactsHelper = new SaveContactsHelper();
                    saveContactsHelper.saveContacts(userData);
                    count = count + userData.size();
                    log.info("fetched users : " + userData.size());

                    if (userData == null || userData.size() < limit)
                        return count;
                } else {
                    log.error("Error occured at the server side " + httpResponse.getResponseContent());
                    throw new Exception(httpResponse.getResponseContent());
                }
            } else {
                log.error("Error occured at the server side status code is not 200 " + httpResponse.getResponseContent());
                throw new Exception(httpResponse.getResponseContent());
            }
        } while (cursor != null);

        return count;
    }


//    @Deprecated
//    public boolean saveAllContacts(Long lastModified, String accesstoken, int limit, String cursorStr) throws IOException {
//
//        HTTPUrl listOfDatas = new HTTPUrl();
//        SaveContactsHelper saveContactsHelper = new SaveContactsHelper();
//        ObjectMapper obj = new ObjectMapper();
//
//        obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        String baseUrl = Constants.AW_API_URL + "/api/v1/account/SEN42/user?limit=" + limit;
//        ArrayList<Contacts> userData;
//        String cursor = null;
//
//        try {
//            if (lastModified != null) {
//                System.out.println("Last Modified : " + lastModified);
//                baseUrl = baseUrl + "&since=" + lastModified;
//            }
//            if (cursorStr != null) {
//
//                baseUrl = baseUrl + "&cursor=" + cursorStr;
//
//            }
//            obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            logger.info("url : " + baseUrl);
//            System.out.println("url : " + baseUrl);
//
//            String methodType = "GET";
//            String contentType = "application/json";
//
//            Map<String, String> datas = listOfDatas.request(accesstoken, baseUrl, methodType, contentType, cursorStr);
//
//            cursor = obj.readValue(obj.writeValueAsString(datas.get("cursor")), new TypeReference<String>() {
//            });
//
//            userData = obj.readValue(obj.writeValueAsString(datas.get("users")), new TypeReference<ArrayList<Contacts>>() {
//            });
//
//            saveContactsHelper.saveContacts(userData);
//            logger.info("fetched users : " + userData.size());
//            //System.out.println("fetched users : " + userData.size());
//            if (userData.size() < limit || userData == null) {
//                return true;
//            }
//
//            saveAllContacts(lastModified, accesstoken, limit, cursor);
//            return true;
//        } catch (Exception ex) {
//            logger.severe(ex.getMessage());
//            //System.out.println(ex.getMessage());
//            String cursorValue = cursor;
//            logger.info(cursorValue);
//            //System.out.println(cursorValue);
//            saveAllContacts(lastModified, accesstoken, limit, cursorValue);
//            return true;
//        }
//    }
//

}
