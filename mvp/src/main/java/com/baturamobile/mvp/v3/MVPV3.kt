package com.baturamobile.mvp.v3

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.view.MenuItem
import com.baturamobile.mvp.BaseActivityV2
import com.baturamobile.mvp.BaseFragmentV2
import com.baturamobile.mvp.BaseInteface
import com.baturamobile.mvp.BasePresenterV2
import java.lang.ref.WeakReference

/**
 * Created by vssnake on 20/02/2018.
 */

abstract class BaseActivityV3<T : BasePresenterV3<BaseContractV3>, out D : ViewDelegateV3> : BaseActivityV2<T>(), BaseContractV3 {

    private lateinit var viewDelegateV3: D

    abstract fun injectDelegate() : D

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDelegateV3 = injectDelegate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun loading(loading: Boolean) {
        viewDelegateV3.loading(loading)
    }

    override fun onError(error: String?, throwable: Throwable?) {
        super.onError(error, throwable)
        var stringError: String? = null
        if (error != null && !error.isEmpty()) {
            stringError = error
        } else if (throwable != null) {
            stringError = throwable.message
        }
        viewDelegateV3.launchError(stringError)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item != null && item.itemId == android.R.id.home) {
            checkFinishMethod()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        checkFinishMethod()
    }

    private fun checkFinishMethod() {
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
}

abstract class BaseFragmentV3<T : BasePresenterV3<BaseContractV3>, D : ViewDelegateV3> : BaseFragmentV2<T>(), BaseContractV3 {

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        viewDelegateV3 = injectDelegate()
    }

    private lateinit var viewDelegateV3: D

    abstract fun injectDelegate() : D


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun loading(loading: Boolean) {
        viewDelegateV3.loading(loading)
    }
}


abstract class BasePresenterV3<T : BaseContractV3> : BasePresenterV2<T>(){
    abstract fun loadData()

    @CallSuper
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MvpConstants.ERROR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //loadData();
        }
        if (requestCode == MvpConstants.SUCESS_ACTIVITY_RESULT && resultCode == Activity.RESULT_OK) {
            if (viewInterface != null) {
                viewInterface!!.finish()
            }
        }
    }


}

interface BaseContractV3 : BaseInteface{
    fun onSuccess()
}

abstract class ViewDelegateV3{

    var weakActivity : WeakReference<Activity>? = null
        private set

    fun injectActivity(activity: Activity){
        weakActivity = WeakReference(activity)
    }
    abstract fun loading(loading: Boolean)
    abstract fun launchError(stringError: String?)
}