package com.fullLearn.servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.*;
import com.fullLearn.services.*;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/HTML");
		PrintWriter out = resp.getWriter();

		ContactServices fc = new ContactServices();

		// Access Token
		String accessToken = fc.getAccessToken();

		// LastMOdified check
//			Long dbCont = fc.getLastModifiedContacts();
//			out.println(dbCont);
		// List Of Contacts to save in DB
		ArrayList<String> contacts= fc.getURLContacts(accessToken);

		String status = fc.saveContacts(contacts);
		out.println(status);





	}
}
