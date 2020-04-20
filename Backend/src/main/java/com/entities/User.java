package com.entities;

import com.enums.AccountType;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import lombok.Data;

import java.util.Map;

@Entity
@Data
public class User extends AbstractBaseEntity {

    private String firstName;
    private String lastName;
    @Index
    private String email;
    private String password;
    private String company;
    private AccountType accountType;
    @Serialize
    private Map<String,Integer> userRatedQuestion;


}
