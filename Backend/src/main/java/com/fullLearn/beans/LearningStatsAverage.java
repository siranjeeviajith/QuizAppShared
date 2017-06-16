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


    @Id
    @Index
    private String id;

    @Index
    private String userId;


    @Index
    private int minutes;

    @Index
    private int challenges_completed;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Index
    private String email;

    public Frequency.Week getFrequencyWeek() {
        return frequencyWeek;
    }

    //default constructor
    public LearningStatsAverage()
    {

    }
    @Index
    private Frequency.Week frequencyWeek;

    @Index
    private long startTime;
    @Index
    private  long endTime;
    public void setFrequencyWeek(Frequency.Week frequencyWeek){
        this.frequencyWeek = frequencyWeek;
    }
    // getter setter for frequency EndTime
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return endTime;
    }


    // getter setter for start time
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getStartTime() {
        return startTime;
    }

    // getter setter for id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // getter setter of userid
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }





    // getter setter for minutes
    public int getMinutes() {
        return minutes;
    }
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    // getter setter no of challenges
    public int getChallenges_completed() {
        return challenges_completed;
    }

    public void setChallenges_completed(int challenges_completed) {
        this.challenges_completed = challenges_completed;
    }





}
