package com.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Data
public class User extends AbstractBaseEntity {

    private String firstName;
    private String lastName;
    @Index
    private String email;
    private String password;
    private String company;


}
