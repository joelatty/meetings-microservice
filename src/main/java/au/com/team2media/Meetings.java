package au.com.team2media;

import au.com.team2media.controller.MeetingController;
import au.com.team2media.service.MeetingService;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

/**
 * Created by joe on 2/01/15.
 */
public class Meetings {
    public static void main(String args[]) throws UnknownHostException {

        new MeetingController(new MeetingService());

    }
}
