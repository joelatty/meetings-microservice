package au.com.team2media.service;

import au.com.team2media.model.Meeting;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Created by joe on 3/01/15.
 */
public class MeetingService {

        private MongoClient client;

        // returns a list of all meetings
        public DBCursor getAllMeetings() {
                DB database = getMongoClient().getDB("naorg");
                DBCollection collection = database.getCollection("meeting");
                return collection.find();
        }


        // returns a single meeting by id
        public Meeting getMeeting(String id) { return new Meeting(); }

        public DBCursor getMeetingsBySuburb(String suburb) {
                DB database = getMongoClient().getDB("naorg");
                DBCollection collection = database.getCollection("meeting");
                BasicDBObject query = new BasicDBObject("suburb", suburb);
                return collection.find(query);
        }

        // creates a new meeting
        public Object createMeeting(Meeting meeting)
        {
            DB database = getMongoClient().getDB("naorg");
            DBCollection collection = database.getCollection("meeting");

            BasicDBObject meetingDBObject = new BasicDBObject();
            meetingDBObject.put("name", meeting.getName());
            meetingDBObject.put("suburb", meeting.getSuburb());
            meetingDBObject.put("startTime", meeting.getStartTime());
            meetingDBObject.put("endTime", meeting.getEndTime());
            meetingDBObject.put("dayOfWeek", meeting.getDayOfWeek() == null ? null : meeting.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            meetingDBObject.put("type", meeting.getType());
            meetingDBObject.put("latitude", meeting.getLatitude());
            meetingDBObject.put("longitude", meeting.getLongitude());

            WriteResult writeResult = collection.insert(meetingDBObject);

            return meetingDBObject.get("_id");
        }


        // updates an existing meeting
        public Meeting updateMeeting(String id, String name, String suburb) { return new Meeting(); }


        private MongoClient getMongoClient() {
                if(client != null) return client;
                try {
                        client = new MongoClient();
                } catch(UnknownHostException e) {
                        throw new RuntimeException(e.getMessage());
                }

                return client;
        }
}
