package com.fullLearn.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import com.fullLearn.helpers.HTTP;
import javax.servlet.http.*;



import com.fullLearn.service.FullLearnService;



@SuppressWarnings("serial")
public class FullLearnProjectServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		PrintWriter out=resp.getWriter();
		
	
		
		///////    Get UserDetails by query
		
		
		
		
		//////    NEED TO PASS EMAIL ,STARTIME AND ENDTIME To getUserActivities Method
		
		String userActivitiesDetail= HTTP.getUserActivities("amandeep.santokh@conversionsupport.com");
	String status= FullLearnService.create(userActivitiesDetail,"userid -jdfhsjfahskdfd","amandeep.santokh@conversionsupport.com");
	
	out.println(status);
	
		
	}
}
