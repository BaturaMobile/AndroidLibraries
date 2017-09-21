package com.vssnake.devxit.view;

import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

/**
 * Created by vssnake on 07/02/2017.
 */
public abstract class DevxitBasePresenter<V extends DevxitView> implements DevxitPresenter<V> {

    private WeakReference<V> viewWeakReference;

    @Override
    @UiThread
    public void attachView(V view) {
        viewWeakReference = new WeakReference<>(view);
        visible();
    }

    @Override
    @UiThread
    public void detachView() {
        if (viewWeakReference != null) {
            viewWeakReference.clear();
            viewWeakReference = null;
        }
        finish();
    }

    /**
     * Check if a view is attached. You should call this method before {@link #getView()}
     */
    public boolean isViewAttached(){ return viewWeakReference != null
            &&  viewWeakReference.get() != null;
    }

    /**
     * Get attached view. You should call this method before {@link #isViewAttached()}
     * @return <code>null</code>,if view is not attached, otherwise return the current view
     */
    public V getView(){
        return isViewAttached() ? viewWeakReference.get() : null;
    }

    /**
     * This method is being called when the view is finishing the lifeCycle.
     */
    public abstract void finish();

    /**
     * This method is called when you should update the view again. There are two
     * casuistic why I call this function.
     * The first is when you create the view first time and the other is for refresh the view
     * (Keep in mind that)
     */
    public abstract void visible();

    /**
     * Will be override when is necessary to close the view
     */
    @Override
    public void onViewNeedFinishing() {

    }
}
