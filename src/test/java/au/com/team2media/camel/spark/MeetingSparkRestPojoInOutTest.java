package au.com.team2media.camel.spark;

import au.com.team2media.model.Meeting;
import au.com.team2media.model.MeetingBean;
import au.com.team2media.model.MeetingTest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 13/03/15
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingSparkRestPojoInOutTest extends BaseSparkTest {
    @Test
    public void testMeetingInOut() throws Exception {
        String body = "{\"name\": \"Newtown Steps\", \"suburb\" : \"Newtown\", \"startTime\": \"8:00PM\"}";
        String out = template.requestBody("http://localhost:" + getPort() + "/meetings/name", body, String.class);

        assertNotNull(out);
        assertEquals("{\"type\":\"Speaker\",\"area\":\"Inner West\"}", out);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // configure to use spark on localhost with the given port
                // and enable auto binding mode
                restConfiguration().component("spark-rest").host("localhost").port(getPort()).bindingMode(RestBindingMode.auto);

                // use the rest DSL to define the rest services
                rest("/meetings/")
                        .post("name").type(Meeting.class)
                        .outType(MeetingBean.class)
                        .route()
                        .bean(new MeetingTest(), "whatSuburb");
            }
        };
    }
}
