package au.com.team2media.camel.freemarker;

import au.com.team2media.builder.MeetingBuilder;
import au.com.team2media.model.Meeting;
import au.com.team2media.util.GeoJSONType;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

public class MeetingFreemarkerTest extends CamelTestSupport {

    @Test
    public void testFreemarkerTemplate() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:mail");
        mock.expectedMessageCount(1);
        mock.message(0).header("Subject").isEqualTo("Thanks for entering a meeting");
        mock.message(0).header("From").isEqualTo("donotreply@team2media.com");
        mock.message(0).body().contains("Thank you for entering a new meeting named Log Cabin");

        Meeting meeting = new MeetingBuilder()
        .setName("Log Cabin")
                .setSuburb("Newtown")
                .setType("General")
                .setStartTime("8:00PM")
                .setEndTime("9:00PM")
                .setDayOfWeek(DayOfWeek.SATURDAY)
                .setLocationType(GeoJSONType.POINT)
                .setLatitude(-33.890844)
                .setLongitude(151.274291)
                .setDateOfBirth(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/1980"))
                .build();

        template.send("direct:mail", createMeetingExchange(meeting));

        assertMockEndpointsSatisfied();
    }

    private Exchange createMeetingExchange(Meeting meeting) {
        Exchange exchange = context.getEndpoint("direct:mail").createExchange();

        Message message = exchange.getIn();
        message.setHeader("meeting", meeting);
        message.setBody("<h2>Here we are</h2>");

        return exchange;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:mail")
                        .setHeader("Subject", constant("Thanks for entering a meeting"))
                        .setHeader("From", constant("donotreply@team2media.com"))
                        .to("freemarker://meeting/mail.ftl")
                        .to("log:mail")
                        .to("mock:mail");
            }
        };
    }

}
