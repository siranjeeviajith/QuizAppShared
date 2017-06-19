package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by user on 6/17/2017.
 */
@Entity
public class WeeksStats {

    @Id
    @Index private String userId;
    @Index private int fourthWeek;
    @Index private int twelfthWeek;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFourthWeek() {
        return fourthWeek;
    }

    public void setFourthWeek(int fourthWeek) {
        this.fourthWeek = fourthWeek;
    }

    public int getTwelvethWeek() {
        return twelfthWeek;
    }

    public void setTwelvethWeek(int twelvethWeek) {
        this.twelfthWeek = twelvethWeek;
    }
}
