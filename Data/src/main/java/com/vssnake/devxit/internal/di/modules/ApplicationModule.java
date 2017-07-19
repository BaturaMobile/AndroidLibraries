package com.vssnake.devxit.internal.di.modules;

import android.content.Context;
import android.os.Bundle;

import com.vssnake.devxit.data.executor.JobExecutor;
import com.vssnake.devxit.domain.usecase.DevxitUseCaseCallerFactory;
import com.vssnake.devxit.executor.PostExecutionThread;
import com.vssnake.devxit.executor.ThreadExecutor;
import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.view.DevxitApp;
import com.vssnake.devxit.view.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vssnake on 07/02/2017.
 */

@Module
public class ApplicationModule {

    private final DevxitApp devxitApp;


    public ApplicationModule(DevxitApp devxitApp)
    {
        if (devxitApp ==  null){
            throw new NullPointerException("DevxitApp is not initialized");
        }

        this.devxitApp = devxitApp;
    }

    @Provides
    @Singleton
    Context provideAppContext(){
        return devxitApp.getApplicationContext();
    }

    @Provides
    public DevxitApp provideApplication() {
        return devxitApp;
    }


    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(){
        return new JobExecutor();
    }

    @Provides @Singleton
    PostExecutionThread provideUIThread(){
        return new UIThread();
    }

    @Provides @Singleton
    DevxitUseCaseCallerFactory provideDevxitUseCaseCallerFactory(
            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new DevxitUseCaseCallerFactory(threadExecutor,postExecutionThread);
    }

    @Provides @Singleton
    ObserverController provideObserverController(PostExecutionThread postExecutionThread){
        return new ObserverController(postExecutionThread);
    }


    @Provides @Singleton
    Bundle provideParametersFactory(){
        return new Bundle();
    }



}
