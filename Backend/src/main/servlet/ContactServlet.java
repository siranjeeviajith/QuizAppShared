package servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import controller.*;
import javax.servlet.http.*;

import beans.Contacts;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			
			resp.setContentType("text/HTML");
			PrintWriter out = resp.getWriter();
			ContactsController fc = new ContactsController();
			String accessToken = fc.getAccessToken();
			String contacts= fc.getContacts(accessToken);
			Contacts map = fc.saveContacts(contacts);
			out.println(map);
			
		}
}
