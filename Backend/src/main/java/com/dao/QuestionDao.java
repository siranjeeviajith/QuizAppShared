package com.dao;

import com.entities.Question;
import com.response.ApiResponse;

import java.util.List;

public interface QuestionDao {
    public ApiResponse addAQuestion(Question question);
    public ApiResponse addAllQuestion(List<Question> questionList);
    public ApiResponse getAllQuestion();
    public ApiResponse getQuestionByTag(String tag);

}
