package com.fullLearn.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import com.fullLearn.helpers.HTTP;
import javax.servlet.http.*;



import com.fullLearn.service.FullLearnService;



@SuppressWarnings("serial")
public class FullLearnProjectServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		PrintWriter out=resp.getWriter();









		
		
		


		boolean status=FullLearnService.fetchUserDetails();

		//// this is just for displaying
			if(status)
			{
				out.println("all details saved successfully");
			}

		//String userActivitiesDetail= HTTP.getUserActivities("amandeep.santokh@conversionsupport.com",1493577000,1494009000);
	//String status= FullLearnService.create(userActivitiesDetail,"userid -jdfhsjfahskdfd","amandeep.santokh@conversionsupport.com");
	
	/*out.println(status);
	*/
		
	}
}
