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
import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

import static com.mongodb.util.JSON.serialize;

/**
 * Created by joe on 3/01/15.
 */
public class MeetingService {

    private static final Logger LOG = LoggerFactory.getLogger(MeetingService.class);
    private static final String SUBURB = "suburb";

    private MongoClient client;
    private String databaseName = "team2media";
    private String collectionName = "meeting";


    public String getMeetings(@Header("suburb") String suburb) {
        DBCollection collection = getMeetingCollection();
        DBCursor cursor = null;
        try {
            if (StringUtil.isBlank(suburb)) {
                cursor = collection.find();
            } else {
                cursor = collection.find(getDbObject(SUBURB, suburb));
            }

            return serialize(cursor);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
     }


    public int getMeetingsCount(@Header("suburb") String suburb) {
        DBCollection collection = getMeetingCollection();
        if(StringUtils.isEmpty(suburb)) {
            return collection.find().count();
        }
        return collection.find(getDbObject(SUBURB, suburb)).count();
    }

    private DBCollection getMeetingCollection() {
        DB database = getMongoClient().getDB(databaseName);
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

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationTypeAdapter());
        gsonBuilder.setDateFormat("dd/MM/yyyy").create();
        Gson gson = gsonBuilder.create();
        Meeting meeting = gson.fromJson(body, Meeting.class);

        BasicDBObject meetingDBObject = getMeetingDBObject(meeting);
        Location location = meeting.getLocation();
        if (location != null) {
            BasicDBList coordinates = getCoordinates(location);
            meetingDBObject.append("location", new BasicDBObject("type", GeoJSONType.POINT.getDisplayValue())
                    .append("coordinates", coordinates));
        }

        DBCollection collection = getMeetingCollection();

        WriteResult writeResult = collection.insert(meetingDBObject);

        return serialize (meetingDBObject.get("_id"));
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
            MongoCredential credential = MongoCredential.createMongoCRCredential("tester", "team2media", "drmf5ltd".toCharArray());
            ServerAddress address = new ServerAddress("ds045988.mongolab.com", 45988);
            client = new MongoClient(address, Arrays.asList(credential));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getMessage());
        }

        return client;
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
}
