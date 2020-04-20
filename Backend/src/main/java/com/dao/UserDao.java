package com.dao;

import com.entities.User;

import java.security.NoSuchAlgorithmException;

public interface UserDao {
    public boolean checkValidDetails(User user);
    public  boolean userAuthenticate(User user) throws NoSuchAlgorithmException;
    public  boolean clientAuthenticate(User user) throws NoSuchAlgorithmException;
    public String getEncryptedPassword(String password) throws NoSuchAlgorithmException;

    public boolean checkUserEmail(String email);
    public boolean createClientAccount(User user) throws NoSuchAlgorithmException;
    public boolean createUserAccount(User user) throws NoSuchAlgorithmException;


}
