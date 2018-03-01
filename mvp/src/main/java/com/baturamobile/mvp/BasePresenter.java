package com.baturamobile.mvp;


import android.support.annotation.Nullable;

/**
 * Created by unai on 12/08/2016.
 */

public abstract class  BasePresenter<T extends BaseInteface> {

    protected @Nullable T viewInterface;

    public void inject(T link){
        viewInterface = link;
    }

    protected void onStart(){}

    protected void onStop(){}

    protected void onDestroy(){
        viewInterface = null;

    }

    protected void onResume(){}

    protected void onPause(){}


}
