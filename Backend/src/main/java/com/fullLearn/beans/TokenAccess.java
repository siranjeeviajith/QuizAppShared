package com.fullLearn.beans;


import lombok.Data;

@Data
public class TokenAccess {


    private String access_token;
    private String token_type;
    private int expires_in;
    private String user_id;
    private String refresh_token;


}
