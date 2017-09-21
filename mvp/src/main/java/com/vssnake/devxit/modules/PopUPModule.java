package com.vssnake.devxit.modules;

import android.app.Activity;
import android.content.Intent;

import com.vssnake.devxit.view.CommonViewInterface;
import com.vssnake.devxit.view.delegate.DevxitDelegateCallback;

/**
 * Created by vssnake on 20/09/2017.
 */

public abstract class PopUPModule {

    public static final int POPUP_RETRY_ERROR_KEY = 9100;
    public static final int POPUP_GENERAL_ERROR_KEY = 9101;
    public static final int POPUP_REQUEST_KEY = 9102;
    public static final int POPUP_SUCCESS_KEY = 9103;

    public abstract Intent onSucessPopup(String title,String description,CommonViewInterface appCompatActivity);

    public abstract Intent onRetryErrorPopup(String title,String description,CommonViewInterface appCompatActivity);

    public abstract Intent onErrorPopup(String title,String description,CommonViewInterface appCompatActivity);


    public void onActivityResult(int requestCode, int resultCode, Intent data,DevxitDelegateCallback devxitDelegateCallback) {
        if (resultCode == Activity.RESULT_CANCELED){
            switch (requestCode){
                case POPUP_RETRY_ERROR_KEY:
                    devxitDelegateCallback.getPresenter().visible();
                    break;
            }
        }else{
            switch (requestCode){
                case POPUP_SUCCESS_KEY:
                    break;
                case POPUP_REQUEST_KEY:
                    break;
                case  POPUP_GENERAL_ERROR_KEY:
                    devxitDelegateCallback.getPresenter().onViewNeedFinishing();
                    break;
                case POPUP_RETRY_ERROR_KEY:
                    devxitDelegateCallback.getPresenter().onViewNeedFinishing();
                    break;
            }
        }

    }
}
