package com.fullLearn.servlet;

import java.io.*;
import java.util.*;
import com.fullLearn.beans.*;
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

		// List Of Contacts to save in DB
		ArrayList<Contacts> contacts= fc.getURLContacts(accessToken);

		boolean status = fc.saveContacts(contacts);
		out.println(status);

		// LastMOdified check
		String dbCont = fc.getLastModifiedContacts();
		out.println(dbCont);



	}
}
