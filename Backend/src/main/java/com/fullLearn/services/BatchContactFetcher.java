package com.fullLearn.services;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;

import com.fullLearn.beans.Contacts;
import com.googlecode.objectify.cmd.Query;

import static com.googlecode.objectify.ObjectifyService.ofy;

interface BatchContactWork {
    void run(Contacts contacts) throws Exception;

    void nextCursor(String cursor);
}

/**
 * FullLearn
 * Created by Ramesh on 8/14/17.
 */
public class BatchContactFetcher {

    public static void fetchAllContacts(String cursor, int limit, BatchContactWork work) throws Exception {

        if (work == null)
            throw new IllegalArgumentException("invalid work object");

        do {

            Query<Contacts> query = ofy().load().type(Contacts.class).limit(limit);
            if (cursor != null)
                query = query.startAt(Cursor.fromWebSafeString(cursor));

            QueryResultIterator<Contacts> iterator = query.iterator();

            //no more element
            if (iterator == null || !iterator.hasNext()) {
                return;
            }

            while (iterator.hasNext()) {
                work.run(iterator.next());
            }

            cursor = iterator.getCursor() != null ? iterator.getCursor().toWebSafeString() : null;

            work.nextCursor(cursor);

        } while (cursor != null);
    }
}
