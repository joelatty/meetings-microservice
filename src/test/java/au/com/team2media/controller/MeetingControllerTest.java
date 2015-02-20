package au.com.team2media.controller;

import au.com.team2media.Meetings;
import au.com.team2media.builder.MeetingBuilder;
import au.com.team2media.model.Meeting;
import au.com.team2media.util.DayOfWeekTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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

    @Test
    public void getDaysOfWeek() {
        TestResponse res = request("GET", "/daysOfTheWeek");
        assertNotNull(res);
    }

    @Test
    public void testCreateMeeting() throws IOException {
        Meeting meeting = getMeeting();

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("name", meeting.getName()));
        urlParameters.add(new BasicNameValuePair("suburb", meeting.getSuburb()));
        urlParameters.add(new BasicNameValuePair("type", meeting.getType()));
        urlParameters.add(new BasicNameValuePair("startTime", meeting.getStartTime()));
        urlParameters.add(new BasicNameValuePair("endTime", meeting.getEndTime()));
        urlParameters.add(new BasicNameValuePair("dayOfWeek", meeting.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)));

        String url = "http://localhost:4567/meetings";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", "Mozilla/5.0");
        post.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Accept-Language", "en-US,en;q=0.5");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Referer", "https://accounts.google.com/ServiceLoginAuth");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        System.out.println("\nSending 'POST' request to URL : " + url);
        BufferedReader rq = new BufferedReader(new InputStreamReader(post.getEntity().getContent()));
        String rqLine = "";
        while ((rqLine = rq.readLine()) != null) {
            System.out.println(rqLine);
        }

        HttpResponse response = client.execute(post);

        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
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
