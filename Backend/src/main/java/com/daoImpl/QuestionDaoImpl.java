package com.daoImpl;

import com.dao.QuestionDao;
import com.entities.Question;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;
import com.response.ApiResponse;

import java.util.List;
import java.util.UUID;

public class QuestionDaoImpl implements QuestionDao {
    @Override
    public ApiResponse addAQuestion(Question question) {
        ApiResponse response = new ApiResponse();
        Question existQuestion = ObjectifyService.ofy().load().type(Question.class).filter("description",question.getDescription()).first().now();
        if(existQuestion!=null){
             response.setOk(false);
             response.setError("question already exists");
             return response;
        }else{
            String uniqueID = UUID.randomUUID().toString();
            question.setId(uniqueID);
            ObjectifyService.ofy().save().entity(question).now();
            response.setOk(true);
            response.addData("result","question added");
            return response;

        }


    }

    @Override
    public ApiResponse addAllQuestion(List<Question> questionList) {
        ApiResponse response = new ApiResponse();
        for(Question question:questionList){
            Question existQuestion = ObjectifyService.ofy().load().type(Question.class).filter("description",question.getDescription()).first().now();
            if(existQuestion!=null) {
                response.addData(question.getDescription(),"question already exist");
            }
            else {
                String uniqueID = UUID.randomUUID().toString();
                question.setId(uniqueID);
                ObjectifyService.ofy().save().entity(question).now();
                response.addData(question.getDescription(),"question added");
            }
        }
            response.setOk(true);
            return response;
        }



    @Override
    public ApiResponse getAllQuestion(Question question) {
        List<Question> allQuestions = ObjectifyService.ofy().load().type(Question.class).filter("company",question.getCompany()).list();
        ApiResponse response = new ApiResponse();
        response.setOk(true);
        response.addData("questions",allQuestions);
        return response;
    }

    @Override
    public ApiResponse getQuestionByTag(Question question) {
        List<Question> allQuestions = ObjectifyService.ofy().load().type(Question.class).filter("company",question.getCompany()).filter("tag",question.getTag()).list();
        ApiResponse response = new ApiResponse();
        response.setOk(true);
        response.addData("questions",allQuestions);
        return response;
    }
}
