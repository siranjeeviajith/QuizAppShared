package com.fullLearn.servlets.taskQueues;

import com.fullLearn.services.ContactServices;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 6/26/2017.
 */
public class ContactSyncTaskQueueServlet extends HttpServlet {

    ContactServices fc = new ContactServices();
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        // Getting LastMOdified
        Long lastModified = fc.getLastModifiedContacts();

        String accessToken=req.getParameter("accesstoken");
        int limit=Integer.parseInt(req.getParameter("limit"));
        String cursorStr=null;
        ContactServices contactservice=new ContactServices();

        contactservice.saveAllContacts(lastModified,accessToken,limit,cursorStr);


    }
}
