package com.taskqueuesample;

import com.endpoint.AbstractBaseApiEndpoint;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.response.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Path("/api/tasks")
public class TaskQueueSample extends AbstractBaseApiEndpoint {

    @Path("/push")
    @GET
    public void runTask(){
        Queue queue = QueueFactory.getDefaultQueue();
        String jsonString ="{\n" +
                "\t\t\"question\":{\n" +
                "\t\t\n" +
                "\t\t\t\t\"tag\":\"general\",\n" +
                "\t\t\t\t\"description\":\"What is your name?\",\n" +
                "\t\t\t\t\"option\":{\"A\":\"ajith\",\"B\":\"my name\",\"C\":\"your name\",\"D\":\"krithika\"},\n" +
                "\t\t\t\t\"correctAns\":\"B\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\n" +
                "\t}";
        byte[] payloadInByte = jsonString.getBytes();
        queue.add(TaskOptions.Builder.withTaskName("addQuestion")
                                     .url("/api/question/addQuestion")
                                     .etaMillis(System.currentTimeMillis()+120000)

                                     .payload(payloadInByte,"application/json"));
        
        Queue queue1 = QueueFactory.getQueue("pull-queue");
        payloadInByte = "Hello World!!".getBytes();
        queue1.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(payloadInByte).tag("hello").taskName("hello_world"));

    }

    @Path("/pull")
    @GET
    public Response pullTask(){

        ApiResponse response = new ApiResponse();
        Queue queue = QueueFactory.getQueue("pull-queue");
        List<TaskHandle> tasks = queue.leaseTasksByTag(10,TimeUnit.SECONDS,1,"hello");

            response.addData("payload", new String(tasks.get(0).getPayload()));
            System.out.println("DEBUG PULL QUEUE PAYLOAD: " + new String(tasks.get(0).getPayload()));


        queue.deleteTask(tasks);
        response.setOk(true);
        return Response.status(200).entity(response).build();
    }



}
