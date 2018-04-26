package com.baturamobile.aioistartcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class AIOISmartTagActivity extends AppCompatActivity implements  StartTagDelegateInterface {

    private static final String TAG = "AIOISmartTagActivity";

    SmartTagDelegate smartTagDelegate;

    ProgressBar progressBar;

    public abstract @LayoutRes Integer getLayout();

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(getLayout());

        progressBar = findViewById(R.id.taga_progress);

        smartTagDelegate = new SmartTagDelegate(this);

        smartTagDelegate.onCreate(getIntent());


    }

    @Override
    protected void onResume() {
        super.onResume();
        smartTagDelegate.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        smartTagDelegate.onPause();


    }


    @Override
    protected void onNewIntent(Intent intent){

        super.onNewIntent(intent);
        smartTagDelegate.onNewIntent(intent);

    }


    @Override
    public void nfcShowProgress(int percent) {
        if (progressBar != null){
            progressBar.setIndeterminate(false);
            progressBar.setProgress(percent);
        }

    }

    @Override
    public void nfcMaxChanged(int max) {
        if (progressBar != null){
            progressBar.setMax(max);
        }

    }

    @Override
    public void nfcVisibleProgress(int visibility) {
        if (progressBar != null){
            progressBar.setVisibility(visibility);
        }

    }

    @Override
    public void nfcSetIndeterminate(boolean indeterminate) {
            if (progressBar != null){
                progressBar.setIndeterminate(indeterminate);
            }
    }


}
