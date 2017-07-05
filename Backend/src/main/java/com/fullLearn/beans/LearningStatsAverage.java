package com.fullLearn.beans;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Getter;
import lombok.Setter;

@Entity
@Cache(expirationSeconds=86400)
public class LearningStatsAverage {

    @Id
    @Index
    @Getter
    @Setter
    private String userId;

    @Index
    @AlsoLoad("fourthWeek")
    @Getter
    @Setter
    private int fourWeekAvg;

    @Index
    @AlsoLoad("twelfthWeek")
    @Getter
    @Setter
    private int twelveWeekAvg;


    @Index
    @Getter
    @Setter
    private String email;


}
