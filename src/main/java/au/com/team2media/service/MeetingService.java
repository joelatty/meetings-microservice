package au.com.team2media.service;

import au.com.team2media.model.Coordinates;
import au.com.team2media.model.Location;
import au.com.team2media.model.Meeting;
import au.com.team2media.util.GeoJSONType;
import com.mongodb.*;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.time.format.TextStyle;
import java.util.Locale;

import static com.mongodb.util.JSON.serialize;

/**
 * Created by joe on 3/01/15.
 */
public class MeetingService {

    private MongoClient client;

    // returns a list of all meetings
    public String getAllMeetings() {
        DB database = getMongoClient().getDB("naorg");
        DBCollection collection = database.getCollection("meeting");
        return serialize(collection.find());
    }

    public int getMeetingsCount() {
        DB database = getMongoClient().getDB("naorg");
        DBCollection collection = database.getCollection("meeting");
        return collection.find().count();
    }

    public int getMeetingsCount(String suburb) {
        DB database = getMongoClient().getDB("naorg");
        DBCollection collection = database.getCollection("meeting");
        DBObject dbobject = new QueryBuilder()
                .start()
                .put("suburb")
                .is(suburb)
                .get();
        return collection.find(dbobject).count();
    }


    // returns a single meeting by id
    public Meeting getMeeting(String id) {
        return new Meeting();
    }

    public String getMeetingsBySuburb(String suburb) {
        DB database = getMongoClient().getDB("naorg");
        DBCollection collection = database.getCollection("meeting");
        BasicDBObject query = new BasicDBObject("suburb", suburb);
        return serialize(collection.find(query));
    }


    // creates a new meeting
    public Object createMeeting(Meeting meeting) {

        BasicDBObject meetingDBObject = getMeetingDBObject(meeting);
        Location location = meeting.getLocation();
        if (location != null) {
            BasicDBList coordinates = getCoordinates(location);
            meetingDBObject.append("location", new BasicDBObject("type", GeoJSONType.POINT.getDisplayValue())
                    .append("coordinates", coordinates));
        }

        DB database = getMongoClient().getDB("naorg");
        DBCollection collection = database.getCollection("meeting");

        WriteResult writeResult = collection.insert(meetingDBObject);

        return meetingDBObject.get("_id");
    }

    private BasicDBObject getMeetingDBObject(Meeting meeting) {
        return new BasicDBObject("name", meeting.getName())
                .append("suburb", meeting.getSuburb())
                .append("dateOfBirth", meeting.getDateOfBirth())
                .append("startTime", meeting.getStartTime())
                .append("endTime", meeting.getEndTime())
                .append("dayOfWeek", meeting.getDayOfWeek() == null ? null : meeting.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .append("type", meeting.getType());
    }

    private BasicDBList getCoordinates(Location location) {
        Coordinates coordinates = location.getCoordinates();
        BasicDBList coordinatesList = new BasicDBList();
        if (coordinates != null) {
            coordinatesList.add(coordinates.getLatitude());
            coordinatesList.add(coordinates.getLongitude());
        }
        return coordinatesList;
    }


    // updates an existing meeting
    public Meeting updateMeeting(String id, String name, String suburb) {
        return new Meeting();
    }


    private MongoClient getMongoClient() {
        if (client != null) return client;
        try {
            client = new MongoClient();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getMessage());
        }

        return client;
    }

    private void closeConnection() {
        client.close();
    }
}
