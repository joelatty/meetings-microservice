package au.com.team2media.service;

import au.com.team2media.model.Coordinates;
import au.com.team2media.model.Location;
import au.com.team2media.model.Meeting;
import au.com.team2media.typeadapter.DayOfWeekTypeAdapter;
import au.com.team2media.typeadapter.LocationTypeAdapter;
import au.com.team2media.util.GeoJSONType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

import static com.mongodb.util.JSON.serialize;

/**
 * Created by joe on 3/01/15.
 */
public class MeetingService {

    private static final Logger LOG = LoggerFactory.getLogger(MeetingService.class);
    private static final String SUBURB = "suburb";

    private Mongo mongoConnection;
    private MongoClient client;
    private String databaseName;
    private String collectionName;
    private String clientURI;


    public String getMeetings(@Header("suburb") String suburb) throws UnsupportedEncodingException {
        DBCollection collection = getMeetingCollection();
        DBCursor cursor = null;
        try {
            if (StringUtils.isBlank(suburb)) {
                cursor = collection.find();
            } else {
                cursor = collection.find(getDbObject(SUBURB, decode(suburb)));
            }

            return serialize(cursor);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String decode(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
    }


    public int getMeetingsCount(@Header("suburb") String suburb) throws UnsupportedEncodingException {
        DBCollection collection = getMeetingCollection();
        if (StringUtils.isEmpty(suburb)) {
            return collection.find().count();
        }
        return collection.find(getDbObject(SUBURB, decode(suburb))).count();
    }

    private DBCollection getMeetingCollection() {
        DB database = mongoConnection.getDB(databaseName);
        return database.getCollection(collectionName);
    }

    private DBObject getDbObject(String key, String value) {
        return new QueryBuilder()
                .start()
                .put(key)
                .is(value)
                .get();
    }


    // returns a single meeting by id
    public Meeting getMeeting(String id) {
        return new Meeting();
    }


    // creates a new meeting
    public Object createMeeting(@Body() String body) {

        Meeting meeting = getMeetingFromBody(body);

        BasicDBObject meetingDBObject = getMeetingDBObject(meeting);
        Location location = meeting.getLocation();
        if (location != null) {
            BasicDBList coordinates = getCoordinates(location);
            meetingDBObject.append("location", new BasicDBObject("type", GeoJSONType.POINT.getDisplayValue())
                    .append("coordinates", coordinates));
        }

        DBCollection collection = getMeetingCollection();

        WriteResult writeResult = collection.insert(meetingDBObject);

        return serialize(meetingDBObject.get("_id"));
    }

    public Meeting getMeetingFromBody(@Body() String body) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationTypeAdapter());
        gsonBuilder.setDateFormat("dd/MM/yyyy").create();
        Gson gson = gsonBuilder.create();
        Meeting meeting = gson.fromJson(body, Meeting.class);

        return meeting;
    }

    public Object insertMeeting(Meeting meeting) {
        BasicDBObject meetingDBObject = getMeetingDBObject(meeting);
        Location location = meeting.getLocation();
        if (location != null) {
            BasicDBList coordinates = getCoordinates(location);
            meetingDBObject.append("location", new BasicDBObject("type", GeoJSONType.POINT.getDisplayValue())
                    .append("coordinates", coordinates));
        }

        DBCollection collection = getMeetingCollection();

        WriteResult writeResult = collection.insert(meetingDBObject);

        return serialize(meetingDBObject.get("_id"));
    }

    public WriteResult removeMeeting(String body) {
        ObjectId objectId = (ObjectId) JSON.parse(body);
        return removeMeeting(objectId);
    }

    public WriteResult removeMeeting(ObjectId objectId) {
        DBCollection collection = getMeetingCollection();
        BasicDBObject query = new BasicDBObject();
        query.put("_id", objectId);
        return collection.remove(query);
    }

    public void deleteMeeting(@Body() String id) {
        ObjectId objectId = new ObjectId(id);
        DBCollection collection = getMeetingCollection();
        BasicDBObject query = new BasicDBObject();
        query.put("_id", objectId);
        collection.remove(query);
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

    private void closeConnection() {
        client.close();
    }

    public void setDatabaseName(String databaseName) {
        LOG.info("Database: " + databaseName);
        this.databaseName = databaseName;
    }

    public void setCollectionName(String collectionName) {
        LOG.info("Collection: " + collectionName);
        this.collectionName = collectionName;
    }

    public void setMongoConnection(Mongo mongoConnection) {
        this.mongoConnection = mongoConnection;
    }

}
