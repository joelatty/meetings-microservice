package au.com.team2media.util;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DayOfWeekUtil {

    public static String getDaysOfTheWeek() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        Gson gson = gsonBuilder.create();
        return gson.toJson(DayOfWeek.values());
    }

}
