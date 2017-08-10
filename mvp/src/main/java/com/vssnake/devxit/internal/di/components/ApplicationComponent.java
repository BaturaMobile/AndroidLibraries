package com.vssnake.devxit.internal.di.components;

import android.content.Context;
import android.os.Bundle;

import com.vssnake.devxit.executor.PostExecutionThread;
import com.vssnake.devxit.executor.ThreadExecutor;
import com.vssnake.devxit.internal.di.modules.ApplicationModule;
import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.view.DevxitPresenter;
import com.vssnake.devxit.view.DevxitView;
import com.vssnake.devxit.view.delegate.DevxitActivityDelegateImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by vssnake on 07/02/2017.
 */
@Singleton
@Component( modules = ApplicationModule.class)
public interface ApplicationComponent {

    //Exposed to sub-graphs
    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    ObserverController observerController();

    Bundle parametersFactory();

    void inject(DevxitActivityDelegateImpl<DevxitView, DevxitPresenter<DevxitView>> devxitActivityDelegate);
}
