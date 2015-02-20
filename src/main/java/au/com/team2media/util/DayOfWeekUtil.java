package au.com.team2media.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DayOfWeekUtil {

   private static final List<String> daysOfTheWeek = Lists.newArrayList();

   public DayOfWeekUtil() {
       initDaysOfWeek();
   }

    private void initDaysOfWeek() {
        for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
            daysOfTheWeek.add(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
    }

    public String getDaysOfTheWeek() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        Gson gson = gsonBuilder.create();
        return gson.toJson(DayOfWeek.values());
    }

    public List<String> getDaysOfTheWeekAsStrings() {
        return daysOfTheWeek;
    }

    public DayOfWeek getDayOfWeek(String dayOfWeek) {

        if (StringUtils.isNotBlank(dayOfWeek) && Iterables.contains(daysOfTheWeek, dayOfWeek)) {
            return DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        }

        return null;
    }
}
