package au.com.team2media.camel.spark;

import au.com.team2media.model.Meeting;
import au.com.team2media.service.MeetingService;
import com.fasterxml.jackson.databind.Module;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mongodb.MongoDbComponent;
import org.apache.camel.component.sparkrest.SparkComponent;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.Registry;
import org.apache.camel.spi.RestRegistry;
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
                from("spark-rest:post:meetings").convertBodyTo(Meeting.class)
                .to("direct:insert");

                from("direct:insert").to("bean:meetingService?method=insertMeeting").to("direct:remove");

                from("direct:remove").to("log:Out").convertBodyTo(ObjectId.class).to("bean:meetingService?method=removeMeeting");
            }
        };
    }
}
