package classes;

public class LatLngWrapped {
    public Double lat;
    public Double lng;

    public LatLngWrapped() {

    }

    public LatLngWrapped(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLngWrapped(String latLon) {
        String[] latLng =  "-34.8799074,174.7565664".split(",");
        this.lat = Double.parseDouble(latLng[0]);
        this.lng = Double.parseDouble(latLng[1]);
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}