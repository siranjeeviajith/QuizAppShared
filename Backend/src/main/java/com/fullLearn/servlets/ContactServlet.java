package com.fullLearn.servlets;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import com.fullLearn.beans.*;
import com.fullLearn.services.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

			resp.setContentType("application/json");
		/*PrintWriter out = resp.getWriter();*/

			ContactServices fc = new ContactServices();

			// Access Token
			String accessToken = fc.getAccessToken();


			// Getting LastMOdified
			Long lastModified = fc.getLastModifiedContacts();


			boolean contacts;
			String cursorStr = null;
			int limit = 30;
			if (lastModified != null) {

				contacts = fc.syncContacts(lastModified, accessToken);

				if (contacts == true) {
					fc.saveAllContacts(accessToken, limit, cursorStr);
				}

			} else {
				fc.saveAllContacts(accessToken, limit, cursorStr);
			}

	}
}
