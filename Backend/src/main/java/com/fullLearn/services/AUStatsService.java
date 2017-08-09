package com.fullLearn.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullLearn.beans.*;
import com.fullLearn.helpers.Constants;
import com.fullLearn.model.ChallengesInfo;
import com.fullauth.api.http.HttpMethod;
import com.fullauth.api.http.HttpRequest;
import com.fullauth.api.http.HttpResponse;
import com.fullauth.api.http.UrlFetcher;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.collect.Lists;
import com.googlecode.objectify.cmd.Query;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AUStatsService {
    public Map<String, ChallengesInfo> challengesCountMap = new HashMap();
    final static MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
    long startTime, endTime ;
    public AUStatsService() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date start = cal.getTime();
        startTime = start.getTime();

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);

        Date end = cal.getTime();
        endTime = end.getTime();

        cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    }

    public void fetchAllUserDailyStats() throws Exception {
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

            for(Contacts contact: contactList)
                fetchUserDailyStats(contact);

            cursorStr = iterator.getCursor().toWebSafeString();
            cache.put(key, cursorStr, Expiration.byDeltaSeconds(300));

        } while (cursorStr != null);

        cache.delete(key);
        TrendingChallenges yesterdayTrends = getYesterdayTrends();
        saveUserStats(yesterdayTrends);
    }

    private TrendingChallenges getYesterdayTrends() {

        List<Map.Entry<String, ChallengesInfo>> userChallenges = new ArrayList(challengesCountMap.entrySet());

        Collections.sort(userChallenges, new Comparator<Map.Entry<String, ChallengesInfo>>() {
            public int compare(Map.Entry<String, ChallengesInfo> o1, Map.Entry<String, ChallengesInfo> o2) {
                if (o1.getValue().getViews() > o2.getValue().getViews())
                    return -1;
                else if (o1.getValue().getViews() < o2.getValue().getViews())
                    return 1;
                else
                    return 0;
            }
        });

        LinkedHashMap<String, ChallengesInfo> topTenTrends = new LinkedHashMap<>();
        TrendingChallenges yesterdayTrends = new TrendingChallenges();

        int rowCount = 1;
        for (Map.Entry<String, ChallengesInfo> userChallenge : userChallenges) {

            if ( rowCount > 10 )
                break;

            topTenTrends.put(userChallenge.getKey(), userChallenge.getValue());
            rowCount++;
        }

        yesterdayTrends.setTrends(topTenTrends);
        yesterdayTrends.setId(startTime);
        yesterdayTrends.setTime(startTime);

        return yesterdayTrends;
    }


    private void fetchUserDailyStats(Contacts contact) throws Exception {

        AUStatsResponse auStatsResponse = fetchUserAUStats(contact.getLogin());
        LearningStats dailyEntity = mapUserLearningStats(auStatsResponse, contact);
        saveUserStats(dailyEntity);
        calculateLearningTrends(dailyEntity.getChallengesDetails());
    }

    private void calculateLearningTrends(Map<String, LearningStatsChallangeInfo> challenges) throws Exception {

        try {
            if (challenges == null || challenges.isEmpty())
                return;

            for (Map.Entry challengesDetailsSet : challenges.entrySet()) {

                String challengeTitle = (String) challengesDetailsSet.getKey();
                ChallengesInfo challengeInformation = challengesCountMap.get(challengeTitle);

                if (challengeInformation == null) {

                    challengeInformation = new ChallengesInfo();
                    LearningStatsChallangeInfo challengeDetails = (LearningStatsChallangeInfo) challengesDetailsSet.getValue();
                    challengeInformation.setDuration(challengeDetails.getMinutes());
                    challengeInformation.setImage(challengeDetails.getImage());
                    challengeInformation.setUrl(challengeDetails.getLink());

                }

                challengeInformation.setViews(challengeInformation.getViews() + 1);
                challengesCountMap.put(challengeTitle, challengeInformation);
            }

        } catch (Exception e) {
            System.out.println("Error occured "+e.getMessage());
            throw new Exception("Exception occured "+e.getMessage());
        }
    }

    private AUStatsResponse fetchUserAUStats(String email) throws Exception {

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
        System.out.println("Error occured " + httpResponse.getResponseContent());
        throw new Exception(httpResponse.getResponseContent());
    }


    private LearningStats mapUserLearningStats(AUStatsResponse dataMap, Contacts contact) {

        if (!dataMap.isResponse())
            return null;

        String email = contact.getLogin();
        String userId = contact.getId();

        LearningStats yesterdayLearnStats = new LearningStats();
        yesterdayLearnStats.setId(userId + ":" + startTime + ":" + endTime);
        yesterdayLearnStats.setUserId(userId);
        yesterdayLearnStats.setStartTime(startTime);
        yesterdayLearnStats.setEndTime(endTime);
        yesterdayLearnStats.setFrequency(Frequency.DAY);
        yesterdayLearnStats.setEmail(email);
        Map<String, LearningStatsChallanges> userLearningStats =  dataMap.getData();

        if(userLearningStats.get(email) == null){

            yesterdayLearnStats.setMinutes(0);
            yesterdayLearnStats.setChallengesCompleted(0);
            yesterdayLearnStats.setChallengesDetails(null);
            return yesterdayLearnStats;
        }

        LearningStatsChallanges learningStatsChallanges = userLearningStats.get(email);

        yesterdayLearnStats.setMinutes(learningStatsChallanges.getMinutes());
        yesterdayLearnStats.setChallengesDetails(learningStatsChallanges.getChallengesDetails());
        yesterdayLearnStats.setChallengesCompleted(learningStatsChallanges.getChallengesCompleted());

        return yesterdayLearnStats;
    }

    public void saveUserStats(Object entry) {

        ofy().save().entity(entry).now();

    }

}
