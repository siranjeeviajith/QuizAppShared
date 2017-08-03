package com.fullLearn.services;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.collect.Lists;

import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.UserDevice;
import com.fullLearn.helpers.Constants;
import com.googlecode.objectify.cmd.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by amandeep on 25/07/17.
 */
public class FireBaseService {

    private static Logger logger = Logger.getLogger(FireBaseService.class.getName());

    public void sendNotificationToAllUsers() throws IOException {

        int count = 0;
        String cursorStr = null;
        do {


            Query<UserDevice> query = ofy().load().type(UserDevice.class).limit(30);
            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<UserDevice> iterator = query.iterator();

            List<UserDevice> deviceList = Lists.newArrayList(iterator);

            count = count + deviceList.size();
            logger.info("total : " + count);


            if (deviceList.size() < 1) {
                break;
            }
            System.out.println("notificationToAlluser");
            //notifyToAllUsers("sdfadsfasdfasdfsdf", "d0LKhSEz5OU:APA91bEIulux80LIPJruXXJBzyxL5KMXp-qZB1nC8L3LubAnNYy-zHooycPLeFOy8BYsJv02Dkplz-ZYb7c62nLrchV5FdBSbkUH-0_dvP3SWWKqDShv8w0dvlnwXG0X9moSkFWyTGHL"/*device.getPushToken()*/);

            for (UserDevice device : deviceList) {

                if (device.getPushToken() != null) {
                    try {
                        notifyToAllUsers(device.getUserId(), device.getPushToken());
                    } catch (Exception e) {
                        logger.severe(e.getMessage());
                        continue;
                    }

                }

            }

            cursorStr = iterator.getCursor().toWebSafeString();


        } while (cursorStr != null);

        return;
    }

    private void notifyToAllUsers(String userId, String pushToken) throws IOException {

        LearningStatsAverage learningStatsAvg = ofy().load().type(LearningStatsAverage.class).id(userId).now();

        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "key=" + Constants.SERVER_KEY);
        connection.setDoOutput(true);
        String title = "Full Learn Weekly Summary";
        String msg = "";

        int fourWeekAvg = learningStatsAvg.getFourWeekAvg();
        if (fourWeekAvg > 300) {
            msg = "Awesome work on learning, your average score is " + fourWeekAvg + " mins, keep it up!";
        } else if (fourWeekAvg >= 150 && fourWeekAvg <= 300) {
            msg = "Good Job, your learning goal looks good, average score is now : " + fourWeekAvg + " mins, Keep learning!";
        } else if (fourWeekAvg < 150) {
            msg = "Your Learning Score is LOW, Average Score is :" + fourWeekAvg + " mins";
        }


        Map<String, Object> requestedData = new HashMap<>();
        Map<String, Object> notification = new HashMap<>();
        notification.put("body", msg);
        notification.put("title", title);
        notification.put("icon", "FullLearn");
        requestedData.put("to", pushToken);
        requestedData.put("notification", notification);

        String jsonData = new ObjectMapper().writeValueAsString(requestedData);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        writer.write(jsonData);
        writer.flush();

        String line;
        StringBuilder resp = new StringBuilder();

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(connection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            resp.append(line);
        }
        line = resp.toString();
        writer.close();
        reader.close();

        List<String> errorList = new ArrayList<>();
        errorList.add("Unavailable");
        errorList.add("InvalidRegistration");
        errorList.add("NotRegistered");
        errorList.add("MismatchSenderId");
        System.out.println("response in string  is " + line);
        Map<String, Object> response;
        response = new ObjectMapper().readValue(line, new TypeReference<Map<String, Object>>() {
        });
        List<String> resposeError = (List<String>) response.get("error");
        if (resposeError == null) {
            logger.info("notification sent to " + userId);
            return;
        }
        logger.severe(line);


    }

}
