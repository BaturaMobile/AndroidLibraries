package com.baturamobile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * Created by vssnake on 21/11/2017.
 */

public class BasicLocationRequest {


    Context mContext;

    private int mInterval;
    private int mFastInterval;
    private int mPriority;

    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationInterfaceCallback mLocationInterfaceCallback;

    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (mLocationInterfaceCallback != null){
                mLocationInterfaceCallback.onLocationResult(locationResult.getLastLocation());
            }
        }
    };




    public BasicLocationRequest(Context context){
        this.mContext = context;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
    }

    /**
     *
     * @param interval
     * @param fastInterval
     * @param priority example --> {@link LocationRequest#PRIORITY_BALANCED_POWER_ACCURACY}
     */
    public void initLocationUpdate(int interval,int fastInterval,int priority,
                                   LocationInterfaceCallback locationInterfaceCallback){
        this.mInterval = interval;
        this.mFastInterval = fastInterval;
        this.mPriority = priority;
        this.mLocationInterfaceCallback = locationInterfaceCallback;

        createLocationRequest();
        startLocationUpdates();
    }

    public void stopLocationUpdate(){
        if (mFusedLocationClient != null){
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }



    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(mInterval);
        mLocationRequest.setFastestInterval(mFastInterval);
        mLocationRequest.setPriority(mPriority);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        try{
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
        }catch (Exception ignore) {}

    }

    public interface LocationInterfaceCallback{
        void onLocationResult(Location location);
    }




}
