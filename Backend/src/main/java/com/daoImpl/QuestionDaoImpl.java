package com.daoImpl;

import com.dao.QuestionDao;
import com.entities.Question;
import com.entities.Rate;
import com.entities.RatedQuestion;
import com.enums.Option;
import com.enums.QuestionStatus;

import com.googlecode.objectify.ObjectifyService;
import com.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionDaoImpl implements QuestionDao {
    @Override
    public ApiResponse addAQuestion(Question question) {
            ApiResponse response = new ApiResponse();

            String uniqueID = UUID.randomUUID().toString();
            question.setId(uniqueID);
            question.setStatus(QuestionStatus.ACTIVE);
            ObjectifyService.ofy().save().entity(question).now();
            response.setOk(true);
            response.addData("question",question);
            return response;

    }

    @Override
    public boolean checkQuestionValid(Question question) {
        if(question.getCorrectAns()==null || question.getDescription()==null){
            return false;
        }
        if(question.getDescription().length()>300  || question.getTag()==null || question.getTag().equals("")){
            return false;
        }

        if( question.getOption().get(Option.A).equals("")|| question.getOption().get(Option.B).equals("")|| question.getOption().get(Option.C).equals("")|| question.getOption().get(Option.D).equals("")){
            return  false;
        }
        return true;
    }

    @Override
    public List<Question> getQuestionByTag(String tag) {
        List<Question> allQuestions = ObjectifyService.ofy().load().type(Question.class).filter("tag",tag).list();

        return allQuestions;
    }

    @Override
    public Question getQuestionById(String id) {
        return ObjectifyService.ofy().load().type(Question.class).id(id).now();

    }

    @Override
    public List<Question> getAllQuestions() {
        List<Question> allQuestions = ObjectifyService.ofy().load().type(Question.class).limit(50).list();
        return allQuestions;
    }

    @Override
    public void createRating(String userId,RatedQuestion queRating,Question question){
        Rate newRating  = new Rate();
        newRating.setId(queRating.getQuestionId()+userId);
        newRating.setQuestionId(queRating.getQuestionId());
        newRating.setUserId(userId);
        newRating.setRating(queRating.getRating());
        int ratingCount=question.getNoOfUsersRated();
        int avgRating= question.getAverageRating();
        int totalStar=avgRating*ratingCount;
        totalStar+=queRating.getRating();
        ratingCount++;
        avgRating = totalStar/ratingCount;
        question.setAverageRating(avgRating);
        question.setNoOfUsersRated(ratingCount);
      //  System.out.println("DEBUG: CREATING RATING");
        ObjectifyService.ofy().save().entity(question).now();
        ObjectifyService.ofy().save().entity(newRating).now();
    }

    @Override
    public void updateRating(String userId,RatedQuestion queRating,Question question){
        com.googlecode.objectify.Key rateKey=  com.googlecode.objectify.Key.create( Rate.class,question.getId()+userId);
        Rate existRating = (Rate) ObjectifyService.ofy().load().key(rateKey).now();

       // System.out.println("DEBUG: UPDATING RATING");
            int ratingCount = question.getNoOfUsersRated();
            int avgRating = question.getAverageRating();
           // System.out.println("RATING COUNT:"+ratingCount+"\n"+"AVG:"+avgRating);
            avgRating = ((ratingCount * avgRating) - existRating.getRating()  + queRating.getRating() ) / ratingCount;
      //  System.out.println("RATING COUNT:"+ratingCount+"\n"+"AVG:"+avgRating);
            existRating.setRating(queRating.getRating());
            question.setAverageRating(avgRating);

            ObjectifyService.ofy().save().entity(question).now();
            ObjectifyService.ofy().save().entity(existRating).now();

    }

    @Override
    public boolean rateQuestion(List<RatedQuestion> ratings, String userId) {
        for(RatedQuestion queRating:ratings) {

            com.googlecode.objectify.Key queKey= com.googlecode.objectify.Key.create(Question.class,queRating.getQuestionId());
            Question question= (Question) ObjectifyService.ofy().load().key(queKey).now();
            if (queRating.getRating() <= 0 && queRating.getRating() > 5) {
                return false;
            }
            if (queRating.getQuestionId() == null || queRating.getQuestionId().equals("")) {
                return false;
            }
            if(question == null){
                return false;
            }
            com.googlecode.objectify.Key rateKey=  com.googlecode.objectify.Key.create(Rate.class,queRating.getQuestionId()+userId);
            Rate existRating = (Rate) ObjectifyService.ofy().load().key(rateKey).now();

           // System.out.println("DEBUG:"+ queRating.getQuestionId()+"  "+queRating.getRating());
            if(existRating==null){
                createRating(userId,queRating,question);


            }else{
                updateRating(userId,queRating,question);
            }

        }
        return  true;
    }

    @Override
    public List<Question> getQuestionByIds(List<String> questionIds) {
        List<Question> questionList=new ArrayList<>();
        for(String questionId:questionIds){
            Question question = getQuestionById(questionId);
            if(question!=null) {
                question.setCorrectAns(null);
                questionList.add(question);
            }


        }
        return  questionList;
    }
}
