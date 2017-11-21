package com.baturamobile.mvp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.baturamobile.utils.storage.PreferencesManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.app.Activity.RESULT_OK;

/**
 * Created by vssnake on 03/01/2017.
 */

public class LocationModule implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener{

    LocationModuleCallback locationModuleCallback;
    LocationPopUPCallback locationPopUPCallback;
    BaseActivity baseActivity;

    public static final int GPS_DISABLED = 1;
    public static final int PERMISSION_INSUFFICIENT = 2;
    public static final int LOCATION_READY = 0;

    private static final int POPUP_ENABLE_LOCATION = 10;
    private static final int  POPUP_REQUEST_PERMISSION = 11;

    private static final int  REQUEST_ENABLE_PERMISSION_LOCATION = 20;
    private static final int  REQUEST_ENABLE_LOCATION = 21;

    private static final int  REQUEST_CHECK_SETTINGS = 30;

    private static final String DENY_POPUP_LOCATION = "popupLocationKEy";
    private static final String DENY_POPUP_REQUEST_LOCATION_PERMISSION = "popupLocationPermissionKEy";

    String mPermission;

    int mPriorityLocation = 0;



    public LocationModule(LocationModuleCallback locationModuleCallback,
                          LocationPopUPCallback locationPopUPCallback,
                          BaseActivity baseActivity,
                          String permission,
                        int priorityLocation){
        this.locationPopUPCallback = locationPopUPCallback;
        this.locationModuleCallback = locationModuleCallback;
        this.baseActivity = baseActivity;
        this.mPermission = permission;
        this.mPriorityLocation = priorityLocation;



    }

    public void  checkRequirements(){

        if ((!checkPermissionEnabled(baseActivity) || !checkPositionPermission()) &&
                isDenyLocationPopUP() && isDenyRequestPermissionLocationPopUp()) {
            locationModuleCallback.onLocationPermissionFailed();
        } else {
            if (!checkPermissionEnabled(baseActivity) && !isDenyLocationPopUP()) {
                showPopupRequestLocation(locationPopUPCallback.onRequestPopUpGpsDisabled());
            } else if (!checkPositionPermission() && !isDenyRequestPermissionLocationPopUp()) {
                showRequestLocationPermission(locationPopUPCallback.onRequestPopUpPermissionInsufficient());
            } else {
                locationModuleCallback.onLocationPermissionReady();
            }
        }

    }

    public void checkRequirementsUglyWay(){
        requestLocationPermission(baseActivity);
    }

    public boolean isRequirementsAllowed(){
        if ((!checkPermissionEnabled(baseActivity) || !checkPositionPermission()) &&
                isDenyLocationPopUP() && isDenyRequestPermissionLocationPopUp()) {
           return false;
        } else {
            if (!checkPermissionEnabled(baseActivity) && !isDenyLocationPopUP()) {
                return false;
            } else if (!checkPositionPermission() && !isDenyRequestPermissionLocationPopUp()) {
                return false;
            } else {
                return true;
            }
        }
    }

    public void forceCheckRequirements(){
        showPopUp(DENY_POPUP_LOCATION);
        showPopUp(DENY_POPUP_REQUEST_LOCATION_PERMISSION);
        checkRequirements();
    }

    private boolean isDenyLocationPopUP(){
        return PreferencesManager.getBoolean(DENY_POPUP_LOCATION,false);
    }

    private boolean isDenyRequestPermissionLocationPopUp(){
        return PreferencesManager.getBoolean(DENY_POPUP_REQUEST_LOCATION_PERMISSION,false);
    }

    private void notShowPopUpAnymore(String key){
        PreferencesManager.setBoolean(key,true);
    }

    private void showPopUp(String key){
        PreferencesManager.setBoolean(key,false);
    }

    public void processResult(int requestCode,int resultCode){
        switch (requestCode){
            case POPUP_ENABLE_LOCATION:
                if (resultCode == RESULT_OK){
                    showRequestEnableLocation(baseActivity);
                }else{
                    notShowPopUpAnymore(DENY_POPUP_LOCATION);
                    checkRequirements();
                }
                break;
            case POPUP_REQUEST_PERMISSION:
                if (resultCode == RESULT_OK){
                    requestLocationPermission(baseActivity);
                }else{
                    notShowPopUpAnymore(DENY_POPUP_REQUEST_LOCATION_PERMISSION);
                    checkRequirements();
                }
                break;
            case REQUEST_ENABLE_PERMISSION_LOCATION:
                if (resultCode == RESULT_OK){
                    locationModuleCallback.onLocationPermissionReady();
                }
                break;
            case REQUEST_ENABLE_LOCATION:
                checkRequirements();
                break;
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        onConnected(null);
                        break;
                    case Activity.RESULT_CANCELED:
                        if (locationModuleCallback != null){
                            locationModuleCallback.onLocationSettingFailed();
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void processPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode  == REQUEST_ENABLE_PERMISSION_LOCATION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationModuleCallback.onLocationPermissionReady();
            }else{
                locationModuleCallback.onLocationPermissionFailed();
            }
        }
    }

    private void showRequestLocationPermission(Intent intent){
        baseActivity.startActivityForResult(intent,POPUP_REQUEST_PERMISSION);
    }

    private void showPopupRequestLocation(Intent intent){
        baseActivity.startActivityForResult(intent,POPUP_ENABLE_LOCATION);
    }

    private void showRequestEnableLocation(BaseActivity baseActivity){
        baseActivity.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                ,REQUEST_ENABLE_LOCATION);
    }

    private void requestLocationPermission(BaseActivity baseActivity){
        ActivityCompat.requestPermissions(baseActivity,
                new String[]{mPermission},REQUEST_ENABLE_PERMISSION_LOCATION);
    }

    private boolean checkPositionPermission(){
        int permission = ContextCompat.checkSelfPermission(baseActivity, mPermission);

        return permission == android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissionEnabled(Context context){
        LocationManager manager = (LocationManager)context
                .getSystemService(Context.LOCATION_SERVICE);

        if (mPermission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)){
            return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }else{
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

    }



    public interface LocationPopUPCallback{
        Intent onRequestPopUpGpsDisabled();
        Intent onRequestPopUpPermissionInsufficient();
    }

    public interface LocationModuleCallback{
        void onLocationPermissionReady();
        void onLocationPermissionFailed();
        void onLocationChanged(Location location);
        void onLocationDisabled();
        void onLocationSettingFailed();
    }

    //region Location Updates
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    int mIntervalLocation = 0;
    int mIntervalFastestLocation = 0;


    public void initLocationUpdate(int interval,int fastestInterval,int priority){
        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(baseActivity)
                    .addApi(LocationServices.API)
                    .enableAutoManage(baseActivity,this)
                    .addConnectionCallbacks(this)
                    .build();
        }

        this.mIntervalLocation = interval;
        this.mIntervalFastestLocation = fastestInterval;
        this.mPriorityLocation = priority;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationModuleCallback.onLocationDisabled();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mLocationRequest == null){
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(mIntervalLocation)
                    .setFastestInterval(mIntervalFastestLocation)

                    .setPriority(mPriorityLocation);
        }


        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .build();

        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(baseActivity).checkLocationSettings(locationSettingsRequest);

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(LocationModule.this.mGoogleApiClient,
                                LocationModule.this.mLocationRequest,LocationModule.this);
                    }catch (Throwable e){
                        Utils.throwError(e);
                    }

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        LocationModule.this.baseActivity,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });



    }

    @Override
    public void onConnectionSuspended(int i) {
        locationModuleCallback.onLocationDisabled();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationModuleCallback != null){
            locationModuleCallback.onLocationChanged(location);
        }

    }

    @SuppressLint("MissingPermission")
    public Location getLastLocation(){
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }
    //endregion
}
