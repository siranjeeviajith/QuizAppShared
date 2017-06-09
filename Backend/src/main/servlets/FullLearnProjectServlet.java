package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import helpers.*;
import javax.servlet.http.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import controller.FullLearnController;



@SuppressWarnings("serial")
public class FullLearnProjectServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		PrintWriter out=resp.getWriter();
		
	
		
		///////    Get UserDetails by query
		
		
		
		
		//////    NEED TO PASS EMAIL ,STARTIME AND ENDTIME To getUserActivities Method
		
		String userActivitiesDetail=HTTP.getUserActivities("amandeep.santokh@conversionsupport.com");
	String status=FullLearnController.create(userActivitiesDetail,"userid -jdfhsjfahskdfd","amandeep.santokh@conversionsupport.com",resp);
	
	out.println(status);
	
		
	}
}
