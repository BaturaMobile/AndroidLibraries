package com.vssnake.devxit.view.delegate;

import android.os.Parcelable;

import com.vssnake.devxit.view.DevxitPresenter;
import com.vssnake.devxit.view.DevxitView;

/**
 * Created by vssnake on 07/02/2017.
 */

public class DevxitViewGroupDelegateImpl< V extends DevxitView,P extends DevxitPresenter<V>>
        implements DevxitViewGroupDelegate {

    DevxitDelegateCallback<V,P> devxitDelegateCallback;

    public DevxitViewGroupDelegateImpl(DevxitDelegateCallback<V,P> delegateCallback){
        if (delegateCallback == null){
            throw new NullPointerException("DevxitDelegateCallback is null!");
        }
        this.devxitDelegateCallback = delegateCallback;
    }

    @Override
    public void onAttachedToWindow() {
        //TODO to implement
    }

    @Override
    public void onDetachedFromWindow() {
        //TODO to implement
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //TODO to implement
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //TODO to implement
        return null;
    }
}
