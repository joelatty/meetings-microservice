package au.com.team2media.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

import java.time.DayOfWeek;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 24/02/15
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DayOfWeekToJsonUtil {
    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static ResponseTransformer dayOfWeekToJson() {
        return DayOfWeekToJsonUtil::toJson;
    }

}
