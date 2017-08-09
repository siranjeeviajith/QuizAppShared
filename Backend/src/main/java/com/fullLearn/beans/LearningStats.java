package com.fullLearn.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import java.util.Map;

import lombok.Data;


@Entity
@Cache(expirationSeconds = 86400)
@Data
public class LearningStats {


    @Id
    @Index
    private String id;
    @Index
    private String userId;
    @Ignore
    @JsonProperty("challenges_details")
    private Map<String, LearningStatsChallangeInfo> challengesDetails;
    @Index
    private int minutes;
    @Index
    @JsonProperty("challenges_completed")
    private int challengesCompleted;
    @Index
    private String email;
    @Index
    private Frequency frequency;
    @Index
    private long startTime;
    @Index
    private long endTime;

    public LearningStats() {

    }

}
