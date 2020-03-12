package com.endpoint;

import com.daoImpl.TestDaoImpl;
import com.entities.Test;
import com.entities.User;
import com.enums.AccountType;
import com.enums.TestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filters.ApiKeyCheck;
import com.google.appengine.repackaged.com.google.protobuf.Api;
import com.googlecode.objectify.ObjectifyService;
import com.response.ApiResponse;
import com.services.TemplateService;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;

@Path("/api/test")
public class TestEndpoint extends AbstractBaseApiEndpoint {
    static TestDaoImpl testOption;

    @POST
    @Path("/generateTestLink")
    @ApiKeyCheck
    public Response generateTestLink(Test test) {

        testOption = new TestDaoImpl();
        ApiResponse response = new ApiResponse();
        String testUrl = UUID.randomUUID().toString();
        HttpSession session = servletRequest.getSession(false);
        if (servletRequest.getSession(false) != null) {
            if (session.getAttribute("accountType") != null && session.getAttribute("accountType").equals(AccountType.ADMIN)) {

                test.setTestURL(testUrl);
                if (testOption.createTest(test)) {
                    response.setOk(true);
                    response.addData("testURL", "http://localhost:8080/api/test/" + testUrl);
                    return Response.status(200).entity(response).build();
                } else {
                    response.setError("Error while creating test");
                    return Response.status(400).entity(response).build();
                }
            } else {
                response.setError("User account is not permitted");
                return Response.status(401).entity(response).build();
            }
        } else {
            response.setError("no session exist");
            return Response.status(401).entity(response).build();
        }
    }




    @GET
    @Path("/{testURL}")
    @Produces(MediaType.TEXT_HTML)
    public Response getTest(@PathParam("testURL") String testURL) throws IOException {
        String response = "";
        List<String> data = new ArrayList<>();
        Test test = ObjectifyService.ofy().load().type(Test.class).filter("testURL", testURL).first().now();
        if (test == null) {
            response = "no test exist";
            return Response.status(400).entity(response).build();
        }
        if(!test.getStatus().equals(TestStatus.NOTSTARTED)){
            response="test is "+test.getStatus();
            data.add(response);
            String content = TemplateService.modify(servletContext, data, "/resources/errorPageTemplate.html");
            return Response.status(403).entity(content).build();
        }
        if (servletRequest.getSession(false) != null) {
            if (servletRequest.getSession(false).getAttribute("userId") != null && servletRequest.getSession(false).getAttribute("userId").toString().equals(test.getUserId())) {
                servletResponse.sendRedirect(servletRequest.getRequestURL() + "/doTest");
                return Response.status(302).entity("<h1>session exist</h1>").build();
            } else {
                return Response.status(400).entity("<h1>This test is not for you</h1>").build();
            }
        }
        else {
            User user = ObjectifyService.ofy().load().type(User.class).id(test.getUserId()).now();
            test.setUserEmail(user.getEmail());

            Map<String, String> testDetails = new HashMap<>();
            testDetails.put("testId", test.getId());
            testDetails.put("userId", test.getUserId());
            data.add(Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsString(testDetails).getBytes()));
            data.add(test.getUserEmail());
            String content = TemplateService.modify(servletContext, data, "/resources/testLoginTemplate.html");
            return Response.status(200).entity(content).build();

        }

    }

    @GET
    @Path("/{testURL}/doTest")
    @Produces(MediaType.TEXT_HTML)
    public Response doTest(@PathParam("testURL") String testURL) throws IOException {
        String response;
        List<String> data = new ArrayList<>();
        Test test = ObjectifyService.ofy().load().type(Test.class).filter("testURL", testURL).first().now();
        if (test == null) {
            response = "no test exist";
            data.add(response);
            String content = TemplateService.modify(servletContext, data, "/resources/errorPageTemplate.html");
            return Response.status(400).entity(content).build();

        }
        if (servletRequest.getSession(false) != null) {
            if (servletRequest.getSession(false).getAttribute("userId") != null && servletRequest.getSession(false).getAttribute("userId").toString().equals(test.getUserId())) {


                data.add(Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsString(test).getBytes()));
                String content = TemplateService.modify(servletContext, data, "/resources/testTemplate.html");
                return Response.status(200).entity(content).build();
            }
            else{
                response ="This test not for you";
                data.add(response);
                String content = TemplateService.modify(servletContext, data, "/resources/errorPageTemplate.html");
                return Response.status(403).entity(content).build();

            }
        }else{
            return null;
        }
    }

}

