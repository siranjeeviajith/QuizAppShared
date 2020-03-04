package com.endpoint;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/contact")
public class ContactEndpoint extends AbstractBaseApiEndpoint {

    @GET
    public Response getContact(){

        Map<String, Object> resp = new HashMap<>();
        resp.put("ok", true);
        resp.put("msg", "successfully tested");
        return Response.ok(resp).build();
    }
}
