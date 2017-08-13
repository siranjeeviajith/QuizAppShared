package com.fullLearn.services;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;

import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.UserDevice;
import com.fullLearn.helpers.Constants;
import com.fullLearn.model.NotificationMessage;
import com.fullLearn.model.PushNotification;
import com.fullauth.api.http.HttpMethod;
import com.fullauth.api.http.HttpRequest;
import com.fullauth.api.http.HttpResponse;
import com.fullauth.api.http.UrlFetcher;
import com.googlecode.objectify.cmd.Query;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by amandeep on 25/07/17.
 */
public class FireBaseService {

    private static Logger logger = Logger.getLogger(FireBaseService.class.getName());

    public void sendNotificationToAllUsers() throws IOException {

        String cursor = null;
        do {

            Query<UserDevice> query = ofy().load().type(UserDevice.class).limit(50);
            if (cursor != null)
                query = query.startAt(Cursor.fromWebSafeString(cursor));

            QueryResultIterator<UserDevice> iterator = query.iterator();
            if (iterator == null || !iterator.hasNext())
                return;

            while(iterator.hasNext()) {

                UserDevice device = iterator.next();
                if(device.getPushToken() == null )
                    continue;

                sendNotifyToUsers( device.getUserId(), device.getPushToken() );
            }

            cursor = iterator.getCursor().toWebSafeString();
        } while (cursor != null);

    }

    private void sendNotifyToUsers(String userId, String pushToken) throws IOException {

        String msg = "";

        LearningStatsAverage learningStatsAvg = ofy().load().type(LearningStatsAverage.class).id(userId).now();
        int fourWeekAvg = learningStatsAvg.getFourWeekAvg();
        if (fourWeekAvg > 300) {

            msg = "Awesome work on learning, your average score is " + fourWeekAvg + " mins, keep it up!";

        } else if (fourWeekAvg >= 150 && fourWeekAvg <= 300) {

            msg = "Good Job, your learning goal looks good, average score is now : " + fourWeekAvg + " mins, Keep learning!";

        } else
            msg = "Your Learning Score is LOW, Average Score is :" + fourWeekAvg + " mins";


        PushNotification pushNotification = new PushNotification();
        pushNotification.setTo(pushToken);

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setBody(msg);
        notificationMessage.setTitle("Full Learn Weekly Summary");
        notificationMessage.setIcon("FullLearn");

        pushNotification.setNotification(notificationMessage);

        String message = new ObjectMapper().writeValueAsString(pushNotification);

        String url = "https://fcm.googleapis.com/fcm/send";
        HttpRequest httpRequest = new HttpRequest(url, HttpMethod.POST);
        httpRequest.setContentType("application/json");
        httpRequest.addHeader("Authorization", "key=" + Constants.SERVER_KEY);
        httpRequest.setPayload(message.getBytes("UTF-8"));

        HttpResponse httpResponse = UrlFetcher.makeRequest(httpRequest);
        if (httpResponse.getStatusCode() != 200) {

            String response = httpResponse.getResponseContent();
            logger.log(Level.SEVERE, "Error occured to the user " +userId+", while sending message " + response);
            return;
        }
    }
}