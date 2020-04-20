package com.daoImpl;

import com.entities.User;
import com.enums.AccountType;
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

class UserDaoImplTest {
    static  Closeable closeable;
    static UserDaoImpl userService ;
    static User user;
    User userAuth;

    static final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());




    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        helper.setUp();
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(User.class);
        closeable = ObjectifyService.begin();
        userService=new UserDaoImpl();
        userAuth = new User();

    }

    @AfterEach
    public void tearDown() {

        helper.tearDown();
            closeable.close();
    }

    @Test
    void checkValidDetails_with_invalid_inputs() {
        user = createUserEntityForTest("ajith7812@wnskfnsdfaraja sivamurugan,manijfkjfkfffffffkfffffffff","ajith","ajith@gmail.com","123","FULL");
        Assert.assertFalse("user details --first name -- validation passed", userService.checkValidDetails(user));

        user = createUserEntityForTest("ajith","dafffffffffffffffffffakfnsdfaraja sivamurugan,mani","ajith@gmail.com","s.r@nj##v*","FULL");
        Assert.assertFalse("user details -- last name -- validation passed",userService.checkValidDetails(user));

        user = createUserEntityForTest("helena","helix","helanaHelix@WCD.in","","International World Health Organization");
        Assert.assertFalse("user details -- blank password -- validation passed",userService.checkValidDetails(user));

        user = createUserEntityForTest("carl","johnson","johnCarl@yahooin","carl@406590915#yahoo*ac^!","RockStarStudio@SaNaNdreaS");
        Assert.assertFalse("user details -- invalid email -- validation passed",userService.checkValidDetails(user));
    }

    @Test
    void checkValidDetails_with_valid_inputs() {
        user = createUserEntityForTest("ajith","ajith","ajith@gmail.com","123","FULL");
        Assert.assertTrue("user details validation failed",  userService.checkValidDetails(user));

        user = createUserEntityForTest("siranjeevi","siranjeevi","siranjeevi@gmail.com","siranjeevi@2020/7/8/1","");
        Assert.assertTrue("user details validation failed",userService.checkValidDetails(user));

        user = createUserEntityForTest("sanjay","sanjay","s@gmail.com","sanjay@eshwar123123","OLX");
        Assert.assertTrue("user details validation failed",userService.checkValidDetails(user));

        user = createUserEntityForTest("karunya","selvi","k@gmail.com","selvi@karunya","University");
        Assert.assertTrue("user details validation failed",userService.checkValidDetails(user));
    }

    @Test
    void createClientAccount_with_valid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("ajith","ajith","ajith@gmail.com","123","FULL");
        Assert.assertTrue("Account creation fails",  userService.createClientAccount(user));
        String expectedId = user.getId();
        user = createUserEntityForTest("ajith","ajith","ajith@gmail.com","a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3","FULL");
        user.setId("customId110101011");
        user.setAccountType(AccountType.ADMIN);
        Assert.assertEquals("Account creation -- wrong user stored -- ",user,ObjectifyService.ofy().load().type(User.class).id(expectedId).now());
        Assert.assertFalse("Account creation -- exist email -- passed",  userService.createClientAccount(user));
    }

    @Test
    void createClientAccount_with_invalid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("ajith","ajith","ajith@gmail.com","123","  amazon,flipkart,google,yahoo,orkut,                                                                                                                                                                                               facebook,instagram,whatsapp,microsoft,snapdeal,tumblr,twitter                   ");
        Assert.assertFalse("Account creation -- company blank spaces -- passed",  userService.createClientAccount(user));
    }

    @Test
    void createUserAccount_with_valid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("Siranjeevi","ajith","siranjeevi@gmail.com","123","");
        Assert.assertTrue("Account creation fails",  userService.createUserAccount(user));

        Assert.assertFalse("Account creation -- exist email -- passed",  userService.createClientAccount(user));
    }

    @Test
    void createUserAccount_with_invalid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("Siranjeevi","ajith","siranjeevi@gmail.com","","");
        Assert.assertFalse("Account creation -- blank password --  passed",  userService.createUserAccount(user));
    }

    @Test
    void clientAuthenticate_with_valid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("ajith","ajith","ajith@gmail.com","123","FULL");
        userService.createClientAccount(user);
        userAuth.setEmail("ajith@gmail.com");
        userAuth.setPassword("123");
        Assert.assertTrue("Client login fails",  userService.clientAuthenticate(userAuth));
    }
    @Test
    void clientAuthenticate_with_invalid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("ajith","ajith","ajith@gmail.com","123","FULL");
        userService.createClientAccount(user);
        userAuth.setEmail("ajith@gmail.com");
        userAuth.setPassword("12$");
        Assert.assertFalse("Client login - wrong password - passed ",  userService.clientAuthenticate(userAuth));
    }

    @Test
    void userAuthenticate_with_valid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("Siranjeevi","ajith","siranjeevi@gmail.com","123","");
        userService.createUserAccount(user);
        userAuth.setEmail("siranjeevi@gmail.com");
        userAuth.setPassword("123");
        Assert.assertTrue("User login fails", userService.userAuthenticate(userAuth));
    }

    @Test
    void userAuthenticate_with_invalid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("Siranjeevi","ajith","siranjeevi@gmail.com","123","");
        userService.createUserAccount(user);
        userAuth.setEmail("siranjevi@gmail.com");
        userAuth.setPassword("123");

        Assert.assertFalse("User login - wrong email - passed", userService.userAuthenticate(userAuth));

        userAuth.setEmail("someverylongemailwhichisveryverylongandcontainsmorecharacters@hellothere.commmm");
        Assert.assertFalse("user authenticate -- long email id -- passed",userService.userAuthenticate(userAuth));

        userAuth.setPassword("someverylongemailwhichisveryverylongandcontainsmorecharacterssohellotherehow r u.");
        Assert.assertFalse("user authenticate -- long password -- passed",userService.userAuthenticate(userAuth));
    }

    @Test
    void checkUserEmail_with_valid_input() throws NoSuchAlgorithmException {
        user = createUserEntityForTest("Siranjeevi","ajith","siranjeevi@gmail.com","123","FULL");
         userService.createClientAccount(user);

         Assert.assertTrue("Email check fails", userService.checkUserEmail(user.getEmail()));
    }

    @Test
    void checkUserEmail_with_invalid_input() throws NoSuchAlgorithmException {

        Assert.assertFalse("Email check -- wrong email -- passed", userService.checkUserEmail("ajith@gmail.com"));

        Assert.assertFalse("Email check -- null -- passed",userService.checkUserEmail(null));

        Assert.assertFalse("Email check -- empty -- passed",userService.checkUserEmail(""));
        Assert.assertFalse("Email check -- invalid email -- passed",userService.checkUserEmail("-dffs364^%$2"));
    }

    public static User createUserEntityForTest(String fName,String lName,String email,String password,String company){
        User user =  new User();
        user.setFirstName(fName);
        user.setLastName(lName);
        user.setEmail(email);
        user.setCompany(company);
        user.setPassword(password);
        return user;
    }



}