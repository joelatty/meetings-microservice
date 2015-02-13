package au.com.team2media;

import au.com.team2media.controller.MeetingController;
import au.com.team2media.service.MeetingService;

import java.net.UnknownHostException;

/**
 * Created by joe on 2/01/15.
 */
public class Meetings {
    public static void main(String args[]) throws UnknownHostException {

        new MeetingController(new MeetingService());

    }
}
