package com.daoImpl;

import com.Constants.Constant;
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

            test.setStatus(TestStatus.NOTSTARTED);
            test.setExpireTime(Constant.EXPIRY_TIME);
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
        int  correctAns = 0;
        int unansweredQuestion=0;
        int totalQuestions = test.getQuestionIds().size();
        if(test.getId().equals(testValues.get("id"))){
            List<String> testQuestionIds=test.getQuestionIds();
                List<Map> attendedQuestions =  (List<Map>)testValues.get("questions");
               // System.out.println(attendedQuestions);
            for(Map questionAttended:attendedQuestions){
                if(testQuestionIds.contains(questionAttended.get("id"))){
                    Question correctQuestion = ObjectifyService.ofy().load().type(Question.class).id(questionAttended.get("id").toString()).now();

                    if(correctQuestion.getCorrectAns().toString().equals(questionAttended.get("choosedOption"))){
                            correctAns++;
                    }
                    if(questionAttended.get("choosedOption")==null||questionAttended.get("choosedOption").toString().trim().equals("") ){
                        unansweredQuestion++;
                    }
                }
            }
            return correctAns+"/"+totalQuestions  + "\n AnsweredQuestion:"+(totalQuestions-unansweredQuestion)+"\t UnAnsweredQuestion:"+unansweredQuestion;
        }
        return "invalid test submitted";
    }

    @Override
    public boolean checkTestValid(List<String> questionIds) {

        if(questionIds.isEmpty()){
            return false;
        }
        for(String questionId:questionIds){
            Question question=ObjectifyService.ofy().load().type(Question.class).id(questionId).now();
            if(question==null){
                return  false;
            }
        }

        return true;
    }
}
