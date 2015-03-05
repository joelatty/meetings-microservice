package au.com.team2media.typeconverter;

import au.com.team2media.util.CursorToJSONUtil;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class DBCursorConverter {

    private static final Logger LOG = LoggerFactory.getLogger(DBCursorConverter.class);

    @Converter (allowNull = true)
    public static String toJson(DBCursor cursor, Exchange exchange){

        LOG.info("Inside the cursor transformer");

        if(cursor == null) {
            return null;
        }

        LOG.info("Created json string from cursor: {}", JSON.serialize(cursor));

      return JSON.serialize(cursor);

    }
}
