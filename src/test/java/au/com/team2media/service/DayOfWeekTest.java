package au.com.team2media.service;

import au.com.team2media.util.DayOfWeekTypeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 24/02/15
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class DayOfWeekTest {

    Logger log = LoggerFactory.getLogger(DayOfWeekTest.class);

    @Test
    public void testDayOfWeekToJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        Gson gson = gsonBuilder.create();
        DayOfWeek[] values = DayOfWeek.values();
        String json = gson.toJson(values);

        log.debug(json);
    }

    @Test
    public void testDayOfWeekToJsonArray() {


        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dayOfWeek", dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            jsonArray.add(jsonObject);
        }


        log.debug(String.valueOf(jsonArray));

        //log.debug(json);
    }
}
