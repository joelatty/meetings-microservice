package au.com.team2media.builder;

import au.com.team2media.model.Coordinates;
import au.com.team2media.model.Location;
import au.com.team2media.model.Meeting;
import au.com.team2media.util.GeoJSONType;

import java.time.DayOfWeek;
import java.util.Date;

public class MeetingBuilder {
    String name;
    String suburb;
    String type;
    DayOfWeek dayOfWeek;
    String startTime;
    String endTime;
    Double latitude;
    Double longitude;
    Date dateOfBirth;
    GeoJSONType locationType;

    public MeetingBuilder() {}

    public MeetingBuilder(Meeting meeting) {
        name = meeting.getName();
        suburb = meeting.getSuburb();
        type = meeting.getType();
        dayOfWeek = meeting.getDayOfWeek();
        startTime = meeting.getStartTime();
        endTime = meeting.getEndTime();
        dateOfBirth = meeting.getDateOfBirth();
        setCoordinates(meeting);
    }

    private void setCoordinates(Meeting meeting) {
        Location location = meeting.getLocation();
        locationType = location.getType();
        if(location != null) {
            Coordinates coordinates = location.getCoordinates();
            if (coordinates != null) {
                latitude = coordinates.getLatitude();
                longitude = coordinates.getLongitude();
            }
        }
    }

    public Meeting build() {
        Meeting meeting = new Meeting();
        meeting.setName(name);
        meeting.setSuburb(suburb);
        meeting.setType(type);
        meeting.setDayOfWeek(dayOfWeek);
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setLocation(getLocation());
        meeting.setDateOfBirth(dateOfBirth);

        return meeting;
    }

    private Location getLocation() {
        Location location = new Location();
        location.setType(locationType);
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(latitude);
        coordinates.setLongitude(longitude);
        location.setCoordinates(coordinates);
        return location;
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

    public MeetingBuilder setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public MeetingBuilder setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public MeetingBuilder setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public MeetingBuilder setLocationType(GeoJSONType type) {
        this.locationType = type;
        return this;
    }
}
