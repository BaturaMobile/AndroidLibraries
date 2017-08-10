package com.vssnake.devxit.view;

import android.app.Application;

import com.vssnake.devxit.internal.di.components.ApplicationComponent;
import com.vssnake.devxit.internal.di.components.DaggerApplicationComponent;
import com.vssnake.devxit.internal.di.modules.ApplicationModule;

/**
 * Created by vssnake on 07/02/2017.
 */

public abstract class DevxitApp extends Application {

    private ApplicationComponent applicattionComponent;


    @Override public void onCreate() {
        super.onCreate();
    }


    protected void initializeInjector() {

        applicattionComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent(){
        return this.applicattionComponent;
    }
}
