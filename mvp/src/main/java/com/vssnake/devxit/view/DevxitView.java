package com.vssnake.devxit.view;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by vssnake on 01/02/2017.
 */

public interface DevxitView {

    AppCompatActivity getViewActivity();

    void onLoading(boolean loading);

    void onError(String title,String error);

    void onRetryError(String title,String error);
}
