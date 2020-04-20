package com.daoImpl;

import com.entities.Question;
import com.entities.User;
import com.enums.Option;
import com.enums.TestStatus;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.daoImpl.QuestionDaoImplTest.createQuestionForTest;
import static com.daoImpl.UserDaoImplTest.createUserEntityForTest;

class TestDaoImplTest {
    static Closeable closeable;
    static com.entities.Test test;
    static User user;
    static Question question;
    static Question question1;
    static QuestionDaoImpl questionService;
    static UserDaoImpl userService;
    static TestDaoImpl testService ;
    List<String> questionIds;
    static final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeEach
    public  void setUp() throws NoSuchAlgorithmException {
        helper.setUp();
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(User.class);
        ObjectifyService.register(Question.class);
        ObjectifyService.register(com.entities.Test.class);
        testService = new TestDaoImpl();
        userService=new UserDaoImpl();
        questionService= new QuestionDaoImpl();
        closeable = ObjectifyService.begin();


        user = createUserEntityForTest("Siranjeevi","ajith","siranjeevi@gmail.com","123","");
        userService.createUserAccount(user);
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        questionIds= new ArrayList<>();
        questionIds.add(question.getId());
        question1 = createQuestionForTest("java","what is class ?","a specification","it is a entity","it is a design pattern","nothing but a class room", Option.B);
        questionService.addAQuestion(question1);
        questionIds.add(question1.getId());

    }

    @AfterEach
    public void tearDown() {
        helper.tearDown();
        closeable.close();
    }


    @Test
    void createTest_with_valid_input(){

        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-01",questionIds,2L);
        Assert.assertTrue("Test creation -- valid test -- fails",testService.createTest(test));

        Assert.assertEquals("Test creation -- test not present in db -- ",test,ObjectifyService.ofy().load().type(com.entities.Test.class).id(test.getId()).now());
    }

    @Test
    void createTest_with_invalid_input() {


        test = createEntityForTest("siranjeeevi@gmail.com","client--FULL-01",questionIds,2L);
        Assert.assertFalse("Test creation -- wrong email -- passed",testService.createTest(test));

        
    }

    @Test
    void saveTest_with_valid_input() {
        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-01",questionIds,2L);
        testService.createTest(test);

        test.setStatus(TestStatus.ONGOING);
        testService.saveTest(test);

        Assert.assertEquals("Test saving -- updated data mismatch with db --",test,ObjectifyService.ofy().load().type(com.entities.Test.class).id(test.getId()).now());

    }

    @Test
    void getAllTestByUser_with_valid_input() {
        List<com.entities.Test> expected = new ArrayList<>();
        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-01",questionIds,2L);
        testService.createTest(test);
        expected.add(test);
        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-01",questionIds,2L);
        testService.createTest(test);
        expected.add(test);
        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-02",questionIds,2L);
        testService.createTest(test);
        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-03",questionIds,2L);
        testService.createTest(test);

        Assert.assertTrue("Get all test by user created -- valid check -- failed",expected.containsAll(testService.getAllTestByUser("client--FULL-01")));
    }

    @Test
    void getAllTestByUser_with_invalid_input() {
        List<com.entities.Test> expected = new ArrayList<>();
        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-01",questionIds,2L);
        testService.createTest(test);

        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-01",questionIds,2L);
        testService.createTest(test);

        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-02",questionIds,2L);
        testService.createTest(test);
        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-03",questionIds,2L);
        testService.createTest(test);

        Assert.assertTrue("Get all test by user created -- valid check -- failed",expected.containsAll(testService.getAllTestByUser("client--FULL-05")));
    }

    @Test
    void validateTest_with_valid_input() {
        test = createEntityForTest("siranjeevi@gmail.com","client--FULL-01",questionIds,2L);
        testService.createTest(test);
        test.setStatus(TestStatus.COMPLETED);


        question.setId(test.getQuestionIds().get(0));

        question.setSelectedOption(Option.C);
        test.setQueList(new ArrayList<Question>());
        test.getQueList().add(question);

        question1.setId(test.getQuestionIds().get(1));
        question1.setSelectedOption(Option.B);
        test.getQueList().add(question1);
        Assert.assertEquals("Test validation -- valid test --","Total Question:"+2+"  "+"Total Score:"+2+"  "+"Your Score:"+1 +"   UnAnsweredQuestion:" + 0,testService.validateTest(test));

    }

    @Test
    void checkTestQuestionsIsValid_with_valid_input() {
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        List<String>questionIds= new ArrayList<>();
        questionIds.add(question.getId());
        question = createQuestionForTest("java","what is class ?","a specification","it is a entity","it is a design pattern","nothing but a class room", Option.B);
        questionService.addAQuestion(question);
        questionIds.add(question.getId());

        Assert.assertTrue("Check Questions is valid -- valid questions -- failed",testService.checkTestQuestionsIsValid(questionIds));


    }

    @Test
    void checkTestQuestionsIsValid_with_invalid_input() {
        question = createQuestionForTest("java","what is java ?","A programming language","it is a protcol","it is Ancient language","nothing but a symbol for coffee", Option.A);
        questionService.addAQuestion(question);
        List<String>questionIds= new ArrayList<>();
        questionIds.add(question.getId());
        question = createQuestionForTest("java","what is class ?","a specification","it is a entity","it is a design pattern","nothing but a class room", Option.B);
        questionService.addAQuestion(question);
        questionIds.add("Wrong-id-242131232-21323213");

        Assert.assertFalse("Check Questions is valid -- invalid question id -- passed",testService.checkTestQuestionsIsValid(questionIds));


    }

    public static com.entities.Test createEntityForTest(String userEmail, String createdByClientId, List<String> questionIds ,Long durationInMin){
        test  = new com.entities.Test();
        test.setDuration(durationInMin);
        test.setUserEmail(userEmail);
        test.setCreatedBy(createdByClientId);
        test.setQuestionIds(questionIds);
        testService.createTest(test);
        return test;
    }
}