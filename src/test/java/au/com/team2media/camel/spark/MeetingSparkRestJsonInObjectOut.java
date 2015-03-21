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

public class MeetingSparkRestJsonInObjectOut extends BaseSparkTest {

    @Test
    public void testMeetingInOut() throws Exception {
        String body = "{ \"name\" : \"Orange Grove Markets\"," +
                " \"suburb\" : \"Lillyfield\"," +
                " \"dateOfBirth\" : \"01/01/1982\"," +
                " \"startTime\" : \"10:00AM\"," +
                " \"endTime\" : \"1:30PM\"," +
                " \"dayOfWeek\" : \"Saturday\"," +
                " \"type\" : \"Organic\"," +
                " \"location\" : {" +
                " \"type\" : \"Point\"," +
                " \"coordinates\" : [ " +
                " -33.882909, " +
                " 151.2239935" +
                "]" +
                "}" +
                "}";

         String out = template.requestBody("http://localhost:" + getPort() + "/meetings", body, String.class);

        assertNotNull(out);
    }


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        MongoURI mongoURI = new MongoURI("mongodb://tester:drmf5ltd@ds045988.mongolab.com:45988/team2media");
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
                from("spark-rest:post:meetings").convertBodyTo(Meeting.class)
                .to("direct:insert");

                from("direct:insert").to("bean:meetingService?method=insertMeeting").to("direct:remove");

                from("direct:remove").to("log:Out").convertBodyTo(ObjectId.class).to("bean:meetingService?method=removeMeeting");
            }
        };
    }
}
