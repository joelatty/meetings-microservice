package au.com.team2media.typeconverter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.util.JSON;
import org.apache.camel.Converter;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 19/03/15
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Converter
public class ObjectIdConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectIdConverter.class);

    @Converter
    public static ObjectId convertToObjectId(String id)  {
        ObjectId objectId = null;
        try {
            objectId = new Gson().fromJson(id, ObjectId.class);
        } catch (JsonSyntaxException e) {
            objectId = new ObjectId(id);
        }

        return objectId;
    }
}
