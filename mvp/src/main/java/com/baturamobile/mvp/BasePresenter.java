package com.baturamobile.mvp;

/**
 * Created by unai on 12/08/2016.
 */

public abstract class  BasePresenter<T extends BaseInteface> {

    protected T viewInterface;

    protected abstract void inject(T link);

    protected void onStart(){}

    protected void onStop(){}

    protected void onDestroy(){}

    protected void onResume(){}

    protected void onPause(){}
}
