package au.com.team2media.builder;

import au.com.team2media.model.Meeting;

import java.time.DayOfWeek;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 18/02/15
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeetingBuilder {
    String name;
    String suburb;
    String type;
    DayOfWeek dayOfWeek;
    String startTime;
    String endTime;
    Date dateOfBirth;

    public MeetingBuilder() {}

    public MeetingBuilder(Meeting meeting) {
        name = meeting.getName();
        suburb = meeting.getSuburb();
        type = meeting.getType();
        dayOfWeek = meeting.getDayOfWeek();
        startTime = meeting.getStartTime();
        endTime = meeting.getEndTime();
        dateOfBirth = meeting.getDateOfBirth();
    }

    public Meeting build() {
        Meeting meeting = new Meeting();
        meeting.setName(name);
        meeting.setSuburb(suburb);
        meeting.setType(type);
        meeting.setDayOfWeek(dayOfWeek);
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setDateOfBirth(dateOfBirth);

        return meeting;
    }

    public MeetingBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MeetingBuilder setSuburb(String suburb) {
        this.suburb = suburb;
        return this;
    }

    public MeetingBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public MeetingBuilder setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public MeetingBuilder setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public MeetingBuilder setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public MeetingBuilder setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

}
