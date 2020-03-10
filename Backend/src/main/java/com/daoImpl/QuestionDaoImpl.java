package com.daoImpl;

import com.dao.QuestionDao;
import com.entities.Question;
import com.enums.QuestionStatus;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;
import com.response.ApiResponse;

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
    public ApiResponse addAllQuestion(List<Question> questionList) {
        ApiResponse response = new ApiResponse();
        for(Question question:questionList){

            String uniqueID = UUID.randomUUID().toString();
            question.setId(uniqueID);
            question.setStatus(QuestionStatus.ACTIVE);
                ObjectifyService.ofy().save().entity(question).now();
                response.addData(question.getDescription(),"question added");
            }
            response.setOk(true);
            return response;
        }



    @Override
    public ApiResponse getAllQuestion() {
        List<Question> allQuestions = ObjectifyService.ofy().load().type(Question.class).list();
        ApiResponse response = new ApiResponse();
        response.setOk(true);
        response.addData("questions",allQuestions);
        return response;
    }

    @Override
    public ApiResponse getQuestionByTag(String tag) {
        List<Question> allQuestions = ObjectifyService.ofy().load().type(Question.class).filter("tag",tag).list();
        ApiResponse response = new ApiResponse();
        response.setOk(true);
        response.addData("questions",allQuestions);
        return response;
    }
}
