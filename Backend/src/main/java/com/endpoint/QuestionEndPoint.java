package com.endpoint;

import com.daoImpl.QuestionDaoImpl;
import com.entities.Question;
import com.response.ApiResponse;

import javax.annotation.PostConstruct;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/question")
public class QuestionEndPoint extends AbstractBaseApiEndpoint {
    static QuestionDaoImpl questionOption;


    static {
        questionOption=new QuestionDaoImpl();
    }
    @POST
    @Path("/addQuestions")
    public Response addAllQuestion(List<Question> questionList){
        ApiResponse response = new ApiResponse();
        if(servletRequest.getSession(false)!=null){
            response=questionOption.addAllQuestion(questionList);
            return Response.status(200).entity(response).build();
        }else{
            response.setError("no session exist");
            return Response.status(401).entity(response).build();
        }
    }


}
