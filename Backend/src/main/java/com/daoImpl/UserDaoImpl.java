package com.daoImpl;

import com.dao.UserDao;
import com.entities.User;
import com.enums.AccountType;
import com.googlecode.objectify.ObjectifyService;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean checkValidDetails(User user) {
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setEmail(user.getEmail().trim());
        user.setPassword(user.getPassword().trim());

        if(user.getFirstName()==null || user.getLastName()==null || user.getEmail()==null || user.getPassword()==null){

            return false;
        }
        if(user.getFirstName().equals("") || user.getLastName().equals("") || user.getEmail().equals("") || user.getPassword().equals("")){

            return false;
        }
        if(user.getFirstName().length()>40 || user.getLastName().length()>40 || user.getEmail().length()>40 || user.getPassword().length()>90){

            return false;
        }
        if(!(user.getEmail().matches(".+\\@.+\\..+"))){
            return false;
        }
        return true;
    }

    @Override
    public boolean clientAuthenticate(User user) throws NoSuchAlgorithmException {
        if(!isUserAuthCredentialsValid(user)){
            return false;
        }

        User existUser=  ObjectifyService.ofy().load().type(User.class).filter("email",user.getEmail()).first().now();
        if(existUser==null){
            return false;
        }
        //System.out.println("DEBUG::income "+user.getEmail()+" "+user.getPassword() +" EXIST:"+existUser.getEmail()+"   "+existUser.getPassword()+"  "+existUser.getAccountType()+" BOOLEAN "+existUser.getPassword().equals(getEncryptedPassword(user.getPassword()))+" exist");
        if( existUser.getAccountType().equals(AccountType.ADMIN) && existUser.getPassword().equals(getEncryptedPassword(user.getPassword()))) {
            return true;
        }else {

            return false;
        }

    }
    @Override
    public boolean userAuthenticate(User user) throws NoSuchAlgorithmException {
        if(!isUserAuthCredentialsValid(user)){
            return false;
        }
        User existUser = ObjectifyService.ofy().load().type(User.class).filter("email", user.getEmail()).first().now();
        if (existUser == null) {
            return false;
        }
       // System.out.println("DEBUG::income "+user.getEmail()+" "+user.getPassword() +" EXIST:"+existUser.getEmail()+"   "+existUser.getPassword()+"  "+existUser.getAccountType()+" BOOLEAN "+existUser.getPassword().equals(getEncryptedPassword(user.getPassword()))+" exist");
        if ( existUser.getPassword().equals(getEncryptedPassword(user.getPassword()))) {
            return true;
        } else {

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
    public boolean checkUserEmail(String email) {

        if(email ==null || email=="" ||email.length()>40 || !(email.matches(".+\\@.+\\..+")) ){
            return false;
        }

        User user=ObjectifyService.ofy().load().type(User.class).filter("email",email).first().now();
        if(user==null){

            return false;
        }

        return true;
    }


    @Override
    public boolean createClientAccount(User user) throws NoSuchAlgorithmException {
        if(!checkValidDetails(user)){
            return false;
        }
        if(user.getCompany()==null) {
            return false;
        }

        user.setCompany(user.getCompany().trim());

        if(user.getCompany().length()>40 || user.getCompany().equals("")){
            return false;
        }
        User existUser = (User) ObjectifyService.ofy().load().type(User.class).filter("email", user.getEmail()).first().now();
        String uniqueID = UUID.randomUUID().toString();
        user.setId(uniqueID);
        user.setAccountType(AccountType.ADMIN);
        user.setPassword(getEncryptedPassword(user.getPassword()));
        if (existUser == null ) {
            ObjectifyService.ofy().save().entity(user).now();
            return true;
        } else{
            return false;
        }

    }
    public boolean createUserAccount(User user) throws NoSuchAlgorithmException{
        if(!checkValidDetails(user)){
            return false;
        }
        User existUser = (User) ObjectifyService.ofy().load().type(User.class).filter("email", user.getEmail()).first().now();
        String uniqueID = UUID.randomUUID().toString();
        user.setId(uniqueID);
        user.setPassword(getEncryptedPassword(user.getPassword()));
        user.setAccountType(AccountType.USER);
        if (existUser == null) {
            ObjectifyService.ofy().save().entity(user).now();
            return true;
        } else{
            return false;
        }
    }

    public boolean isUserAuthCredentialsValid(User user){
        user.setEmail(user.getEmail().trim());
        user.setPassword(user.getPassword().trim());
        if( user.getEmail()==null || user.getPassword()==null){

            return false;
        }
        if( user.getPassword().equals("")){

            return false;
        }
        if(user.getEmail().length()>40 || user.getPassword().length()>90){

            return false;
        }
        if(!(user.getEmail().matches(".+\\@.+\\..+"))){
            return false;
        }
        return true;
    }

}
