package com.baturamobile.mvp;

/**
 * Created by vssnake on 09/01/2018.
 */

public abstract class BaseActivityV2<T extends BasePresenterV2> extends BaseActivity<T> {

    @Override
    public void onStart() {
        super.onStart();
        injectDI();
        if (getPresenter() != null){
            getPresenter().onStart();
        }
    }

    public abstract void injectDI();
}
