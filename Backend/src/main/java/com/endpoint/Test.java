package com.endpoint;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Test extends HttpServlet {

    @GET
    public Response test() throws IOException {

        Map<String, Object> resp = new HashMap<>();
        resp.put("ok", true);
        resp.put("msg", "successfully tested");



        return Response.ok(resp).build();

    }

public static void main(String arg[]){
    System.out.println("hello world");
}

}
