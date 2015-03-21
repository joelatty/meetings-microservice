package au.com.team2media.camel.spark;

import au.com.team2media.model.Meeting;
import au.com.team2media.service.MeetingService;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 19/03/15
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveMeetingTest extends BaseSparkTest {

    @Test
    public void testMeetingInOut() throws Exception {

        // getMockEndpoint("mock:delete").expectedMessageCount(1);

        String id = "550a4f9082e34dacdb288eb7";

        String out = template.requestBody("http://localhost:" + getPort() + "/meetings", id, String.class);

        assertNotNull(out);
    }



    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        MongoURI mongoURI = new MongoURI("mongodb://localhost:27017/team2media");
        Mongo mongo = new Mongo(mongoURI);
        MeetingService service = new MeetingService();
        service.setDatabaseName("team2media");
        service.setCollectionName("meeting");
        service.setMongoConnection(mongo);

        ((JndiRegistry)((PropertyPlaceholderDelegateRegistry)context.getRegistry()).getRegistry()).bind("meetingService", service);

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // use the rest DSL to define the rest services
                from("spark-rest:post:meetings").to("log:Out").convertBodyTo(ObjectId.class).to("bean:meetingService?method=removeMeeting");
            }
        };
    }
}
