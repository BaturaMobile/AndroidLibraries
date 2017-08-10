package com.vssnake.devxit.view.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.vssnake.devxit.modules.DevxitModuleDelegate;
import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.view.DevxitPresenter;
import com.vssnake.devxit.view.DevxitView;

/**
 * Created by vssnake on 07/02/2017.
 */

public class DevxitFragmentDelegateImpl<V extends DevxitView,P extends DevxitPresenter<V>>
        implements DevxitFragmentDelegate {


    protected DevxitDelegateCallback<V, P> devxitDelegateCallback;
    protected DevxitMVPDelegate<V, P> devxitMVPDelegate;
    protected DevxitModuleDelegate devxitModuleDelegate;

    ObserverController observerController;

    public DevxitFragmentDelegateImpl(DevxitDelegateCallback<V,P> devxitDelegateCallback,
                                      ObserverController observerController){
        if (devxitDelegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }
        this.observerController = observerController;
        this.devxitDelegateCallback = devxitDelegateCallback;
    }

    protected DevxitMVPDelegate<V, P> getDevxitMVPDelegate() {
        if (devxitMVPDelegate == null) {
            devxitMVPDelegate = new DevxitMVPDelegate<>(devxitDelegateCallback);
        }

        return devxitMVPDelegate;
    }

    protected DevxitModuleDelegate getDevxitModuleDelegate(){
        if (devxitModuleDelegate == null){
            devxitModuleDelegate = new DevxitModuleDelegate(observerController);
        }
        return devxitModuleDelegate;
    }


    @Override
    public void onCreate(Bundle saved) {
        //getDevxitMVPDelegate().initializeDependencieInjection();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getDevxitMVPDelegate().attachView();

    }

    @Override
    public void onDestroyView() {
        getDevxitMVPDelegate().detachView();

    }

    @Override
    public void onPause() {
        getDevxitModuleDelegate().onStop();
    }

    @Override
    public void onResume() {
        getDevxitModuleDelegate().onStart();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onAttach(Activity activity) {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
