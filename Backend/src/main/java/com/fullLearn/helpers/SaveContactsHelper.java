package com.fullLearn.helpers;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fullLearn.beans.Contacts;

public class SaveContactsHelper
{
public boolean saveContacts(ArrayList<Contacts> contacts) throws IOException
{
	ObjectMapper obj = new ObjectMapper();
	boolean status = false;
	Contacts cc = new Contacts();

	List<Contacts> list = ofy().load().type(Contacts.class).filter("status !=","ACTIVE").list();
    if(!list.isEmpty()) {
		System.out.println("Checking");
		for (int j = 0; j < list.size(); j++) {
			String userId = list.get(j).getId();
			ofy().delete().type(Contacts.class).id(userId);
		}
	}
	else
	{
		System.out.println("Success");
	}

	for (int i=0;i<contacts.size();i++)
	{
	Contacts users = obj.readValue(obj.writeValueAsString(contacts.get(i)),new TypeReference<Contacts>(){});
	if(users.getStatus().equals("ACTIVE")) {
		status = ofy().save().entity(users).now() != null;
	}
	}
	return status;
}
}