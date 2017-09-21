package com.vssnake.devxit.view;

import android.support.annotation.UiThread;

/**
 * The base interface for each mvp presenter.
 *
 * <p>
 * Mosby assumes that all interaction (i.e. updating the View) between Presenter and View is
 * executed on android's main UI thread.
 * </p>
 *
 * @author Hannes Dorfmann
 */

public interface DevxitPresenter<V extends DevxitView> {

    /**
     * Attach the view to the presenter
     * @param view to attach
     */
    @UiThread
    void attachView(V view);

    /**
     * Will be called if the view has been destroyed. Typically this method will be invoked from
     * <code>Activity.detachView()</code> or <code>Fragment.onDestroyView()</code>
     */
    @UiThread
    void detachView();

    /**
     * Will be called when the Is need to load the data of the screen
     */
    @UiThread
    void visible();

    /**
     * Will be called when is necessary to close the view
     */
    @UiThread
    void onViewNeedFinishing();
}
