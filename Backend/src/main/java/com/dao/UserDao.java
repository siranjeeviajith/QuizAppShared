package com.dao;

import com.entities.User;

import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface UserDao {
    public  boolean authenticate(User user) throws NoSuchAlgorithmException;
    public String getEncryptedPassword(String password) throws NoSuchAlgorithmException;
    public void getUserDetails();
    public boolean createAdminAccount(User user) throws NoSuchAlgorithmException;
    public boolean createUserAccount(User user) throws NoSuchAlgorithmException;


}
