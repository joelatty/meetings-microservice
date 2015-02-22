package au.com.team2media.controller;

import au.com.team2media.builder.MeetingBuilder;
import au.com.team2media.model.Meeting;
import au.com.team2media.util.DayOfWeekTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.utils.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

public class MeetingControllerTest {

    private MeetingController controller;

    @BeforeClass
    public static void setUp() throws Exception {
//        Meetings.main(null);
    }

    @AfterClass
    public static void breakDown() {
//        Spark.stop();
    }

    @Test
    public void getMeetingsList() {
        TestResponse res = request("/meetings/Newtown");
        Collection<Meeting> meetings = res.json();
        assertNotNull(meetings);
        assertTrue(meetings.size() > 0);
        Meeting meeting = meetings.iterator().next();
        assertEquals("Newtown", meeting.getSuburb());
        assertEquals("Newtown Steps", meeting.getName());
    }

    @Test
    public void getDaysOfWeek() {
        TestResponse res = request("/daysOfTheWeek");
        assertNotNull(res);
    }

    @Test
    public void testCreateMeeting() {
        Meeting meeting = getMeeting();

        String url = "http://localhost:4567/meetings";
        org.apache.http.client.HttpClient client = new DefaultHttpClient();

        try {
            HttpPost request = new HttpPost(url);


            StringEntity params = new StringEntity("{\"name\":\"Paddington Steps\",\"suburb\":\"Paddington\", \"type\":\"General\"}");

            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);

            HttpResponse response = client.execute(request);

            String result = IOUtils.toString(response.getEntity().getContent());

            assertNotNull(result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
            // method.releaseConnection();
        }
    }

    private Meeting getMeeting() {

        String[] names = {"Cliff Hangers", "Glebe Speaker Meeting", "Bedford Steps", "Newtown Community Centre", "Annandale Boomerangs", "Lewisham Saturday Night"};
        String[] suburbs = {"Bondi North", "Glebe", "Newtown", "Annandale", "Paddington", "Camperdown", "Darlinghurst"};
        String[] types = {"General", "Speaker", "Topic", "Just for Today", "Reading", "Steps"};
        String[] startTimes = {"10:30AM", "7:00PM", "8:00PM", "12:00PM", "7:30PM", "12:30PM"};
        String[] endTimes = {"11:30AM", "8:30PM", "8:30PM", "1:00PM", "8:30PM", "2:00PM"};
        DayOfWeek[] daysOfWeek = DayOfWeek.values();

        int times = randInt(0,5);
        return new MeetingBuilder()
                    .setName(names[randInt(0,5)])
                    .setSuburb(suburbs[randInt(0,6)])
                    .setType(types[randInt(0,5)])
                    .setStartTime(startTimes[times])
                    .setEndTime(endTimes[times])
                    .setDayOfWeek(daysOfWeek[randInt(0,6)])
                    .build();
    }

    private int randInt(int min, int max) {
       Random rand = new Random();
       int randomNum = rand.nextInt((max - min) + 1) + min;
       return randomNum;
    }

    @Test
    public void getDaysOfWeekAsJson() {
        Type collectionType = new TypeToken<Collection<Meeting>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        Gson gson = gsonBuilder.create();
        assertNotNull(gson.toJson(DayOfWeek.values()));
    }

    private TestResponse request(String path) {

        String url = "http://localhost:4567";
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url + path);

        try {

//            // Provide custom retry handler is necessary
//            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//                    new DefaultHttpMethodRetryHandler(3, false));

            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            return new TestResponse(statusCode,  new String(responseBody));

        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        } finally {
            method.releaseConnection();
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
