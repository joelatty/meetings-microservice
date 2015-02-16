package au.com.team2media.model;

import java.time.DayOfWeek;

/**
 * Created by joe on 3/01/15.
 */
public class Meeting {
    String name;
    String suburb;
    DayOfWeek dayOfWeek;
    String start;
    String end;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setDayOfWeek(String dayOfWeek) {
        setDayOfWeek(DayOfWeek.valueOf(dayOfWeek));
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
