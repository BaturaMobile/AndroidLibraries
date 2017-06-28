package com.vssnake.devxit.internal.di.modules;

import android.support.v7.app.AppCompatActivity;

import com.vssnake.devxit.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vssnake on 07/02/2017.
 */

@Module
public class ActivityModule {
    protected final AppCompatActivity appCompatActivity;

    public ActivityModule(AppCompatActivity appCompatActivity){
        if (appCompatActivity == null){
            throw new NullPointerException("appCompatActivity is null");
        }

        this.appCompatActivity = appCompatActivity;
    }

    @Provides
    @PerActivity
    AppCompatActivity activity(){
        return this.appCompatActivity;
    }

}
