
package com.vssnake.devxit.view;



import android.os.Handler;
import android.os.Looper;

import com.vssnake.devxit.executor.PostExecutionThread;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class UIThread implements PostExecutionThread {

  Handler handler;

  @Inject
  public UIThread() {
    handler = new Handler(Looper.getMainLooper());
  }


  @Override
  public void execute(Runnable command) {
    handler.post(command);
  }
}
