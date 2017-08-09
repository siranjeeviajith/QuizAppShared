package com.fullLearn.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class LearningStatsChallanges {

    private int minutes;

    @JsonProperty("challenges_details")
    private Map<String,LearningStatsChallangeInfo> challengesDetails;

    @JsonProperty("challenges_completed")
    private int challengesCompleted;

}
