package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by user on 6/16/2017.
 */
@Entity
public class LearningStatsAverage {

 @Id
 private String Id;

 @Index
 private String userId;

 @Index
 private String email;

 @Index
 private Frequency.Week frequencyWeek;

 @Index
 private int minutes;

 @Index
private int challenges_completed;

 @Index
private long startTime;

 @Index
private long endTime;


public LearningStatsAverage()
{

}

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Frequency.Week getFrequencyWeek() {
        return frequencyWeek;
    }

    public void setFrequencyWeek(Frequency.Week frequencyWeek) {
        this.frequencyWeek = frequencyWeek;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getChallenges_completed() {
        return challenges_completed;
    }

    public void setChallenges_completed(int challenges_completed) {
        this.challenges_completed = challenges_completed;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
