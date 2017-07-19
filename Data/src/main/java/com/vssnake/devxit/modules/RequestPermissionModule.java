package com.vssnake.devxit.modules;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

/**
 * Created by vssnake on 14/07/2017.
 */

public class RequestPermissionModule {

    AppCompatActivity appCompatActivity;

    public static int LOCATION_FINE_REQUEST = 1;


    @Inject
    public RequestPermissionModule(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
    }

    public void checkLocationPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(appCompatActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(appCompatActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(appCompatActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_FINE_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
