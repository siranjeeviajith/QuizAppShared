package com.dao;

import com.entities.Question;
import com.google.appengine.repackaged.com.google.protobuf.Api;
import com.response.ApiResponse;

import java.util.List;

public interface QuestionDao {
    public ApiResponse addAQuestion(Question question);

    public boolean checkQuestionValid(Question question);
    public List<Question> getQuestionByTag(String tag);
    public Question getQuestionById(String id);
    public List<Question> getQuestionByIds(List<String> questionIds);
}
