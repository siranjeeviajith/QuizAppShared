package com.fullLearn.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by amandeep on 7/14/2017.
 */
@Data
public class ChallengesInfo implements Serializable{

    public int views;
    public int duration;
    public String image;
    public String url;
}
