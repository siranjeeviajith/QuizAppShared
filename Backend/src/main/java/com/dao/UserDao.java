package com.dao;

import com.entities.User;

import javax.ws.rs.core.Response;
import java.util.Map;

public interface UserDao {
    public  boolean authenticate(User user);
    public void getUserDetails();
    public boolean createUserAccount(User user);


}
