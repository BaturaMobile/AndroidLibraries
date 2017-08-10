package com.vssnake.devxit.modules;

import android.content.Context;

import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.observer.ObserverInterface;

/**
 * Created by vssnake on 27/02/2017.
 */

public abstract class BaseModule implements ObserverInterface{

    public static final String ON_ATTACH_MODULE = "onResume";
    public static final String ON_DETTACH_MODULE = "onDestroy";

    protected ObserverController observerController;
    protected Context context;

    public BaseModule(ObserverController observerController,Context context){
        this.context = context;
        this.observerController = observerController;
        this.observerController.observe(ON_ATTACH_MODULE,this);
        this.observerController.observe(ON_DETTACH_MODULE,this);
    }

    public void onEvent(String event, Object data){

        switch (event){
            case ON_ATTACH_MODULE:
                onStart();
                break;
            case ON_DETTACH_MODULE:
                onStop();
                break;
        }
    }

    public abstract void onStart();

    public abstract void onStop();


}
