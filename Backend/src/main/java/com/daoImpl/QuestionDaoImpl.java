package com.daoImpl;

import com.dao.QuestionDao;
import com.entities.Question;
import com.enums.Option;
import com.enums.QuestionStatus;
import com.googlecode.objectify.ObjectifyService;
import com.response.ApiResponse;

import javax.ws.rs.core.Response;
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
