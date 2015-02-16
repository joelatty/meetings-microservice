package au.com.team2media.controller;


import au.com.team2media.au.com.team2media.error.ResponseError;
import au.com.team2media.service.MeetingService;
import au.com.team2media.util.DayOfWeekUtil;
import com.google.common.collect.Maps;
import com.mongodb.DBCursor;

import java.util.Map;

import static au.com.team2media.util.CursorToJSONUtil.cursorToJson;
import static au.com.team2media.util.CursorToJSONUtil.toJson;
import static au.com.team2media.util.JsonUtil.json;
import static au.com.team2media.util.JsonUtil.toJsonFromGson;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.halt;

/**
 * Created by joe on 3/01/15.
 */
public class MeetingController {

    public MeetingController(final MeetingService meetingService) {

        get("/", (request, response) -> getMapResponse(), json());

        get("/daysOfTheWeek", (request, response) ->
                DayOfWeekUtil.getDaysOfTheWeek(),
                json());

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
                DBCursor cursor = meetingService.getMeetingsBySuburb(request.params(":suburb"));
                if(cursor != null) {
                    return cursor;
                } else {
                    return new ResponseError("No meetings found for suburb: " + request.params(":suburb"));
                }
            } catch (Exception e) {
                return new ResponseError(e.getMessage());
            }
        }, cursorToJson());

        exception(IllegalArgumentException.class, (exception, request, response) -> {
            response.status(400);
            response.body(toJsonFromGson(new ResponseError(exception)));
        });
    }

    private Map<String, String> getMapResponse() {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", "Your just trying to trick me");
        return map;
    }
}