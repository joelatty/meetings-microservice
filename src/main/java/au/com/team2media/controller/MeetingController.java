package au.com.team2media.controller;


import au.com.team2media.builder.MeetingBuilder;
import au.com.team2media.error.ResponseError;
import au.com.team2media.model.Meeting;
import au.com.team2media.service.MeetingService;

import au.com.team2media.util.DayOfWeekUtil;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import spark.Request;
import spark.Response;
import spark.Route;

import java.time.DayOfWeek;
import java.util.Map;

import static au.com.team2media.util.CursorToJSONUtil.cursorToJson;
import static au.com.team2media.util.JsonUtil.json;
import static spark.Spark.*;

/**
 * Created by joe on 3/01/15.
 */
public class MeetingController {

    private static final DayOfWeekUtil dayOfWeekUtil = new DayOfWeekUtil();

    public MeetingController(final MeetingService meetingService) {

        get("/", (request, response) -> getMapResponse(), json());

        get("/daysOfTheWeek", (request, response) ->
                dayOfWeekUtil.getDaysOfTheWeek(),
                json());

        get("/meetings",
            (request, response) ->  {
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

        post("/meetings", (request, response) -> {

            Meeting meeting = new MeetingBuilder()
            .setName(request.queryParams("name"))
            .setSuburb(request.queryParams("suburb"))
            .setType(request.queryParams("type"))
            .setStartTime(request.queryParams("startTime"))
            .setEndTime(request.queryParams("endTime"))
            .setDayOfWeek(dayOfWeekUtil.getDayOfWeek(request.queryParams("dayOfWeek")))
            .build();

            return meetingService.createMeeting(meeting);

        }, cursorToJson());

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(new Gson().toJson(new ResponseError(e)));
        });
    }

    private Map<String, String> getMapResponse() {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", "Your just trying to trick me");
        return map;
    }
}