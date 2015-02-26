package au.com.team2media.controller;


import au.com.team2media.error.ResponseError;
import au.com.team2media.model.Location;
import au.com.team2media.model.Meeting;
import au.com.team2media.service.MeetingService;
import au.com.team2media.typeadapter.DayOfWeekTypeAdapter;
import au.com.team2media.typeadapter.LocationTypeAdapter;
import au.com.team2media.util.DayOfWeekUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mongodb.DBCursor;

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

        post("/meetings", "application/json", (request, response) -> {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(DayOfWeek.class, new DayOfWeekTypeAdapter());
            gsonBuilder.registerTypeAdapter(Location.class, new LocationTypeAdapter());
            gsonBuilder.setDateFormat("dd/MM/yyyy").create();
            Gson gson = gsonBuilder.create();
            Meeting meeting = gson.fromJson(request.body(), Meeting.class);
            return meetingService.createMeeting(meeting);

        }, cursorToJson());

        exception(JsonSyntaxException.class, (e, req, res) -> {
            res.status(400);
            res.body(new Gson().toJson(new ResponseError(e)));
        });

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