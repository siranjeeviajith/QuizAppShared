package com.fullLearn.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by amandeep on 7/14/2017.
 */
@Data
@NoArgsConstructor
public class ChallengesInfo implements Serializable {

    public int views;
    public int duration;
    public String image;
    public String url;
}
