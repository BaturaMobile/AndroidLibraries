package com.vssnake.devxit.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.vssnake.devxit.executor.PostExecutionThread;
import com.vssnake.devxit.internal.di.components.ApplicationComponent;
import com.vssnake.devxit.internal.di.components.HasClientComponent;
import com.vssnake.devxit.internal.di.modules.ActivityModule;
import com.vssnake.devxit.modules.PopUPModule;
import com.vssnake.devxit.observer.ObserverController;
import com.vssnake.devxit.view.delegate.DevxitActivityDelegate;
import com.vssnake.devxit.view.delegate.DevxitActivityDelegateImpl;
import com.vssnake.devxit.view.delegate.DevxitDelegateCallback;

import javax.inject.Inject;

/**
 * Created by vssnake on 01/02/2017.
 */

public abstract  class DevxitActivity<V extends DevxitView, P extends DevxitPresenter<V>>
        extends AppCompatActivity implements DevxitDelegateCallback<V,P>,DevxitView,CommonViewInterface {

    public DevxitActivityDelegate<V,P> activityDelegate;

    @Inject
    ObserverController observerController;

    @Inject
    PopUPModule popUPModule;

    @Inject
    PostExecutionThread postExecutionThread;

    @SuppressWarnings("uncheked")
    public DevxitActivityDelegate<V,P> getActivityDelegate(){
        if (activityDelegate == null){
            initializeDependencieInjection();
            activityDelegate = new DevxitActivityDelegateImpl<>(this,observerController,postExecutionThread, popUPModule);
        }
        return activityDelegate;

    }

    /**
     * Get the Main Application component for dependency injection.
     *
     */
    public ApplicationComponent getApplicationComponent() {
        return ((DevxitApp)getApplication()).getApplicationComponent();
    }
    private ActivityModule activityModule;

    /**
     * Get activity module for dependencing injection
     *
     */
    protected ActivityModule getActivityModule(){
        if (activityModule == null){
            activityModule = new ActivityModule(this);
        }
        return activityModule;
    }

    protected <C> C getComponent(Class<C> componentClass){
        return componentClass.cast(((HasClientComponent<C>) this).getComponent());
    }

    protected boolean implementsComponent(Class clazz){
        return clazz.getClass().isAssignableFrom(HasClientComponent.class);
    }

    @NonNull
    @SuppressWarnings("uncheked")
    @Override
    public V getDevxitView() {
        return (V) this;
    }


    @Override
    public DevxitApp getDevxitApp(){
        return (DevxitApp) getApplication();
    }

    @Override public AppCompatActivity getViewActivity(){
        return this;
    }

    @Override
    public Context getContext(){
        return getBaseContext();
    }

    //region lifeCycle

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityDelegate().onCreate(savedInstanceState,this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        getActivityDelegate().onDestroy();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getActivityDelegate().onSaveInstanceState(outState);
    }

    @Override protected void onPause() {
        super.onPause();
        getActivityDelegate().onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        getActivityDelegate().onResume();
    }

    @Override protected void onStart() {
        super.onStart();
        getActivityDelegate().onStart();
    }

    @Override protected void onStop() {
        super.onStop();
        getActivityDelegate().onStop();
    }

    @Override protected void onRestart() {
        super.onRestart();
        getActivityDelegate().onRestart();
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        getActivityDelegate().onContentChanged();
    }

    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActivityDelegate().onPostCreate(savedInstanceState);
    }

    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getActivityDelegate().onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public void onLoading(boolean loading) {
        getActivityDelegate().onLoading(loading);
    }

    @Override
    public void onError(String title, String error) {
        getActivityDelegate().onError(title,error);
    }

    @Override
    public void onRetryError(String title, String error) {
        getActivityDelegate().onRetryError(title,error);
    }

    @Override
    public DevxitActivity<V, P> getDevxitActivity() {
        return this;
    }

}
