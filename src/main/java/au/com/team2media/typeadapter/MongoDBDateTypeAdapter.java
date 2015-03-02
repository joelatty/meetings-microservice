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

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 2/03/15
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
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

        while(jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            switch (token) {
                case BEGIN_OBJECT:
                    jsonReader.beginObject();
                    String objectName = jsonReader.nextName();
                    Date date = null;
                    if(objectName.equals("$date")) {
                        date = getDateFromString(jsonReader.nextString());
                        jsonReader.endObject();
                        return date;
                    }
                    break;
                default:
                    jsonReader.skipValue();
            }
        }



        return null;
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
