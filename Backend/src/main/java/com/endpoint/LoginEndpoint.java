package com.endpoint;

import com.daoImpl.UserDaoImpl;
import com.entities.User;
import com.enums.AccountType;
import com.filters.ApiKeyCheck;
import com.googlecode.objectify.ObjectifyService;
import com.response.ApiResponse;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;


@Path("/api/")
public class LoginEndpoint extends AbstractBaseApiEndpoint {
    static UserDaoImpl userOption;
    static{
        userOption=new UserDaoImpl();
    }
    @POST
    @Path("client/clientLogin")
    @ApiKeyCheck
    public Response adminLogin(User user) throws NoSuchAlgorithmException {
        ApiResponse response = new ApiResponse();

        HttpSession session= servletRequest.getSession(false);
        if(session!=null){
            response.setError("Session already exists");
            return Response.status(302).entity(response).build();
        }
        else {
            if (userOption.clientAuthenticate(user)) {

                user= ObjectifyService.ofy().load().type(User.class).filter("email",user.getEmail()).first().now();
                session = servletRequest.getSession(true);
                session.setAttribute("userId", user.getId());
                session.setAttribute("firstName", user.getFirstName());
                session.setAttribute("accountType",user.getAccountType());
                session.setAttribute("company",user.getCompany());
                response.setOk(true);
                response.addData("data","Account login successfull");
                return Response.status(200).entity(response).build();
            } else {
                response.setError("Invalid credentials");
                return Response.status(400).entity(response).build();
            }
        }
        }

        @POST
        @Path("user/userLogin")
        public Response userLogin(User user) throws NoSuchAlgorithmException {
            ApiResponse response = new ApiResponse();

            HttpSession session = servletRequest.getSession(false);
            if (session != null) {
                response.setError("Session already exists");
                return Response.status(302).entity(response).build();
            } else {
                if (userOption.userAuthenticate(user)) {
                    session = servletRequest.getSession(true);
                    user = ObjectifyService.ofy().load().type(User.class).filter("email", user.getEmail()).first().now();
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("firstName", user.getFirstName());
                    session.setAttribute("accountType",user.getAccountType());
                    session.setAttribute("company", user.getCompany());
                    response.setOk(true);
                    response.addData("data", "Account login successfull");
                    return Response.status(200).entity(response).build();
                } else {
                    response.setError("Invalid credentials");
                    return Response.status(400).entity(response).build();

                }
            }
        }

    @POST
    @Path("client/clientSignup")
    @ApiKeyCheck
    public Response adminSignup(User user) throws NoSuchAlgorithmException {
        ApiResponse response = new ApiResponse();
        if(servletRequest.getSession(false)!=null){
            response.setError("Session already exist");
            return Response.status(302).entity(response).build();
        }
        if(user.getPassword()=="" || user.getCompany()=="" || user.getEmail()=="" || user.getFirstName()=="" || user.getLastName()==""){
            response.setError("Empty input fields");
            return Response.status(400).entity(response).build();
        }
        if(userOption.createClientAccount(user)) {
            response.setOk(true);
            response.addData("data","Account Created");
            return Response.status(200).entity(response).build();
        }else{
            response.setError("EmailId is already exist!");
            return Response.status(409).entity(response).build();
        }

    }
    @POST
    @Path("client/userSignup")
    @ApiKeyCheck
    public Response userSignup(User user) throws NoSuchAlgorithmException {
        ApiResponse response = new ApiResponse();
        HttpSession session = servletRequest.getSession(false);
        if(session==null){
            response.setError(" no session exist");
            return Response.status(302).entity(response).build();
        }
        if(session.getAttribute("accountType")!=null && session.getAttribute("accountType").equals(AccountType.ADMIN)) {

            if (user.getPassword() == "" || user.getCompany() == "" || user.getEmail() == "" || user.getFirstName() == "" || user.getLastName() == "") {
                response.setError("Empty input fields");
                return Response.status(400).entity(response).build();
            }
            if (userOption.createUserAccount(user)) {
                response.setOk(true);
                response.addData("data", "Account Created");
                return Response.status(200).entity(response).build();
            } else {
                response.setError("EmailId is already exist!");
                return Response.status(409).entity(response).build();
            }
        }else{
                response.setError("User account is not permitted");
                return Response.status(401).entity(response).build();
            }

    }
    @GET
    @Path("/user/checkEmail")
    @ApiKeyCheck
    public Response getUserEmail(@QueryParam("email") String email){
        ApiResponse response = new ApiResponse();
        HttpSession session = servletRequest.getSession(false);
        if(servletRequest.getSession(false)!=null){
            if(session.getAttribute("accountType")!=null && session.getAttribute("accountType").equals(AccountType.ADMIN)) {
                if(userOption.checkUserEmail(email)) {
                    response.setOk(true);
                    response.addData("email",email);
                    return Response.status(200).entity(response).build();
                }else{
                    response.setError("email not found");
                    return  Response.status(404).entity(response).build();
                }


            }else{
                response.setError("User account is not permitted");
                return Response.status(401).entity(response).build();
            }
        }else{
            response.setError("no session exist");
            return Response.status(401).entity(response).build();
        }
    }


    @GET
    @Path("/logout")
    @ApiKeyCheck
    public Response userLogut(){
        ApiResponse response = new ApiResponse();
        HttpSession session = servletRequest.getSession(false);
        if(session!=null){
            session.invalidate();
            response.setOk(true);
            response.addData("data","Account logged out");
            return Response.status(200).entity(response).build();
        }else {

            response.setError("Please login first");
            return Response.status(401).entity(response).build();
        }
    }
}
