package com.vssnake.devxit.modules;

import com.vssnake.devxit.observer.ObserverController;

/**
 * Created by vssnake on 27/02/2017.
 */

public abstract class BaseModule {
    ObserverController observerController;

    public BaseModule(ObserverController observerController){

        this.observerController = observerController;
    }
}
