package com.baturamobile.mvp;

import android.content.Context;

/**
 * Created by unai on 17/08/2016.
 */

public interface BaseInteface {

    Context getContext();

    void loading(boolean loading);

    void onError(String error,Throwable throwable);
}
