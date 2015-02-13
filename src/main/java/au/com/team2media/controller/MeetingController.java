package au.com.team2media.controller;


import au.com.team2media.service.MeetingService;
import com.google.common.collect.Maps;

import java.util.Map;

import static au.com.team2media.util.CursorToJSONUtil.cursorToJson;
import static au.com.team2media.util.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.halt;

/**
 * Created by joe on 3/01/15.
 */
public class MeetingController {

    public MeetingController(final MeetingService meetingService) {

        get("/", (request, response) -> getMapResponse(), json());

        get("/meetings",
                (request, response) -> {
                    try {
                        return meetingService.getAllMeetings();
                    } catch (Exception e) {
                        halt(500);
                    }
                    return null;

                }, cursorToJson());

        get("/meetings/:suburb", (request, response) -> {
            try {
                return meetingService.getMeetingsBySuburb(request.params(":suburb"));
            } catch (Exception e) {
                halt(500);
            }

            return null;
        }, cursorToJson());
    }

    private Map<String, String> getMapResponse() {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", "Your just trying to trick me");
        return map;
    }
}