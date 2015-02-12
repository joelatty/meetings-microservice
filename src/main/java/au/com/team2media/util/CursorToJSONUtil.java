package au.com.team2media.util;

import com.google.gson.Gson;
import com.mongodb.util.JSON;
import spark.ResponseTransformer;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 6/01/15
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CursorToJSONUtil {

    public static String toJson(Object object) {
        return JSON.serialize(object);
    }

    public static ResponseTransformer cursorToJson() {
        return CursorToJSONUtil::toJson;
    }
}
