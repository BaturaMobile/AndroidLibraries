package com.baturamobile.mvp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.baturamobile.design.R;

/**
 * Created by unai on 12/08/2016.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements  BaseInteface{

    public abstract T getPresenter();


    protected ProgressDialog progress;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        progress = new ProgressDialog(getContext());
        progress.setTitle(R.string.loading);
        progress.setMessage(getContext().getString(R.string.please_wait));
        progress.setCancelable(false);

    }
    @Override
    public void onStart(){
        super.onStart();
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
    @CallSuper
    public void onError(String error,Throwable throwable){
        if (throwable != null){
            Utils.throwError(throwable);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
    }

    public void finish(){
        if (getActivity() != null){
            getActivity().finish();
        }
    }





}
