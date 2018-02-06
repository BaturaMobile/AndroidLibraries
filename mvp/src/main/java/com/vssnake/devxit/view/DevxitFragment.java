package com.vssnake.devxit.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vssnake.devxit.internal.di.components.HasClientComponent;
import com.vssnake.devxit.internal.di.modules.ActivityModule;
import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.modules.PopUPModule;
import com.vssnake.devxit.view.delegate.DevxitDelegateCallback;
import com.vssnake.devxit.view.delegate.DevxitFragmentDelegate;
import com.vssnake.devxit.view.delegate.DevxitFragmentDelegateImpl;

import javax.inject.Inject;

/**
 * Created by vssnake on 07/02/2017.
 */
public abstract class DevxitFragment<V extends DevxitView, P extends DevxitPresenter<V>> extends Fragment
        implements DevxitDelegateCallback<V, P>, DevxitView,CommonViewInterface {


    protected DevxitFragmentDelegate fragmentDelegate;

    @Inject
    ObserverController observerController;

    @Inject
    PopUPModule popUPModule;

    @SuppressWarnings("uncheked")
    protected DevxitFragmentDelegate<V, P> getFragmentDelegate() {
        if (fragmentDelegate == null) {
            initializeDependencieInjection();
            fragmentDelegate = new DevxitFragmentDelegateImpl<>(this,
                    this, observerController, popUPModule);
        }
        return fragmentDelegate;
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasClientComponent<C>) getDevxitActivity()).getComponent());
    }

    protected ActivityModule getActivityModule(){
        return getDevxitActivity().getActivityModule();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        getFragmentDelegate().onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void initializeDependencieInjection() {

    }

    @NonNull
    @SuppressWarnings("uncheked")
    @Override
    public V getDevxitView() {
        return (V) this;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFragmentDelegate().onViewCreated(view, savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        getFragmentDelegate().onDestroyView();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentDelegate().onCreate(savedInstanceState, getDevxitActivity());
    }

    @Override public void onDestroy() {
        super.onDestroy();
        getFragmentDelegate().onDestroy();
    }

    @Override public void onPause() {
        super.onPause();
        getFragmentDelegate().onPause();
    }

    @Override public void onResume() {
        super.onResume();
        getFragmentDelegate().onResume();
    }

    @Override public void onStart() {
        super.onStart();
        getFragmentDelegate().onStart();
    }

    @Override public void onStop() {
        super.onStop();
        getFragmentDelegate().onStop();
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFragmentDelegate().onActivityCreated(savedInstanceState);
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        getFragmentDelegate().onAttach(activity);
    }

    @Override public void onDetach() {
        super.onDetach();
        getFragmentDelegate().onDetach();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentDelegate().onSaveInstanceState(outState);
    }

    @Override public AppCompatActivity getViewActivity(){
       return getDevxitActivity();
    }


    @Override
    public void onError(String title, String error)
    {
        getFragmentDelegate().onError(title,error);
    }

    @Override
    public void onRetryError(String title, String error) {
        getFragmentDelegate().onRetryError(title,error);
    }


    @Override public DevxitApp getDevxitApp(){
        return (DevxitApp) getDevxitActivity().getApplication();
    }

    @Override
    public void onLoading(boolean loading) {
        getFragmentDelegate().onLoading(loading);
    }

    @Override
    public DevxitActivity getDevxitActivity() {
        return (DevxitActivity) getActivity();
    }
}
