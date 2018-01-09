package com.baturamobile.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by vssnake on 09/01/2018.
 */

public abstract class BaseFragmentV2<T extends BasePresenterV2> extends BaseFragment<T> {



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onStart();
        injectDI();
        if (getPresenter() != null){
            getPresenter().onStart();
        }
    }

    public abstract void injectDI();

}
