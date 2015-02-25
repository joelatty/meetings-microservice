package au.com.team2media.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DayOfWeekUtil {

   private static final List<String> daysOfTheWeek = Lists.newArrayList();
   private static final JsonArray jsonArray = new JsonArray();

   public DayOfWeekUtil() {
       init();
   }

    private void init() {

        for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
            daysOfTheWeek.add(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH));

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dayOfWeek", dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            jsonArray.add(jsonObject);
        }
    }

    public List<String> getDaysOfTheWeekAsStrings() {
        return daysOfTheWeek;
    }

    public JsonArray getDaysOfTheWeek() {
        return jsonArray;
    }

    public DayOfWeek getDayOfWeek(String dayOfWeek) {

        if (StringUtils.isNotBlank(dayOfWeek) && Iterables.contains(daysOfTheWeek, dayOfWeek)) {
            return DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        }

        return null;
    }
}
