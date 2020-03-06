package com.daoImpl;

import com.dao.UserDao;
import com.entities.User;
import com.googlecode.objectify.ObjectifyService;

import java.util.UUID;

public class UserDaoImpl implements UserDao {

    @Override
    public boolean authenticate(User user) {
        User existUser=  ObjectifyService.ofy().load().type(User.class).filter("email",user.getEmail()).first().now();
        if(existUser==null){
            return false;
        }
        if(existUser.getPassword().equals(user.getPassword())) {
           return true;
        }else {

            return false;
        }

    }

    @Override
    public void getUserDetails() {

    }

    @Override
    public boolean createUserAccount(User user) {

        User existUser = (User) ObjectifyService.ofy().load().type(User.class).filter("email", user.getEmail()).first().now();
        String uniqueID = UUID.randomUUID().toString();
        user.setId(uniqueID);
        if (existUser == null) {
            ObjectifyService.ofy().save().entity(user).now();
            return true;
        } else{
            return false;
        }

    }

}
