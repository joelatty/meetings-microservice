package au.com.team2media.typeadapter;

import au.com.team2media.model.Coordinates;
import au.com.team2media.model.Location;
import au.com.team2media.util.GeoJSONType;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

public class LocationTypeAdapter  extends TypeAdapter<Location> {

    @Override
    public void write(JsonWriter jsonWriter, Location location) throws IOException {
        if(location == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.beginObject();
        jsonWriter.name("type").value(location.getType().getDisplayValue());
        jsonWriter.name("coordinates");
        jsonWriter.beginArray().value(location.getCoordinates().getLatitude()).value(location.getCoordinates().getLongitude());
        jsonWriter.endArray();
        jsonWriter.endObject();

    }

    @Override
    public Location read(JsonReader jsonReader) throws IOException {
        Location location = new Location();

        // For now only Point types being set
        // location.setType(GeoJSONType.POINT);

        Coordinates coordinates = new Coordinates();

        jsonReader.beginObject();

        String name = "";

        while(jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            switch (token) {
                case NAME:
                    if(jsonReader.nextName().equals("type")) {
                        location.setType(GeoJSONType.fromDisplayValue(jsonReader.nextString()));
                    }
                    break;
                case STRING:
                    jsonReader.nextString();
                    break;
                case BEGIN_ARRAY:
                    jsonReader.beginArray();
                    coordinates.setLatitude(jsonReader.nextString());
                    coordinates.setLongitude(jsonReader.nextString());
                    jsonReader.endArray();
                    break;
                default:
                    jsonReader.skipValue();
            }
        }

        jsonReader.endObject();

        location.setCoordinates(coordinates);

        return location;
    }

}
