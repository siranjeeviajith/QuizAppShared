package com.dao;

import com.entities.Test;

import java.util.List;

public interface TestDao {
   public boolean createTest(Test test);
   public boolean saveTest(Test test);
   public String validateTest(Test test);
   public boolean checkTestQuestionsIsValid(List<String> questionIds);
   public List<Test> getAllTestByUser(String userId);
}
