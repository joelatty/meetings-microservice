package au.com.team2media.service;

import au.com.team2media.model.Meeting;
import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by joe on 3/01/15.
 */
public class MeetingService {

    private MongoClient client;

    public MeetingService() {
        init();
    }

    // returns a list of all meetings
    public DBCursor getAllMeetings() {
        DB database = client.getDB("naorg");
        DBCollection collection = database.getCollection("meeting");
        return collection.find();
    }


    // returns a single meeting by id
    public Meeting getMeeting(String id) {
        return new Meeting();
    }

    public DBCursor getMeetingsBySuburb(String suburb) {
        DB database = client.getDB("naorg");
        DBCollection collection = database.getCollection("meeting");
        BasicDBObject query = new BasicDBObject("suburb", suburb);
        return collection.find(query);
    }

    // creates a new meeting
    public Meeting createMeeting(String name, String email) {
        return new Meeting();
    }

    // updates an existing meeting
    public Meeting updateMeeting(String id, String name, String suburb) {
        return new Meeting();
    }


    private void init() {
        try {
            client = new MongoClient();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
