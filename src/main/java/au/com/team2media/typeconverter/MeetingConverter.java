package au.com.team2media.typeconverter;

import au.com.team2media.model.Location;
import au.com.team2media.model.Meeting;
import au.com.team2media.typeadapter.DayOfWeekTypeAdapter;
import au.com.team2media.typeadapter.LocationTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;

import java.time.DayOfWeek;

/**
 * Created by joelatty on 14/03/15.
 */

public class MeetingConverter extends TypeConverterSupport {

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationTypeAdapter());
        gsonBuilder.setDateFormat("dd/MM/yyyy").create();
        Gson gson = gsonBuilder.create();
        return (T) gson.fromJson(value.toString(), Meeting.class);

    }
}

