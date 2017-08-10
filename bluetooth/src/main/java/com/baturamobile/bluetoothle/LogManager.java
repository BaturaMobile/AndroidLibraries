package com.baturamobile.bluetoothle;

import android.util.Log;

/**
 * Created by unai on 19/08/2016.
 */

public class LogManager {
    public static void d (String TAG, String msg){
        Log.d(TAG,msg);
    }

    public static void e(String TAG, String msg){ Log.e(TAG,msg);}
}


