package au.com.team2media.model;

import au.com.team2media.util.GeoJSONType;

/**
 * Created with IntelliJ IDEA.
 * User: s65721
 * Date: 25/02/15
 * Time: 9:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class Location {
    GeoJSONType type;
    Coordinates coordinates;

    public GeoJSONType getType() {
        return type;
    }

    public void setType(GeoJSONType type) {
        this.type = type;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
