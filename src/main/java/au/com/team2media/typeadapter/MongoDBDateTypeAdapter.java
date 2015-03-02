package au.com.team2media.typeadapter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 2/03/15
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class MongoDBDateTypeAdapter extends TypeAdapter<Calendar> {

    @Override
    public void write(JsonWriter jsonWriter, Calendar calendar) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Calendar read(JsonReader jsonReader) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
