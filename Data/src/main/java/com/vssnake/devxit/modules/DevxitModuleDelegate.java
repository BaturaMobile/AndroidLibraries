package com.vssnake.devxit.modules;

import com.vssnake.devxit.observer.ObserverController;

import javax.inject.Inject;

/**
 * Created by vssnake on 27/02/2017.
 */

public class DevxitModuleDelegate{

    ObserverController observerController;

    @Inject
    public DevxitModuleDelegate(ObserverController observerController){
        this.observerController = observerController;
    }


    public void onStart() {
        if (observerController != null){
            observerController.emit(BaseModule.ON_ATTACH_MODULE,null);
        }

    }

    public void onStop() {
        if (observerController != null){
            observerController.emit(BaseModule.ON_DETTACH_MODULE,null);
        }

    }
}
