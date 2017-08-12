package com.fullLearn.services;

import com.fullLearn.beans.TrendingChallenges;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class LatestTrendsService {

    public TrendingChallenges getLatestTrends(long date) {

        return ofy().load().type(TrendingChallenges.class).id(date).now();

    }
}
