package com.vssnake.devxit.view.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.vssnake.devxit.modules.DevxitModuleDelegate;
import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.modules.PopUPModule;
import com.vssnake.devxit.view.DevxitFragment;
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
    protected DevxitLoadingDelegate devxitLoadingDelegate;
    PopUPModule mPopupModule;
    private DevxitFragment mDevxitFragment;

    ObserverController observerController;

    public DevxitFragmentDelegateImpl(DevxitFragment devxitFragment, DevxitDelegateCallback<V,P> devxitDelegateCallback,
                                      ObserverController observerController, PopUPModule popUPModule){
        if (devxitDelegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }
        this.observerController = observerController;
        this.devxitDelegateCallback = devxitDelegateCallback;
        this.mDevxitFragment = devxitFragment;
        mPopupModule = popUPModule;
        initLoadingDelegate(mDevxitFragment.getContext());
    }

    private void initLoadingDelegate(Context context){
        devxitLoadingDelegate = new DevxitLoadingDelegate(context);
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
    public void onCreate(Bundle saved,Context context) {
        initLoadingDelegate(context);
        //getDevxitMVPDelegate().initializeDependencieInjection();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



    }

    @Override
    public void onDestroyView() {
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

    @Override
    public void onLoading(boolean loading) {
        devxitLoadingDelegate.loading(loading);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPopupModule.onActivityResult(requestCode,resultCode,data,devxitDelegateCallback);
    }

    @Override
    public void onError(String title, String error) {
        mPopupModule.onErrorPopup(title,error,mDevxitFragment);
    }

    @Override
    public void onRetryError(String title, String error) {
        mPopupModule.onRetryErrorPopup(title,error,mDevxitFragment);
    }
}
