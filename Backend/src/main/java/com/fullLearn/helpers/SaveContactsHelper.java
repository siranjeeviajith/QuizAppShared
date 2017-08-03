package com.fullLearn.helpers;

import com.fullLearn.beans.Contacts;
import com.fullLearn.beans.LearningStatsAverage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SaveContactsHelper {

    private final static Logger logger = Logger.getLogger(SaveContactsHelper.class.getName());

    public void saveContacts(ArrayList<Contacts> contacts) throws IOException {


        List<Contacts> saveContactList = new ArrayList<>();
        List<String> deleteContactsList = new ArrayList<>();
        Iterator contactsIterator = contacts.iterator();
        while (contactsIterator.hasNext()) {
            Contacts contact = (Contacts) contactsIterator.next();
            if (contact.getStatus().equals("ACTIVE")) {


                saveContactList.add(contact);
            } else {
                deleteContactsList.add(contact.getId());
            }
        }
        if (!saveContactList.isEmpty()) {
            logger.info("saving contacts size : " + saveContactList.size());
            ofy().save().entities(saveContactList).now();
        }
        if (!deleteContactsList.isEmpty()) {
            logger.info("deleting contacts size: " + deleteContactsList.size());
            //System.out.println("deleting contacts size: "+deleteContactsList.size());
            ofy().delete().type(Contacts.class).ids(deleteContactsList).now();
            ofy().delete().type(LearningStatsAverage.class).ids(deleteContactsList).now();
        }
    }
}