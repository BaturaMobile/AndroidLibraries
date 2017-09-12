package com.vssnake.devxit.view.delegate;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by vssnake on 12/09/2017.
 */

public class DevxitLoadingDelegate {

    private Context mContext;
    private ProgressDialog mProgress;

    DevxitLoadingDelegate(Context context){
        if (context == null){
            throw new NullPointerException("Context is null");
        }

        this.mContext = context;

        mProgress = new ProgressDialog(mContext);
        mProgress.setTitle(com.baturamobile.design.R.string.loading);
        mProgress.setMessage(mContext.getString(com.baturamobile.design.R.string.please_wait));
        mProgress.setCancelable(false);
    }

    public void loading(boolean loading) {
        if (loading){
            mProgress.show();
        }else{
            mProgress.dismiss();
        }
    }

}
