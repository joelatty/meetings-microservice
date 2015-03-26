package au.com.team2media.camel.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserTimeLinePollingTest extends CamelTwitterConsumerTestSupport {

    @Override
    protected String getUri() {
        return "twitter://timeline/user?type=polling&user=joelatty&";
    }

    @Override
    protected Logger getLogger() {
        return LoggerFactory.getLogger(UserTimeLinePollingTest.class);
    }
}
