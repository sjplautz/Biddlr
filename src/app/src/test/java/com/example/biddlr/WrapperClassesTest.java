package com.example.biddlr;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.time.LocalDateTime;

import classes.LatLngWrapped;
import classes.LocalDateTimeWrapped;

import static org.junit.Assert.*;

public class WrapperClassesTest {

    @Test
    public void WrapLatLngTest() {
        double lat = -34.8799074;
        double lng = 174.7565664;

        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);

        LatLngWrapped locationWrapped = LatLngWrapped.wrap(location);

        assertEquals(location.getLatitude(), locationWrapped.getLat(), 0.0);
        assertEquals(location.getLongitude(), locationWrapped.getLng(), 0.0);
    }

    @Test
    public void LatLngWrappedConversionTest() {
        double lat = -34.8799074;
        double lng = 174.7565664;

        LatLng location = new LatLng(lat, lng);
        LatLngWrapped locationWrapped = new LatLngWrapped(lat, lng);

        assertEquals(location.latitude, locationWrapped.getLat(), 0.0);
        assertEquals(location.longitude, locationWrapped.getLng(), 0.0);
    }

    @Test
    public void LatLngWrappedFromStringTest() {
        Double lat = -34.8799074;
        Double lng = 174.7565664;

        LatLng location = new LatLng(lat, lng);
        LatLngWrapped locationWrapped = new LatLngWrapped(location.toString());

        assertEquals(location.latitude, locationWrapped.getLat(), 0.0);
        assertEquals(location.longitude, locationWrapped.getLng(), 0.0);
    }

    @Test
    public void LocalDateTimeWrappedConversionTest() {
        LocalDateTime localDateTime = LocalDateTime.of(2019,1,1,12,0);
        LocalDateTimeWrapped localDateTimeWrapped = new LocalDateTimeWrapped(localDateTime.toString());

        assertEquals(localDateTime, localDateTimeWrapped.toLocalDateTime());
    }

}
