package com.fullLearn.model;

import com.fullLearn.beans.TrendingChallenges;
import lombok.Data;

@Data
public class LatestTrendsResponse {

    private TrendingChallenges data;
    private String error;
    private boolean response;
}
