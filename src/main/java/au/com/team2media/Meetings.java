package au.com.team2media;

import au.com.team2media.controller.MeetingController;
import au.com.team2media.service.MeetingService;
import spark.Spark;

import java.net.UnknownHostException;

/**
 * Created by joe on 2/01/15.
 */
public class Meetings {
    public static void main(String args[]) throws UnknownHostException {

        Spark.options("/*", (request,response)->{

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if(accessControlRequestMethod != null){
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
        });


        new MeetingController(new MeetingService());

    }
}
