package com.endpoint;

import com.daoImpl.QuestionDaoImpl;
import com.entities.Question;
import com.response.ApiResponse;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/question")
public class QuestionEndpoint extends AbstractBaseApiEndpoint {
    static QuestionDaoImpl questionOption;
    static {
        questionOption=new QuestionDaoImpl();
    }
    @GET
    public Response getAllQuestion(){
        ApiResponse response=new ApiResponse();
        Question question = new Question();
        HttpSession session = servletRequest.getSession(false);
        if(session==null){
            response.setError("No session exist");
            return Response.status(401).entity(response).build();
        }else{

            response = questionOption.getAllQuestion();
            return  Response.status(200).entity(response).build();
        }

    }

    @GET
    @Path("/{tag}")
    public Response getQuestionByTag(@PathParam("tag") String tag){
        ApiResponse response = new ApiResponse();
        if(servletRequest.getSession(false)!=null){
            response=questionOption.getQuestionByTag(tag);
            return Response.status(200).entity(response).build();
        }else{
            response.setError("no session exist");
            return Response.status(401).entity(response).build();
        }
    }


    @POST
    @Path("/addQuestion")
    public Response addQuestion(Question question){
        ApiResponse response = new ApiResponse();
        if(servletRequest.getSession(false)!=null){
            response=questionOption.addAQuestion(question);
            return Response.status(200).entity(response).build();
        }else{
            response.setError("no session exist");
            return Response.status(401).entity(response).build();
        }
    }





}
