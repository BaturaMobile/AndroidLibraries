package com.baturamobile.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;

import com.baturamobile.design.R;


/**
 * Created by unai on 01/09/2016.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity
        implements BaseInteface, LocationModule.LocationPopUPCallback {

    private static final String TAG = "BaseActivity";

    public abstract T getPresenter();

    protected LocationModule locationModule;

    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    protected ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressDialog(this);
        progress.setTitle(R.string.loading);
        progress.setMessage(getContext().getString(R.string.please_wait));
        progress.setCancelable(false);

    }

    @Override
    public void onStart(){
        super.onStart();
        if (getPresenter() != null){
            getPresenter().onStart();
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        if (getPresenter() != null){
            getPresenter().onStop();
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (getPresenter() != null){
            getPresenter().onDestroy();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (getPresenter() != null){
            getPresenter().onResume();
        }
    }

    @Override
    @UiThread
    public void loading(boolean loading) {
        if (loading){
            progress.show();
        }else{
            progress.dismiss();
        }
    }

    @Override
    public void onError(String error,Throwable throwable) {
        if (throwable != null){
            Utils.throwError(throwable);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (locationModule != null) {
            locationModule.processResult(requestCode, resultCode);
        }

    }

    @Override
    public abstract Intent onRequestPopUpGpsDisabled();

    @Override
    public abstract Intent onRequestPopUpPermissionInsufficient();

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
       if (locationModule != null){
           locationModule.processPermissionResult(requestCode,permissions,grantResults);
       }
    }

    public void initLocationModule(LocationModule.LocationModuleCallback locationModuleCallback){
        if (getLocationModule() == null){
            locationModule = new LocationModule(locationModuleCallback,this,this);
        }
    }
    Context context;

    @Override
    public Context getApplicationContext(){
        if (this.context == null){
            return super.getApplicationContext();
        }else{
            return context;
        }
    }

    public void setContext(Context context){
        this.context = context;
    }

    public LocationModule getLocationModule() {
        return locationModule;
    }
}
