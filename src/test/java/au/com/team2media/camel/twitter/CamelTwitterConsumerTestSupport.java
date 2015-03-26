package au.com.team2media.camel.twitter;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.slf4j.Logger;

public abstract class CamelTwitterConsumerTestSupport extends CamelTwitterTestSupport {
    protected abstract String getUri();

    protected abstract Logger getLogger();

    @Test
    public void testDailyTrend() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);
        mock.assertIsSatisfied();
        List<Exchange> tweets = mock.getExchanges();
        for (Exchange e : tweets) {
            getLogger().info("Tweet: " + e.getIn().getBody(String.class));
        }

    }

    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from(getUri() + getUriTokens())
                        .transform(body().convertToString()).to("mock:result");
            }
        };
    }

}
