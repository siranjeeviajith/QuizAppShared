package com.fullLearn.helpers;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStatsAverage;

public class SaveContactsHelper {
    public boolean saveContacts(ArrayList<Contacts> contacts) throws IOException {
        ObjectMapper obj = new ObjectMapper();
        boolean status = false;
        Contacts cc = new Contacts();
        List<Contacts> saveContactList=new ArrayList();
        List<String> deleteContactsList=new ArrayList<>();
        Iterator contactsIterator =contacts.iterator();
        while(contactsIterator.hasNext())
        {
            Contacts contact= (Contacts) contactsIterator.next();
            if(contact.getStatus().equals("ACTIVE"))
            {
                System.out.println("active");

                saveContactList.add(contact);
            }else{
                deleteContactsList.add(contact.getId());
            }


        }
if(saveContactList.isEmpty() || saveContactList==null)
{
    System.out.println("save");
    ofy().save().entity(saveContactList).now();
}
if(deleteContactsList.isEmpty() || deleteContactsList==null)
{
    System.out.println("delete");
    ofy().delete().type(Contacts.class).ids(deleteContactsList).now();
    ofy().delete().type(LearningStatsAverage.class).ids(deleteContactsList).now();
}
/*
        // To check the existing DB for status
        List<Contacts> list = ofy().load().type(Contacts.class).filter("status !=", "ACTIVE").list();
        if (!list.isEmpty()) {
            System.out.println(list);
            for (int j = 0; j < list.size(); j++) {
                String userId = list.get(j).getId();
                ofy().delete().type(Contacts.class).id(userId);
            }
        }

        // To save the data
        for (int i = 0; i < contacts.size(); i++) {
            Contacts users = obj.readValue(obj.writeValueAsString(contacts.get(i)), new TypeReference<Contacts>() {
            });
            if (users.getStatus().equals("ACTIVE")) {
                // Debugging purpose
                //if(users.getLogin().equals("ramesh.lingappa@a-cti.com") || users.getLogin().equals("shaikanjavali.mastan@a-cti.com") || users.getLogin().equals("naresh.talluri@a-cti.com"))
                status = ofy().save().entity(users).now() != null;
            }
        }*/
        return status;
    }
}