package au.com.team2media.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by joe on 3/01/15.
 */
public class JsonUtil {

    public static String toJsonFromGson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJsonFromGson;
    }

}
