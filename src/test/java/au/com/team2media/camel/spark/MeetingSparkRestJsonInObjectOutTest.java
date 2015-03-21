package au.com.team2media.camel.spark;

import au.com.team2media.model.Meeting;
import au.com.team2media.typeconverter.MeetingConverter;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 13/03/15
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingSparkRestJsonInObjectOutTest extends BaseSparkTest {

    private static String meetingJson = "{" +
            "    \"name\" : \"Living Alone\"," +
            "    \"suburb\" : \"Newtown\"," +
            "    \"dateOfBirth\" : \"01/01/1980\"," +
            "    \"startTime\" : null," +
            "    \"endTime\" : null," +
            "    \"dayOfWeek\" : \"Wednesday\"," +
            "    \"type\" : \"LGBTG\"," +
            "    \"location\" : {" +
            "        \"type\" : \"Point\"," +
            "        \"coordinates\" : [ " +
            "            -33.8972841, " +
            "            151.1758641" +
            "        ]" +
            "    }" +
            "}";


    @Test
    public void testMeetingInOut() throws Exception {
        String body = meetingJson;
        Meeting out = template.requestBody("http://localhost:" + getPort() + "/meetings", body, Meeting.class);

        assertNotNull(out);
        assertEquals("{\"type\":\"Speaker\",\"area\":\"Inner West\"}", out);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // add our own type converter manually that converts from String -> MyOrder using MyOrderTypeConverter
                context.getTypeConverterRegistry().addTypeConverter(Meeting.class, String.class, new MeetingConverter());

                restConfiguration().component("spark-rest");

                from("spark-rest:post:/meetings")
                        .convertBodyTo(Meeting.class);
            }
        };
    }
}
