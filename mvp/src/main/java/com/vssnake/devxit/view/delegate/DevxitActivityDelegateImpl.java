package com.vssnake.devxit.view.delegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vssnake.devxit.executor.PostExecutionThread;
import com.vssnake.devxit.modules.DevxitModuleDelegate;
import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.modules.PopUPModule;
import com.vssnake.devxit.view.DevxitPresenter;
import com.vssnake.devxit.view.DevxitView;

/**
 * Created by vssnake on 07/02/2017.
 */

public class DevxitActivityDelegateImpl<V extends DevxitView,P extends DevxitPresenter<V>>
        implements DevxitActivityDelegate {

    protected DevxitDelegateCallback<V,P> devxitMVPDelegateCallback;
    protected DevxitMVPDelegate<V,P> devxitMVPDelegate;
    protected DevxitLoadingDelegate devxitLoadingDelegate;
    private DevxitModuleDelegate devxitModuleDelegate;
    private PopUPModule mPopUpModule;

    ObserverController observerController;
    PostExecutionThread postExecutionThread;


    public DevxitActivityDelegateImpl(
            DevxitDelegateCallback<V, P> callback,
            ObserverController observerController,
            PostExecutionThread postExecutionThread,
            PopUPModule popUPModule) {
        if (devxitMVPDelegateCallback != null) {
            throw new NullPointerException("DevxitDelegateCallback is null");
        }
        this.postExecutionThread = postExecutionThread;
        this.devxitMVPDelegateCallback = callback;
        this.observerController = observerController;
        this.mPopUpModule = popUPModule;

        getDevxitModuleDelegate();

    }

    private void initLoadingDelegate(Context context){
        devxitLoadingDelegate = new DevxitLoadingDelegate(context);
    }

    protected DevxitMVPDelegate<V,P> getDevxitMVPDelegate(){
        if (devxitMVPDelegate == null){
            devxitMVPDelegate = new DevxitMVPDelegate<>(devxitMVPDelegateCallback);
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
    public void onCreate(Bundle bundle,Context context) {
        initLoadingDelegate(context);
        //getDevxitMVPDelegate().initializeDependencieInjection();

    }

    @Override
    public void onDestroy() {
        getDevxitMVPDelegate().detachView();

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {


    }

    @Override
    public void onStart() {
        getDevxitMVPDelegate().attachView();
        getDevxitModuleDelegate().onStart();
    }

    @Override
    public void onStop() {
        getDevxitModuleDelegate().onStop();
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onContentChanged() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPopUpModule != null)
            mPopUpModule.onActivityResult(requestCode,resultCode,data,devxitMVPDelegateCallback);
    }

    @Override
    public void onLoading(boolean loading) {
        devxitLoadingDelegate.loading(loading);
    }

    @Override
    public void onError(String title, String error) {
        mPopUpModule.onErrorPopup(title,error,devxitMVPDelegateCallback.getDevxitActivity());
    }

    @Override
    public void onRetryError(String title, String error) {
        mPopUpModule.onRetryErrorPopup(title,error,devxitMVPDelegateCallback.getDevxitActivity());
    }

}
