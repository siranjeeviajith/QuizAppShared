package com.endpoint;

import com.daoImpl.QuestionDaoImpl;
import com.entities.Question;
import com.enums.AccountType;
import com.filters.ApiKeyCheck;
import com.response.ApiResponse;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

@NoCache
@Path("/api/question")
@ApiKeyCheck
public class QuestionEndpoint extends AbstractBaseApiEndpoint {
    static QuestionDaoImpl questionOption;
    static {
        questionOption=new QuestionDaoImpl();

    }
//    @GET
//    public Response getAllQuestion(){
//        ApiResponse response=new ApiResponse();
//        Question question = new Question();
//        HttpSession session = servletRequest.getSession(false);
//        if(session==null){
//            response.setError("No session exist");
//            return Response.status(401).entity(response).build();
//        }else{
//
//            response = questionOption.getAllQuestion();
//            return  Response.status(200).entity(response).build();
//        }
//
//    }
    @POST
    @Path("/getQuestionByIds/")
    public Response getQuestionsByIds(List<String> questionIds){
        ApiResponse response = new ApiResponse();
        HttpSession session = servletRequest.getSession(false);
        if(session!=null){
            if(session.getAttribute("accountType")!=null && session.getAttribute("accountType").equals(AccountType.ADMIN)) {
                response = questionOption.getQuestionsByIds(questionIds);

                response.setOk(true);

                return Response.status(200).entity(response).build();
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
    @Path("/getQuestion/{tag}")
    public Response getQuestionByTag(@PathParam("tag") String tag){
        ApiResponse response = new ApiResponse();
        HttpSession session = servletRequest.getSession(false);
        if(servletRequest.getSession(false)!=null){
            if(session.getAttribute("accountType")!=null && session.getAttribute("accountType").equals(AccountType.ADMIN)) {
                response = questionOption.getQuestionByTag(tag);
                return Response.status(200).entity(response).build();
            }else{
                response.setError("User account is not permitted");
                return Response.status(401).entity(response).build();
            }
        }else{
            response.setError("no session exist");
            return Response.status(401).entity(response).build();
        }
    }


    @POST
    @Path("/addQuestion")
    public Response addQuestion(Question question){
        ApiResponse response = new ApiResponse();
        HttpSession session = servletRequest.getSession(false);

        if(servletRequest.getSession(false)!=null){
            if(session.getAttribute("accountType")!=null && session.getAttribute("accountType").equals(AccountType.ADMIN)) {

                response = questionOption.addAQuestion(question);
                return Response.status(200).entity(response).build();
            } else{
                response.setError("User account is not permitted");
                return Response.status(401).entity(response).build();
            }
        }else{
            response.setError("no session exist");
            return Response.status(401).entity(response).build();
        }
    }

}
