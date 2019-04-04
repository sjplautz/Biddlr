package classes;

import android.location.Location;

/**
 * Wrapper class for LatLng
 * Firebase requires empty constructors for classes to be written/read from database (which LatLng does not have)
 */
public class LatLngWrapped {
    public Double lat;
    public Double lng;

    public LatLngWrapped() { }

    public LatLngWrapped(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLngWrapped(String latLon) {
        String[] latLng =  "-34.8799074,174.7565664".split(",");
        this.lat = Double.parseDouble(latLng[0]);
        this.lng = Double.parseDouble(latLng[1]);
    }

    //takes a google location object and wraps it as a LatLngWrapped object
    public static LatLngWrapped wrap(Location l){
        LatLngWrapped location = new LatLngWrapped();
        location.setLat(l.getLatitude());
        location.setLng(l.getLongitude());
        return location;
    }

    public Double getLat() { return lat; }

    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }

    public void setLng(Double lng) { this.lng = lng; }
}