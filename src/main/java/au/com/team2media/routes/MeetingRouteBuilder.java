package au.com.team2media.routes;

import au.com.team2media.model.Meeting;
import com.mongodb.DBCursor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

public class MeetingRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // configure we want to use spark-rest as the component for the rest DSL
        // and we enable json binding mode

        restConfiguration().component("spark-rest").bindingMode(RestBindingMode.json)  // and output using pretty print
        .dataFormatProperty("prettyPrint", "true");

        from("spark-rest:get:hello").to("bean:meetingService?method=getAllMeetings");

        // this user REST service is json only
        rest("/test").consumes("application/json").produces("application/json")
            .get("/meetings")
            .to("mongodb:mongoBean?database=naorg&collection=meeting&operation=findAll")


            .get("all-meetings")
                .to("bean:meetingService?method=getAllMeetings");



//                .get("/all")
//                .to("bean:meetingService?method=getAll");

//                .put("/update").type(Meeting.class).outType(Meeting.class)
//                .to("bean:meetingService?method=updateUser");
    }
}
