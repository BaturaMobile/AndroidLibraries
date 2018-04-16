package com.baturamobile.mvp.v3

import android.os.Build
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity


/**
 * Created by virtu on 6/12/2017.
 */

class MvpUtils() {



    companion object {

        const val ERROR_REQUEST_CODE = 1234
        const val SUCESS_ACTIVITY_RESULT = 1235




    }
}

fun AppCompatActivity.checkFinishMethod(){
    val upIntent = NavUtils.getParentActivityIntent(this)
    if (upIntent != null) {
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities()
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(this, upIntent)
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
    }
}


