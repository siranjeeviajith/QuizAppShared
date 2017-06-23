package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class LearningStatsAverage {

    @Id
    @Index
    private String userId;
    @Index private int fourthWeek;
    @Index private int twelfthWeek;

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



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Index private String email;



}
