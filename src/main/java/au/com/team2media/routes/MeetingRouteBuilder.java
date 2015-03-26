package au.com.team2media.routes;

import au.com.team2media.model.TwitterOAuth;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.atmosphere.websocket.WebsocketComponent;
import org.apache.camel.component.twitter.TwitterComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;

public class MeetingRouteBuilder extends RouteBuilder {

    @Autowired
    private TwitterOAuth twitterOAuth;

    @Override
    public void configure() throws Exception {

        // setup Camel web-socket component on the port we have defined
        WebsocketComponent websocketComponent = getContext().getComponent("atmosphere-websocket", WebsocketComponent.class);
        websocketComponent.createConfiguration("atmosphere-websocket://services");


        // setup Twitter component
        TwitterComponent twitterComponent = getContext().getComponent("twitter", TwitterComponent.class);
        twitterComponent.setAccessToken(twitterOAuth.getAccessToken());
        twitterComponent.setAccessTokenSecret(twitterOAuth.getAccessTokenSecret());
        twitterComponent.setConsumerKey(twitterOAuth.getConsumerKey());
        twitterComponent.setConsumerSecret(twitterOAuth.getConsumerSecret());

        // poll twitter search for new tweets
        fromF("twitter://search?keywords=%s&httpProxyHost=%s&httpProxyPort=%s", "Sydney", "proxy.auiag.corp", "8080")
                .to("log:tweet")
                        // and push tweets to all web socket subscribers on camel-tweet
                .to("atmosphere-websocket:////services");


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
