package com.fullLearn.services;

import com.fullLearn.beans.*;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.collect.Lists;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.*;
import java.util.logging.Level;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AUStatsService {
    final static MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
    public Map<String, ChallengesInfo> challengesCountMap = new HashMap<>();

    public AUStatsService() {
        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    }

    public int fetchAllUserDailyStats() throws Exception {
        int count = 0;

        String key = "dailyStatsCursor";
        String cursorStr = (String) cache.get(key);
        do {

            Query<Contacts> query = ofy().load().type(Contacts.class).limit(50);
            if (cursorStr != null)
                query = query.startAt(Cursor.fromWebSafeString(cursorStr));

            QueryResultIterator<Contacts> iterator = query.iterator();

            List<Contacts> contactList = Lists.newArrayList(iterator);
            count = count + contactList.size();

            if (contactList.size() < 1) {
                break;
            }

            for (Contacts contact : contactList)
                fetchUserDailyStats(contact);

            cursorStr = iterator.getCursor().toWebSafeString();
            cache.put(key, cursorStr, Expiration.byDeltaSeconds(300));

        } while (cursorStr != null);

        cache.delete(key);

        TrendingChallenges yesterdayTrends = getYesterdayTrends();
        ofy().save().entity(yesterdayTrends).now();

        return count;
    }

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

        Date start = cal.getTime();
        long startTime = start.getTime();

        TrendingChallenges yesterdayTrends = new TrendingChallenges();
        yesterdayTrends.setTrends(topTrends);
        yesterdayTrends.setId(startTime);
        yesterdayTrends.setTime(startTime);

        return yesterdayTrends;
    }

    private void fetchUserDailyStats(Contacts contact) throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date start = cal.getTime();
        long startTime = start.getTime();

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);

        Date end = cal.getTime();
        long endTime = end.getTime();

        AUStatsResponse auStatsResponse = fetchUserAUStats(contact.getLogin(), startTime, endTime);

        LearningStats dailyEntity = mapUserLearningStats(auStatsResponse, contact, startTime, endTime, Frequency.DAY);
        ofy().save().entity(dailyEntity).now();
        calculateLearningTrends(dailyEntity.getChallengesDetails());
    }

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


    private LearningStats mapUserLearningStats(AUStatsResponse response, Contacts contact, long startTime, long endTime, Frequency frequency) {

        if (!response.isResponse())
            return null;

        String email = contact.getLogin();
        String userId = contact.getId();

        LearningStats learningStats = new LearningStats();
        learningStats.setId(userId + ":" + startTime + ":" + endTime);
        learningStats.setUserId(userId);
        learningStats.setStartTime(startTime);
        learningStats.setEndTime(endTime);
        learningStats.setFrequency(frequency);
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

    public int calculateAllUserStatsAverage() {

        int count = 0;
        String cursor = null;

        do {

            Query<Contacts> contactQuery = ofy().load().type(Contacts.class).limit(30);
            if (cursor != null)
                contactQuery = contactQuery.startAt(Cursor.fromWebSafeString(cursor));

            QueryResultIterator<Contacts> dsUserContacts = contactQuery.iterator();

            List<Contacts> userContact = contactQuery.list();
            if (userContact.size() < 1) {
                return count;
            }

            count = count + userContact.size();
            for(Contacts contact: userContact)
                calculateUserWeekAverage(contact);

            cursor = dsUserContacts.getCursor().toWebSafeString();
        } while (cursor != null);

        return count;
    }

    private void calculateUserWeekAverage(Contacts contact){

        List<LearningStats> StateUser = ofy().load().type(LearningStats.class).filter("userId", contact.getId()).filter("frequency", Frequency.WEEK).order("-startTime").limit(12).list();

        int fourWeekAverage = 0;
        int twelfthWeekAverage = 0;

        int weekCount = 1;
        for(LearningStats learningStat : StateUser){

            if (weekCount > 4)
                twelfthWeekAverage = twelfthWeekAverage + learningStat.getMinutes();
            else {

                fourWeekAverage = fourWeekAverage + learningStat.getMinutes();
                twelfthWeekAverage = twelfthWeekAverage + learningStat.getMinutes();
            }

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
    }

}
