package au.com.team2media.typeadapter;

import au.com.team2media.util.GeoJSONType;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;

public class MongoDBDateTypeAdapter extends TypeAdapter<Date> {

    private static final String MONGO_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @Override
    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        if(date == null) {
            jsonWriter.nullValue();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        jsonWriter.value(dateFormat.format(date));

    }

    @Override
    public Date read(JsonReader jsonReader) throws IOException {
        if(jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        Date date = null;

//        while(jsonReader.hasNext()) {
            if(jsonReader.peek().equals(JsonToken.BEGIN_OBJECT)) {
                jsonReader.beginObject();

                while(!jsonReader.peek().equals(JsonToken.END_OBJECT)) {
                    JsonToken token = jsonReader.peek();
                    switch (token) {
                        case NAME:
                            if(jsonReader.nextName().equals("$date")) {
                                date = getDateFromString(jsonReader.nextString());
                            }
                            break;
                        default:
                            jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
            }
  //      }

        return date;
    }

    private Date getDateFromString(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(MONGO_UTC_FORMAT);
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
