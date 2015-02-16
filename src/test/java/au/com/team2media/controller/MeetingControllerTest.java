package au.com.team2media.controller;

import au.com.team2media.Meetings;
import au.com.team2media.model.Meeting;
import au.com.team2media.service.MeetingService;
import au.com.team2media.util.DayOfWeekTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 16/02/15
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingControllerTest {

    private MeetingController controller;

    @BeforeClass
    public static void setUp() throws Exception {
        Meetings.main(null);
    }

    @AfterClass
    public static void breakDown() {
        Spark.stop();
    }

    @Test
    public void getMeetingsList() {
        TestResponse res = request("GET", "/meetings/Newtown");
        Collection<Meeting> meetings = res.json();
        assertNotNull(meetings);
        // assertEquals("Newtown", json.get("suburb"));
        //assertEquals("Bedford Steps", json.get("name"));
    }

    private TestResponse request(String method, String path) {
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public Collection<Meeting> json() {
            Type collectionType = new TypeToken<Collection<Meeting>>(){}.getType();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
            Gson gson = gsonBuilder.create();
            Collection<Meeting> meetings = gson.fromJson(body, collectionType);
            return meetings;
        }
    }
}
