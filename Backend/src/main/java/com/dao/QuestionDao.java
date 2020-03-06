package com.dao;

import com.entities.Question;
import com.response.ApiResponse;

public interface QuestionDao {
    public ApiResponse addQuestion(Question question);
    public ApiResponse getAllQuestion(Question question);
    public ApiResponse getQuestionByTag(Question question);

}
