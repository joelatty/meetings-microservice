package au.com.team2media.camel.spark;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 13/03/15
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingSparkRestTest extends BaseSparkTest {

    @Test
    public void testSparkGet() throws Exception {
        getMockEndpoint("mock:meetings").expectedMessageCount(1);
        getMockEndpoint("mock:meetings").expectedHeaderReceived("suburb", "Newtown");

        String out = template.requestBody("http://localhost:" + getPort() + "/meetings/Newtown", null, String.class);
        assertEquals("Hello Newtown", out);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("spark-rest:get:/meetings/:suburb")
                        .to("mock:meetings")
                        .transform().simple("Hello ${header.suburb}");
            }
        };
    }
}
