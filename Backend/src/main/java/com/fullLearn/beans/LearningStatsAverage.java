package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import java.util.Map;

/**
 * Created by user on 6/16/2017.
 */
@Entity
public class LearningStatsAverage {


    public String getUserid() {
        return userId;
    }

    public void setUserid(String userid) {
        this.userId = userid;
    }

    public int getFourthWeek() {
        return fourthWeek;
    }

    public void setFourthWeek(int fourthWeek) {
        this.fourthWeek = fourthWeek;
    }

    public int getTwelfthWeek() {
        return twelfthWeek;
    }

    public void setTwelfthWeek(int twelfthWeek) {
        this.twelfthWeek = twelfthWeek;
    }

    @Id

    @Index
    private String userId;

    @Index private int fourthWeek;

    @Index private int twelfthWeek;



}
