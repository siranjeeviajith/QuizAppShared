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
    public void saveContacts(ArrayList<Contacts> contacts) throws IOException {

        List<Contacts> saveContactList = new ArrayList<>();
        List<String> deleteContactsList = new ArrayList<>();
        Iterator contactsIterator = contacts.iterator();
        while (contactsIterator.hasNext()) {
            Contacts contact = (Contacts) contactsIterator.next();
            if (contact.getStatus().equals("ACTIVE")) {
                System.out.println("active");

                saveContactList.add(contact);
            } else {
                deleteContactsList.add(contact.getId());
            }


        }
        if (!saveContactList.isEmpty()) {
            System.out.println("save");
            ofy().save().entities(saveContactList).now();
        }
        if (!deleteContactsList.isEmpty()) {
            System.out.println("deleting contacts size: "+deleteContactsList.size());
            ofy().delete().type(Contacts.class).ids(deleteContactsList).now();
            ofy().delete().type(LearningStatsAverage.class).ids(deleteContactsList).now();
        }

    }
}