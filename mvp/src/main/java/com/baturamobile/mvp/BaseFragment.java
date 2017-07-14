package com.baturamobile.mvp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.baturamobile.design.R;

/**
 * Created by unai on 12/08/2016.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements  BaseInteface{

    public abstract T getPresenter();


    protected ProgressDialog progress;
    @Override
    public void onStart(){
        super.onStart();
        progress = new ProgressDialog(getContext());
        progress.setTitle(R.string.loading);
        progress.setMessage(getContext().getString(R.string.please_wait));
        progress.setCancelable(false);
        if (getPresenter() != null){
            getPresenter().onStart();
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        if (getPresenter() != null){
            getPresenter().onStop();
        }
    }
    @Override
    public void onDetach(){
        super.onDetach();
        if (getPresenter() != null){
            getPresenter().onDestroy();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void loading(boolean loading){
        if (loading){
            progress.show();
        }else{
            progress.dismiss();
        }
    }

    @Override
    public void onError(String error,Throwable throwable){
        if (throwable != null){
            Utils.throwError(throwable);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }





}
