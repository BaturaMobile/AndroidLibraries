package com.baturamobile.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by vssnake on 27/06/2017.
 */

public class PoolManager {

    private static int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();


    // Sets the amount of time an idle thread waits until terminated
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> mQueue;

    ThreadPoolExecutor mThreadPool;

    // An object that manages Messages in a Thread
    private Handler mUIHandler;

    private PoolManager(){
        mQueue = new LinkedBlockingQueue<>();
        mThreadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES,       // Initial pool size
                NUMBER_OF_CORES,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mQueue);

        mThreadPool.prestartCoreThread();

        mUIHandler = new Handler(Looper.getMainLooper());
    }

    public void cancelAll(){


        mThreadPool.shutdownNow();
    }

    public void addToQueue(Runnable runnable){
        mQueue.add(runnable);
    }

    public void addToUiQueue(Runnable runnable){
        mUIHandler.post(runnable);
    }

    public static final PoolManager sPoolManager;

    static{
        sPoolManager = new PoolManager();

    }


}
