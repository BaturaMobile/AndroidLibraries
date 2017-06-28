package com.vssnake.devxit.internal.di.components;

import android.support.v7.app.AppCompatActivity;

import com.vssnake.devxit.internal.di.modules.ActivityModule;
import com.vssnake.devxit.internal.di.PerActivity;

import dagger.Component;

/**
 * Created by vssnake on 07/02/2017.
 */

@PerActivity
@Component (dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    //Exposed to sub-graph
    AppCompatActivity activity();


}
