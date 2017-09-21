package com.vssnake.devxit.view;

import android.content.Intent;

/**
 * Created by vssnake on 21/09/2017.
 */

public interface CommonViewInterface {


    void  startActivityForResult(Intent intent, int requestCode);

    DevxitActivity getDevxitActivity();
}
