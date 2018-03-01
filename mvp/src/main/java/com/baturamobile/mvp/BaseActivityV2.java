package com.baturamobile.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by vssnake on 09/01/2018.
 */

public abstract class BaseActivityV2<T extends BasePresenterV2> extends BaseActivity<T> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDI();
        if (getPresenter() != null){
            getPresenter().onCreate();
        }
    }

    public abstract void injectDI();
}
