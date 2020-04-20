package com.daoImpl;

import com.entities.Question;
import com.entities.Rate;
import com.entities.User;
import com.enums.Option;
import com.enums.QuestionStatus;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class QuestionDaoImplTest {
    static Closeable closeable;
    static QuestionDaoImpl questionService ;
    static Question question;
    static final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());



    @BeforeEach
    public  void setUp(){
        helper.setUp();
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(User.class);
        ObjectifyService.register(Question.class);
        ObjectifyService.register(Rate.class);
        closeable = ObjectifyService.begin();
        questionService = new QuestionDaoImpl();

    }

    @AfterEach
    public void tearDown() {
        helper.tearDown();
        closeable.close();
    }


    @Test
    void addAQuestion_valid_input() {
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        Assert.assertTrue("question not added",questionService.addAQuestion(question));
    }

    @Test
    void addAQuestion_invalid_input() {
        question = createQuestionForTest("","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        Assert.assertFalse("question added",questionService.addAQuestion(question));
    }

    @Test
    void checkQuestionValid_valid_input() {
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        Assert.assertTrue("questionInvalid",questionService.checkQuestionValid(question));
    }

    @Test
    void checkQuestionValid_invalid_input() {
        question = createQuestionForTest("java","what is java ? this question is very long                with lot of space                                             and lot of more characters and  length efsdfasfsd dsfsdf dsfdgdfds seriouly long characters and checking ......................................","A programming language ","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        Assert.assertFalse("question description is valid",questionService.checkQuestionValid(question));

        question = createQuestionForTest("java","what is java ?","A programming language actually option is very long ...................and definitly contains empty spaces                             ","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        Assert.assertFalse("question optionA is valid",questionService.checkQuestionValid(question));
    }
    @Test
    void getQuestionByTag_with_valid_input() {
        String tag="java";
        List<Question>expected = new ArrayList<>();
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        expected.add(question);
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        expected.add(question);
        question = createQuestionForTest("html/css","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        question = createQuestionForTest("javascript","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        List<Question> actual = questionService.getQuestionByTag(tag);

        Assertions.assertIterableEquals(expected,actual,"Getting question by tag  failed");


    }

    @Test
    void getQuestionByTag_with_invalid_input() {
        String tag="python";
        List<Question>expected = new ArrayList<>();
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);

        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);

        question = createQuestionForTest("html/css","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        question = createQuestionForTest("javascript","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        List<Question> actual = questionService.getQuestionByTag(tag);

        Assertions.assertIterableEquals(expected,actual,"Getting question by tag -- wrong tag --  passed");


    }


    @Test
    void getQuestionById_with_valid_input() {
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        String expectedQuestionId=question.getId();
        question = createQuestionForTest("java","what is class ?","a specification","it is a entity","it is a design pattern","nothing but a class room", Option.B);
        questionService.addAQuestion(question);
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        question.setStatus(QuestionStatus.ACTIVE);
        question.encryptDescription();
        Assert.assertEquals("Getting question by id -- wrong question --",question,questionService.getQuestionById(expectedQuestionId));

    }

    @Test
    void getQuestionById_with_invalid_input() {
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);

        question = createQuestionForTest("java","what is class ?","a specification","it is a entity","it is a design pattern","nothing but a class room", Option.B);
        questionService.addAQuestion(question);

        Assert.assertEquals("Getting question by id -- wrong question id -- passed ",null,questionService.getQuestionById("invalidId-not-present-in-db"));

    }

    @Test
    void getAllQuestions_with_valid_input() {
        List<Question>expected = new ArrayList<>();
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        expected.add(question);
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        expected.add(question);
        question = createQuestionForTest("html/css","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        expected.add(question);
        question = createQuestionForTest("javascript","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        expected.add(question);
        List<Question>actual = questionService.getAllQuestions();

        Assert.assertTrue("Get all question -- mismatch with db --",expected.containsAll(actual));

    }

    @Test
    void getAllQuestions_with_invalid_input() {
        List<Question>expected = new ArrayList<>();


        Assertions.assertIterableEquals(expected,questionService.getAllQuestions(),"get all question -- no questions in db -- passed");

    }


    @Test
    void rateQuestion_valid_input() {
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);

        Rate rating = new Rate();
        rating.setUserId("user3301");
        rating.setQuestionId(question.getId());
        rating.setRating(3);
        Assert.assertTrue(questionService.rateQuestion(rating));
        Assert.assertEquals("incorrect rating",3,question.getAverageRating());

        rating.setUserId("user123");
        rating.setRating(5);
        Assert.assertTrue(questionService.rateQuestion(rating));
        Assert.assertEquals("incorrect rating ",4,question.getAverageRating());
        Assert.assertEquals("no of user rated -- wrong count --",2,question.getNoOfUsersRated());
    }

    @Test
    void rateQuestion_invalid_input() {
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);

        Rate rating = new Rate();
        rating.setUserId("user3301");
        rating.setQuestionId(question.getId());
        rating.setRating(8);
        Assert.assertFalse("rating above 5 invalid -- passed",questionService.rateQuestion(rating));
        Assert.assertEquals("incorrect rating",0,question.getAverageRating());

        rating.setRating(-2);
        Assert.assertFalse("rating above 5 invalid -- passed",questionService.rateQuestion(rating));
        Assert.assertEquals("incorrect rating",0,question.getAverageRating());
    }


//    @Test
//    void getQuestionByIds() {
//    }

    public static Question createQuestionForTest(String tag,String description,String optionA,String optionB, String optionC, String optionD,Option correctAns){
        Question question = new Question();
        question.setTag(tag);
        question.setDescription(description);
        Map<Option,String> option = new HashMap<>();
        option.put(Option.A,optionA);
        option.put(Option.B,optionB);
        option.put(Option.C,optionC);
        option.put(Option.D,optionD);
        question.setOption(option);
        question.setCorrectAns(correctAns);
        return question;
    }
//    public boolean compareListOfQuestion(List<Question>expected, List<Question>actual){
//        if(expected.size()!=actual.size()){
//            return false;
//        }
//        for(int i=0;i<expected.size();i++){
//            if(!compareQuestion(expected.get(i),actual.get(i))){
//                return false;
//            }
//        }
//        return true;
//    }
//    public boolean compareQuestion(Question expected,Question actual){
//        if(!expected.getTag().equals(actual.getTag()) || !expected.getDescription().equals(actual.getDescription()) || !expected.getCorrectAns().equals(actual.getCorrectAns())){
//            return false;
//        }
//        if(!expected.getOption().get(Option.A).equals(actual.getOption().get(Option.A)) || !expected.getOption().get(Option.B).equals(actual.getOption().get(Option.B)) || !expected.getOption().get(Option.C).equals(actual.getOption().get(Option.C))|| !expected.getOption().get(Option.D).equals(actual.getOption().get(Option.D))){
//            return false;
//        }
//        if( expected.getAverageRating()!=actual.getAverageRating() || expected.getNoOfUsersRated()!=actual.getNoOfUsersRated() || expected.getTotalRating()!= actual.getTotalRating()){
//            return false;
//        }
//        return true;
//    }

}