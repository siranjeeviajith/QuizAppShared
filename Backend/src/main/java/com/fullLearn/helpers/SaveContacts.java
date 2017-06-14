package com.fullLearn.helpers;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fullLearn.beans.Contacts;

public class SaveContacts
{
public boolean saveContacts(ArrayList<Contacts> contacts) throws IOException
{
	ObjectMapper obj = new ObjectMapper();
	boolean status = false;
	for (int i=0;i<contacts.size();i++)
	{
	Contacts users = obj.readValue(obj.writeValueAsString(contacts.get(i)),new TypeReference<Contacts>(){});
	status = ofy().save().entity(users).now() != null;
	}
	return status;
}
}