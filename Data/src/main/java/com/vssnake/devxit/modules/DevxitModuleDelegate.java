package com.vssnake.devxit.modules;

import android.content.Intent;

import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.view.delegate.DevxitDelegateCallback;

import javax.inject.Inject;

/**
 * Created by vssnake on 27/02/2017.
 */

public class DevxitModuleDelegate{

    DevxitDelegateCallback callback;

    PermissionModule permission;

    @Inject
    ObserverController observerController;

    @Inject
    PermissionModule permissionModule;

    public DevxitModuleDelegate(DevxitDelegateCallback callback){
        this.callback = callback;

        callback.getDevxitApp().getApplicationComponent().inject(this);



    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionModule.resultPermission(requestCode,resultCode);
    }
}
