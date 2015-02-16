package au.com.team2media.util;

import com.mongodb.util.JSON;
import spark.ResponseTransformer;

public class CursorToJSONUtil {

    public static String toJson(Object object) {
        return JSON.serialize(object);
    }

    public static ResponseTransformer cursorToJson() {
        return CursorToJSONUtil::toJson;
    }
}
