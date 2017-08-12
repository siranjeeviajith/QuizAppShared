package com.fullLearn.services;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.Frequency;
import com.fullLearn.beans.LearningStats;
import com.fullLearn.beans.LearningStatsAverage;
import com.fullLearn.beans.TrendingChallenges;
import com.fullLearn.helpers.Constants;
import com.fullLearn.model.AUStatsChallangeInfo;
import com.fullLearn.model.AUStatsChallanges;
import com.fullLearn.model.AUStatsResponse;
import com.fullLearn.model.ChallengesInfo;
import com.fullauth.api.http.HttpMethod;
import com.fullauth.api.http.HttpRequest;
import com.fullauth.api.http.HttpResponse;
import com.fullauth.api.http.UrlFetcher;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AUStatsService {
    final static MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
    public Map<String, ChallengesInfo> challengesCountMap = new HashMap<>();

    public AUStatsService() {
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    }

    /**
     * Fetching user contacts from the Contact Kind, with limit 50
     * */
    public void fetchAllUserDailyStats() throws Exception {

        String key = "dailyStatsCursor";
        String cursorStr = (String) cache.get(key);
        do {

            Query<Contacts> query = ofy().load().type(Contacts.class).limit(50);
            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = query.iterator();

            //no more element
            if (iterator == null || !iterator.hasNext()) {
                return;
            }

            while (iterator.hasNext()){
                fetchUserDailyStats(iterator.next());
            }

            cursorStr = iterator.getCursor().toWebSafeString();
            cache.put(key, cursorStr, Expiration.byDeltaSeconds(300));

        } while (cursorStr != null);

        cache.delete(key);

        TrendingChallenges yesterdayTrends = getYesterdayTrends();
        ofy().save().entity(yesterdayTrends).now();
    }

    /**
     * Sorting trends and store the top 15 trend challenges in TrendingChallenges
     */
    private TrendingChallenges getYesterdayTrends() {

        List<Map.Entry<String, ChallengesInfo>> challenges = new ArrayList(challengesCountMap.entrySet());

        Collections.sort(challenges, new Comparator<Map.Entry<String, ChallengesInfo>>() {
            public int compare(Map.Entry<String, ChallengesInfo> o1, Map.Entry<String, ChallengesInfo> o2) {
                if (o1.getValue().getViews() > o2.getValue().getViews())
                    return -1;
                else if (o1.getValue().getViews() < o2.getValue().getViews())
                    return 1;
                else
                    return 0;
            }
        });

        LinkedHashMap<String, ChallengesInfo> topTrends = new LinkedHashMap<>();
        int rowCount = 1;
        for (Map.Entry<String, ChallengesInfo> challenge : challenges) {

            if (rowCount > 15 || challenge.getValue().getViews() < 2)
                break;

            topTrends.put(challenge.getKey(), challenge.getValue());
            rowCount++;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startTime = cal.getTime().getTime();

        TrendingChallenges yesterdayTrends = new TrendingChallenges();
        yesterdayTrends.setTrends(topTrends);
        yesterdayTrends.setId(startTime);
        yesterdayTrends.setTime(startTime);

        return yesterdayTrends;
    }

    /**
     * Storing previous day user learning stat in LearningStat, after getting the information from the Adaptive
     */
    private void fetchUserDailyStats(Contacts contact) throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startTime = cal.getTime().getTime();

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);

        long endTime = cal.getTime().getTime();

        AUStatsResponse auStatsResponse = fetchUserAUStats(contact.getLogin(), startTime, endTime);

        LearningStats dailyEntity = mapUserLearningStats(auStatsResponse, contact, startTime, endTime);
        if( dailyEntity == null )
            return;

        ofy().save().entity(dailyEntity).now();
        calculateLearningTrends(dailyEntity.getChallengesDetails());
    }

    /**
     * Getting the challenges information and store it into the Global Hashmap challengesCountMap
     */
    private void calculateLearningTrends(Map<String, AUStatsChallangeInfo> challenges) throws Exception {

        try {
            if (challenges == null || challenges.isEmpty())
                return;

            for (Map.Entry<String, AUStatsChallangeInfo> challengesDetailsSet : challenges.entrySet()) {

                String challengeTitle = challengesDetailsSet.getKey();

                ChallengesInfo challengeInfo = challengesCountMap.get(challengeTitle);
                if (challengeInfo == null) {

                    AUStatsChallangeInfo challengeDetails = challengesDetailsSet.getValue();

                    challengeInfo = new ChallengesInfo();
                    challengeInfo.setDuration(challengeDetails.getMinutes());
                    challengeInfo.setImage(challengeDetails.getImage());
                    challengeInfo.setUrl(challengeDetails.getLink());
                }

                challengeInfo.setViews(challengeInfo.getViews() + 1);
                challengesCountMap.put(challengeTitle, challengeInfo);
            }

        } catch (Exception e) {
            System.out.println("Error occured " + e.getMessage());
            throw new Exception("Exception occured " + e.getMessage());
        }
    }

    /**
     * Fetching user Learning stats from the Adaptive using API, based on the start and end time
     */
    private AUStatsResponse fetchUserAUStats(String email, long startTime, long endTime) throws Exception {

        String url = Constants.AU_API_URL + "/v1/completedMinutes?apiKey=" + Constants.AU_APIKEY + "&email=" + email + "&startTime=" + startTime + "&endTime=" + endTime;
        HttpRequest httpRequest = new HttpRequest(url, HttpMethod.POST);
        httpRequest.setContentType("application/json");
        httpRequest.setConnectionTimeOut(30000);

        HttpResponse httpResponse = UrlFetcher.makeRequest(httpRequest);
        if (httpResponse.getStatusCode() == 200) {
            String message = httpResponse.getResponseContent();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(message, AUStatsResponse.class);
        }
        System.out.println("Error occured, while fetching information from the API" + httpResponse.getResponseContent());
        throw new Exception(httpResponse.getResponseContent());
    }

    /**
     * Mapping the user Learning stat into LearningStats and return that entity.
     */
    private LearningStats mapUserLearningStats(AUStatsResponse response, Contacts contact, long startTime, long endTime) {

        if (!response.isResponse())
            return null;

        String email = contact.getLogin();
        String userId = contact.getId();

        LearningStats learningStats = new LearningStats();
        learningStats.setId(userId + ":" + startTime + ":" + endTime);
        learningStats.setUserId(userId);
        learningStats.setStartTime(startTime);
        learningStats.setEndTime(endTime);
        learningStats.setFrequency(Frequency.DAY);
        learningStats.setEmail(email);
        Map<String, AUStatsChallanges> userLearningStats = response.getData();

        if (userLearningStats.get(email) == null) {

            learningStats.setMinutes(0);
            learningStats.setChallengesCompleted(0);
            learningStats.setChallengesDetails(null);
            return learningStats;
        }

        AUStatsChallanges auStatsChallanges = userLearningStats.get(email);
        learningStats.setMinutes(auStatsChallanges.getMinutes());
        learningStats.setChallengesDetails(auStatsChallanges.getChallengesDetails());
        learningStats.setChallengesCompleted(auStatsChallanges.getChallengesCompleted());

        return learningStats;
    }

    /**
     * Fetching user contacts from Contact, based on limit 50
     */
    public void calculateAllUserOverallAverage() {

        String cursor = null;
        do {

            Query<Contacts> contactQuery = ofy().load().type(Contacts.class).limit(50);
            if (cursor != null)
                contactQuery = contactQuery.startAt(Cursor.fromWebSafeString(cursor));

            QueryResultIterator<Contacts> iterator = contactQuery.iterator();

            // no more element
            if (iterator == null || !iterator.hasNext())
                return;

            while (iterator.hasNext()){
                calculateUserOverAllAverage(iterator.next());
            }

            cursor = iterator.getCursor().toWebSafeString();
        } while (cursor != null);
    }

    /**
     * Fetching user Learning stats, based on the Contact id of a user, from the LearningStats for previous 12 week,
     * and compute average of previous four and twelfth week,
     * then save it into the LearningStatsAverage
     */
    private LearningStatsAverage calculateUserOverAllAverage(Contacts contact){

        List<LearningStats> weeklyLearningStat = ofy().load().type(LearningStats.class)
                .filter("userId", contact.getId())
                .filter("frequency", Frequency.WEEK)
                .order("-startTime").limit(12).list();

        if(weeklyLearningStat == null)
            return null;

        int fourWeekAverage = 0;
        int twelfthWeekAverage = 0;

        int weekCount = 1;
        for(LearningStats learningStat : weeklyLearningStat){

            twelfthWeekAverage = twelfthWeekAverage + learningStat.getMinutes();

            if (weekCount <= 4)
                fourWeekAverage = fourWeekAverage + learningStat.getMinutes();

            weekCount++;
        }

        fourWeekAverage = Math.round(fourWeekAverage / 4);
        twelfthWeekAverage = Math.round(twelfthWeekAverage / 12);

        LearningStatsAverage learningStatsAverage = new LearningStatsAverage();
        learningStatsAverage.setUserId(contact.getId());
        learningStatsAverage.setFourWeekAvg(fourWeekAverage);
        learningStatsAverage.setTwelveWeekAvg(twelfthWeekAverage);
        learningStatsAverage.setEmail(contact.getLogin());

        ofy().save().entity(learningStatsAverage).now();

        return learningStatsAverage;
    }


    /**
     * Calculate Weekly Learning for All Users based on past 7 days learning
     */
    public void calculateAllUserWeeklyStats() {

        String cursor = null;
        do {

            Query<Contacts> contactQuery = ofy().load().type(Contacts.class).limit(50);
            if (cursor != null)
                contactQuery = contactQuery.startAt(Cursor.fromWebSafeString(cursor));

            QueryResultIterator<Contacts> iterator = contactQuery.iterator();

            //no more elements available
            if (iterator == null || !iterator.hasNext()){
                return;
            }

            while (iterator.hasNext()) {
                calculateUserWeekStats(iterator.next());
            }

            cursor = iterator.getCursor().toWebSafeString();

        } while (cursor != null);

    }

    /**
     * Calculate Weekly Learning for a Single User based on past 7 days learning
     */
    private LearningStats calculateUserWeekStats(Contacts contact){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startDate = cal.getTime().getTime();

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);

        long endDate = cal.getTime().getTime();

        List<LearningStats> userDailyLearning = ofy().load().type(LearningStats.class)
                .filter("userId", contact.getId())
                .filter("frequency", Frequency.DAY)
                .filter("startTime >=", startDate)
                .filter("endTime <=", endDate)
                .list();

        if( userDailyLearning == null )
            return null;

        int totalMinutes = 0;
        int totalChallenges = 0;
        for(LearningStats dailyLearning : userDailyLearning){

            totalMinutes = totalMinutes + dailyLearning.getMinutes();
            totalChallenges = totalChallenges + dailyLearning.getChallengesCompleted();
        }

        LearningStats weeklyLearningStats = new LearningStats();
        weeklyLearningStats.setId(contact.getId() + ":" + startDate + ":" + endDate);
        weeklyLearningStats.setUserId(contact.getId());
        weeklyLearningStats.setMinutes(totalMinutes);
        weeklyLearningStats.setChallengesCompleted(totalChallenges);
        weeklyLearningStats.setEmail(contact.getLogin());
        weeklyLearningStats.setFrequency(Frequency.WEEK);
        weeklyLearningStats.setStartTime(startDate);
        weeklyLearningStats.setEndTime(endDate);

        ofy().save().entities(weeklyLearningStats).now();
        return weeklyLearningStats;
    }

}
