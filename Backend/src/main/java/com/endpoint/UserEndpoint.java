package com.endpoint;

import com.daoImpl.UserDaoImpl;
import com.entities.User;
import com.googlecode.objectify.ObjectifyService;
import com.response.ApiResponse;


import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Path("/user")
public class UserEndpoint extends AbstractBaseApiEndpoint {



}
