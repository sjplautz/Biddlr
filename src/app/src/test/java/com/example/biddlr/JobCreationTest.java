package com.example.biddlr;

import android.location.Location;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.MapFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import classes.LatLngWrapped;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ui.JobCreationFragment.checkFormat;
import static ui.JobCreationFragment.forwardGeocode;

public class JobCreationTest {

    @Test
    public void checkFormat_isCorrect(){
        String goodString = "3720 Modesto Drive, Rockford, Il";
        String badString = "123 bad";
        Boolean resultGood = checkFormat(goodString);
        Boolean resultBad = checkFormat(badString);
        assertTrue(resultGood);
        assertFalse(resultBad);
    }

    /*
    @Test
    public void checkForwardGeocode_isCorrect(){
        String address = "2021 Fox Ridge Rd, Tuscaloosa, AL 45406-3056";
        LatLngWrapped correctLatLng = new LatLngWrapped(33.2260,-87.5652);
        LatLngWrapped result = forwardGeocode(address);
        assertEquals(correctLatLng.lat, result.lat);
        assertEquals(correctLatLng.lng, result.lng);
    }
    */
}
