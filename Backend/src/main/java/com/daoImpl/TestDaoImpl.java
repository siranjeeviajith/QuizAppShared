package com.daoImpl;

import com.dao.TestDao;
import com.entities.Question;
import com.entities.Test;
import com.entities.User;
import com.enums.TestStatus;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestDaoImpl implements TestDao {

    @Override
    public boolean createTest(Test test) {
        String testURL= (String) test.getTestURL();
        User user= ObjectifyService.ofy().load().type(User.class).filter("email",test.getUserEmail()).first().now();
        if(user==null){
            return false;
        }else{
//            List<String>questionids=test.getQuesionIds();
//            for(String questionId:questionids){
//                Key key = Key.create(Question.class, questionId);
//                Key datastoreKey = ObjectifyService.ofy().load().type(Question.class).filterKey(key).keys().first().now();
//
//                if(!key.equals(datastoreKey)){
//                    return  false;
//
//                }
//            }
            String userId=user.getId();
            long expiryTime=60*60000;
            test.setStatus(TestStatus.NOTSTARTED);
            test.setExpireTime(expiryTime);
          //  System.out.println(test.getQuesionIds());
            test.setUserId(userId);
            String uniqueID = UUID.randomUUID().toString();
            test.setId(uniqueID);
            ObjectifyService.ofy().save().entity(test).now();
            return true;

        }
    }

    @Override
    public boolean saveTest(Test test) {
        if(test==null){
            return  false;
        }
        ObjectifyService.ofy().save().entity(test).now();
        return true;
    }

    @Override
    public String validateTest(Test test, Map testValues) {
      //  if(test.getId().equals())
        return "";
    }
}
