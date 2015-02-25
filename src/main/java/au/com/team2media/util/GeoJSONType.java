package au.com.team2media.util;

public enum GeoJSONType {
    POINT( "Point"),
    LINESTRING("LineString"),
    POLYGON("Polygon"),
    MULTIPOINT("MultiPoint"),
    MULTILINESTRING("MultiLineString"),
    MULTIPOLYGON("MultiPolygon"),
    GEOMETRYCOLLECTION("GeometryCollection");

    private String displayValue;

    private GeoJSONType(String value) {
        this.displayValue = value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static GeoJSONType fromDisplayValue(String displayValue) {
        for(GeoJSONType type : GeoJSONType.values()) {
            if(type.displayValue.equals(displayValue)) {
                return type;
            }
        }
        return null;
    }
}
