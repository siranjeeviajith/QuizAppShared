package com.fullLearn.beans;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import lombok.Data;

@Entity
@Cache(expirationSeconds = 86400)
@Data

public class Contacts {

    @Id
    private String id;
    private String createdAt;
    @Index
    private Long modifiedAt;
    private String accountId;
    @Index
    private String login;
    private String firstName;
    private String lastName;
    private String photoId;
    @Index
    private String status;
    private String title;


}
