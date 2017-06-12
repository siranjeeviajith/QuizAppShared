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


		// Getting LastMOdified
		Long lastModified = fc.getLastModifiedContacts();
		ArrayList<Contacts> contacts = null;
		if(lastModified != null)
		{
			int limit = 30;
			contacts = fc.syncContacts(lastModified,accessToken);

			if(contacts == null)
			{
				contacts = fc.saveAllContacts(accessToken, limit);
			}

		}
		else
		{
			out.println("null");
		}


		boolean status = fc.saveContacts(contacts);
		out.println(status);



	}
}
