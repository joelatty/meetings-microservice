package au.com.team2media.camel.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * consumes tweets
 */
public class SearchDirectTest extends CamelTwitterConsumerTestSupport {

    @Override
    protected String getUri() {
        return "twitter://search?type=direct&keywords=java&";
    }

    @Override
    protected Logger getLogger() {
        return LoggerFactory.getLogger(SearchDirectTest.class);
    }
}
