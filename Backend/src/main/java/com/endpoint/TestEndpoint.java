package com.endpoint;

import com.daoImpl.TestDaoImpl;
import com.entities.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.ObjectifyService;
import com.response.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Path("/api/test")
public class TestEndpoint extends AbstractBaseApiEndpoint
{
    static TestDaoImpl testOption;
    @POST
    @Path("/generateTestLink")
    public Response generateTestLink(Map testDetails){
        testOption=new TestDaoImpl();
        ApiResponse response = new ApiResponse();
        String testUrl="http://localhost:8080/api/test/"+ UUID.randomUUID().toString();
        testDetails.put("testURL",testUrl);
        if(testOption.createTest(testDetails)) {
            response.setOk(true);
            response.addData("testURL", testUrl);
            return  Response.status(200).entity(response).build();
        }else {
            response.setError("Error while creating test");
            return Response.status(400).entity(response).build();
        }



    }
    @GET
    @Path("/{testURL}")
    public  Response getTest(@PathParam("testURL") String testURL) throws IOException {
        ApiResponse response=new ApiResponse();
        Test test=ObjectifyService.ofy().load().type(Test.class).filter("testURL",servletRequest.getRequestURL().toString().replace("/doTest","")).first().now();
        if(test==null){
            response.setError("no URL exist!");
            return Response.status(400).entity(response).build();
        }
        if(servletRequest.getSession(false)==null){
            response.setError("pleaseLogin");
            //servletResponse.sendRedirect("/");
            return Response.status(401).entity(response).build();
        }
        else if(!(servletRequest.getSession(false).getAttribute("userId").toString().equals(test.getUserId()))) {
            response.setError("Wrong User!");
            //servletResponse.sendRedirect("/");
            return Response.status(302).entity(response).build();
        }
        else{
            response.setOk(true);
            servletResponse.sendRedirect("/api/test/"+testURL+"/doTest");
            return Response.status(200).entity(response).build();
        }

    }

    @GET
    @Path("/{testURL}/doTest")
    @Produces(MediaType.TEXT_HTML)
    public Response doTest(@PathParam("testURL") String testURL) throws IOException {

        Test test = ObjectifyService.ofy().load().type(Test.class).filter("testURL",servletRequest.getRequestURL().toString().replace("/doTest","")).first().now();
        String content="";
        InputStream st=servletContext.getResourceAsStream("/resource/testTemplate.html");
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(st));

        String string = new String();
        while( (string = buffReader.readLine() ) != null){
            content=content+string;
        }

        buffReader.close();
       content = content.replaceAll("@@config@@", Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsString(test).getBytes()));


        return Response.status(200).entity(content).build();
    }
}
