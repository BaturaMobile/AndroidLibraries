package com.vssnake.devxit.view.delegate;

import android.content.Context;

import com.vssnake.devxit.view.DevxitActivity;
import com.vssnake.devxit.view.DevxitApp;
import com.vssnake.devxit.view.DevxitPresenter;
import com.vssnake.devxit.view.DevxitView;

/**
 * Created by vssnake on 07/02/2017.
 */

public interface DevxitDelegateCallback<V extends DevxitView, P extends DevxitPresenter<V>> {

    /**
     *
     * @return
     */
    P getPresenter();

    void initializeDependencieInjection();

    V getDevxitView();

    Context getContext();

    DevxitApp getDevxitApp();

    DevxitActivity getDevxitActivity();
}
