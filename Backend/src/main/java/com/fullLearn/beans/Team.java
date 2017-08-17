package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Cache(expirationSeconds = 86400)
public class Team {

    @Id
    private long teamId;
    @Index
    private String teamName;
    @Index
    private Set<String> members;
    @Index
    private long modifiedAt;

}
