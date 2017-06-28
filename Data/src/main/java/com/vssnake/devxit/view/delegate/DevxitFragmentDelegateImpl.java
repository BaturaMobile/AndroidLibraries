package com.vssnake.devxit.view.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.vssnake.devxit.view.DevxitPresenter;
import com.vssnake.devxit.view.DevxitView;

/**
 * Created by vssnake on 07/02/2017.
 */

public class DevxitFragmentDelegateImpl<V extends DevxitView,P extends DevxitPresenter<V>>
        implements DevxitFragmentDelegate {


    protected DevxitDelegateCallback<V, P> devxitDelegateCallback;
    protected DevxitMVPDelegate<V, P> devxitMVPDelegate;

    public DevxitFragmentDelegateImpl(DevxitDelegateCallback<V,P> devxitDelegateCallback){
        if (devxitDelegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }

        this.devxitDelegateCallback = devxitDelegateCallback;
    }

    protected DevxitMVPDelegate<V, P> getDevxitMVPDelegate() {
        if (devxitMVPDelegate == null) {
            devxitMVPDelegate = new DevxitMVPDelegate<>(devxitDelegateCallback);
        }

        return devxitMVPDelegate;
    }


    @Override
    public void onCreate(Bundle saved) {
        getDevxitMVPDelegate().initializeDependencieInjection();
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

    }

    @Override
    public void onResume() {

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
