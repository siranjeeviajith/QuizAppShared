package com.daoImpl;

import com.dao.TestDao;
import com.entities.Question;
import com.entities.Test;
import com.entities.User;
import com.enums.TestStatus;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;
import java.util.UUID;

public class TestDaoImpl implements TestDao {

    @Override
    public boolean createTest(Test test) {
        if(test.getDuration()<1 || test.getDuration()>1440){
            return false;
        }
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
           // test.setExpireTime(Constant.EXPIRY_TIME);
          //  System.out.println(test.getQuesionIds());
            test.setDuration(test.getDuration()*60);
            test.setUserId(userId);
            String uniqueID = UUID.randomUUID().toString();
            test.setId(uniqueID);
            String testUrl = UUID.randomUUID().toString();
            test.setTestURL(testUrl);
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
    public List<Test> getAllTestByUser(String userId) {
        return ObjectifyService.ofy().load().type(Test.class).filter("createdBy",userId).limit(50).list();
    }

    @Override
    public String validateTest(Test test) {
        int correctAns = 0;
        int unansweredQuestion = 0;
        int totalQuestions = test.getQuestionIds().size();

        List<String> testQuestionIds = test.getQuestionIds();
        List<Question> attendedQuestions = test.getQueList();
        // System.out.println(attendedQuestions);
        if (attendedQuestions.size() < totalQuestions) {
            unansweredQuestion += totalQuestions - attendedQuestions.size();
        }
        if (!attendedQuestions.isEmpty()) {
            for (Question questionAttended : attendedQuestions) {
                if (testQuestionIds.contains(questionAttended.getId())) {

                    Question correctQuestion = ObjectifyService.ofy().load().type(Question.class).id(questionAttended.getId()).now();
                    if (correctQuestion.getCorrectAns().toString().equals(questionAttended.getSelectedOption().toString())) {
                        correctAns++;
                    }
                    if (questionAttended.getSelectedOption() == null || questionAttended.getSelectedOption().toString().trim().equals("")) {
                        unansweredQuestion++;
                    }
                }
            }
        }
            return "Total Question:"+totalQuestions+"  "+"Total Score:"+totalQuestions+"  "+"Your Score:"+correctAns +"   UnAnsweredQuestion:" + unansweredQuestion;


    }

    @Override
    public boolean checkTestQuestionsIsValid(List<String> questionIds) {

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
