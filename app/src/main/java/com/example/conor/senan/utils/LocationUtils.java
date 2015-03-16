package com.example.conor.senan.utils;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.example.conor.senan.SenanApp;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Conor on 27/01/2015.
 */
public class LocationUtils {

    public static LatLng getDublinLatLng(){
        return new LatLng(53.3478, -6.2597);


    }


    public static Location getLocation() {
        // Get location from GPS if it's available
        LocationManager lm = (LocationManager)SenanApp.getContext().getSystemService(SenanApp.getContext().LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Location wasn't found, check the next most accurate place for the current location
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = lm.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            myLocation = lm.getLastKnownLocation(provider);
        }


        return myLocation;
    }


}
