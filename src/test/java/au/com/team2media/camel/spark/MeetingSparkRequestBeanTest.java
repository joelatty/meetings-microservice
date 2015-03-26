package au.com.team2media.camel.spark;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import spark.Request;

public class MeetingSparkRequestBeanTest extends BaseSparkTest {

    @Test
    public void testSparkGet() throws Exception {
        getMockEndpoint("mock:foo").expectedMessageCount(1);

        String out = template.requestBody("http://localhost:" + getPort() + "/hello/camel/to/world", null, String.class);
        assertEquals("Bye big world from camel", out);

        assertMockEndpointsSatisfied();


    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("spark-rest:get:/hello/*/to/*")
                        .to("mock:foo")
                        .bean(MeetingSparkRequestBeanTest.class, "doSomething");
            }
        };
    }

    public String doSomething(Request request) {
        return "Bye big " + request.splat()[1] + " from " + request.splat()[0];
    }

}

