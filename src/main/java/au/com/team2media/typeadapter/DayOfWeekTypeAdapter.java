package au.com.team2media.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

public class DayOfWeekTypeAdapter extends TypeAdapter<DayOfWeek> {


    @Override
    public void write(JsonWriter jsonWriter, DayOfWeek dayOfWeek) throws IOException {
        if(dayOfWeek == null) {
            jsonWriter.nullValue();
            return;
        }

//        jsonWriter.beginObject();
//        jsonWriter.name(dayOfWeek.toString()).value(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
//        jsonWriter.endObject();
        jsonWriter.value(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH));

    }

    @Override
    public DayOfWeek read(JsonReader jsonReader) throws IOException {
        if(jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return DayOfWeek.valueOf(jsonReader.nextString().toUpperCase());
    }
}
