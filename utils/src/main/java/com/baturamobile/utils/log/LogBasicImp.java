package com.baturamobile.utils.log;

import android.util.Log;

/**
 * Created by Iratxe on 01/12/2017.
 */

public class LogBasicImp implements  LogInterface {
    @Override
    public void d(String TAG, String message) {
        Log.d(TAG,message);
    }
}
