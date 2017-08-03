package com.fullLearn.beans;

import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import lombok.Data;


@Entity
@Cache(expirationSeconds = 86400)
@Data
public class LearningStatsAverage {

    @Id
    @Index
    private String userId;
    @Index
    @AlsoLoad("fourthWeek")
    private int fourWeekAvg;
    @Index
    @AlsoLoad("twelfthWeek")
    private int twelveWeekAvg;
    @Index
    private String email;


}
