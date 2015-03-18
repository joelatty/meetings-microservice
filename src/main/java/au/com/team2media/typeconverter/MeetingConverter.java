package au.com.team2media.typeconverter;

import au.com.team2media.model.Location;
import au.com.team2media.model.Meeting;
import au.com.team2media.typeadapter.DayOfWeekTypeAdapter;
import au.com.team2media.typeadapter.LocationTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Converter;

import java.time.DayOfWeek;

@Converter
public class MeetingConverter {
    @Converter
    public static Meeting convertToMeeting(String body) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationTypeAdapter());
        gsonBuilder.setDateFormat("dd/MM/yyyy").create();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(body, Meeting.class);
    }
}
