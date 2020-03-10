package com.daoImpl;

import com.dao.UserDao;
import com.entities.User;
import com.googlecode.objectify.ObjectifyService;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UserDaoImpl implements UserDao {

    @Override
    public boolean authenticate(User user) throws NoSuchAlgorithmException {
        User existUser=  ObjectifyService.ofy().load().type(User.class).filter("email",user.getEmail()).first().now();
        if(existUser==null){
            return false;
        }
        if(existUser.getPassword().equals(getEncryptedPassword(user.getPassword()))) {
           return true;
        }else {

            return false;
        }

    }

    @Override
    public String getEncryptedPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(password.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    @Override
    public void getUserDetails() {

    }

    @Override
    public boolean createAdminAccount(User user) throws NoSuchAlgorithmException {

        User existUser = (User) ObjectifyService.ofy().load().type(User.class).filter("email", user.getEmail()).first().now();
        String uniqueID = UUID.randomUUID().toString();
        user.setId(uniqueID);
        user.setPassword(getEncryptedPassword(user.getPassword()));
        if (existUser == null) {
            ObjectifyService.ofy().save().entity(user).now();
            return true;
        } else{
            return false;
        }

    }
    public boolean createUserAccount(User user) throws NoSuchAlgorithmException{
        User existUser = (User) ObjectifyService.ofy().load().type(User.class).filter("email", user.getEmail()).first().now();

        String uniqueID = UUID.randomUUID().toString();
        user.setId(uniqueID);
        user.setPassword(getEncryptedPassword(user.getPassword()));
        user.setCompany("client");
        if (existUser == null) {
            ObjectifyService.ofy().save().entity(user).now();
            return true;
        } else{
            return false;
        }
    }

}
