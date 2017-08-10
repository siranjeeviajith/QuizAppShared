package com.fullLearn.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullLearn.model.AUStatsChallangeInfo;
import lombok.Data;

import java.util.Map;

@Data
public class AUStatsChallanges {

    private int minutes;

    @JsonProperty("challenges_details")
    private Map<String,AUStatsChallangeInfo> challengesDetails;

    @JsonProperty("challenges_completed")
    private int challengesCompleted;

}
