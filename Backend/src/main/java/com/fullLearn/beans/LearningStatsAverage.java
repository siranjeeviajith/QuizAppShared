package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class LearningStatsAverage {

    @Id
    @Index private String userId;
    @Index private int fourWeekAvg;
    @Index private int twelveWeekAvg;
    @Index private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFourWeekAvg() {
        return fourWeekAvg;
    }

    public void setFourWeekAvg(int fourWeekAvg) {
        this.fourWeekAvg = fourWeekAvg;
    }

    public int getTwelveWeekAvg() {
        return twelveWeekAvg;
    }

    public void setTwelveWeekAvg(int twelveWeekAvg) {
        this.twelveWeekAvg = twelveWeekAvg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }





}
