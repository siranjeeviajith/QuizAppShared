package com.dao;

import com.entities.Question;
import com.google.appengine.repackaged.com.google.protobuf.Api;
import com.response.ApiResponse;

import java.util.List;

public interface QuestionDao {
    public ApiResponse addAQuestion(Question question);
    public ApiResponse addAllQuestion(List<Question> questionList);
    public ApiResponse getAllQuestion();
    public ApiResponse getQuestionByTag(String tag);
    public Question getQuestionById(String id);
    public ApiResponse getQuestionsByIds(List<String> questionIds);

}
