package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Team {

    @Id
    private long teamId;
    private String teamName;
    @Index
    private Set<String> members;
    private long modifiedAt;

}
