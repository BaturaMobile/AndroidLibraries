package com.vssnake.devxit.view.delegate;

import com.vssnake.devxit.view.DevxitPresenter;
import com.vssnake.devxit.view.DevxitView;

/**
 * Created by vssnake on 07/02/2017.
 */

public class DevxitMVPDelegate<V extends DevxitView, P extends DevxitPresenter<V>> {

    protected DevxitDelegateCallback<V,P> devxitMVPDelegateCallback;

    DevxitMVPDelegate(DevxitDelegateCallback<V,P> callback){
        if (callback == null){
            throw new NullPointerException("DevxitDelegateCallback is null");
        }

        this.devxitMVPDelegateCallback = callback;
    }


    /**
     * Attaches the view to the presenter
     */
    void attachView() {
        if (devxitMVPDelegateCallback.getPresenter() != null)
            devxitMVPDelegateCallback.getPresenter()
                    .attachView(devxitMVPDelegateCallback.getDevxitView());

    }

    /**
     * Called to detach the view from presenter
     */
    void detachView() {
        if (devxitMVPDelegateCallback.getPresenter() != null)
            devxitMVPDelegateCallback.getPresenter().detachView();
    }

    /**
     * Called when you want to initialice dependencie Injection
     */
    void initializeDependencieInjection(){
        devxitMVPDelegateCallback.initializeDependencieInjection();
    }


}
