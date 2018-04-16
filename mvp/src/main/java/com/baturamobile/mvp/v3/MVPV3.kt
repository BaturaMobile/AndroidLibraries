package com.baturamobile.mvp.v3

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.baturamobile.mvp.v3.MvpUtils
import java.lang.ref.WeakReference


/**
 * Created by vssnake on 20/02/2018.
 */

abstract class BaseActivityV3 : AppCompatActivity(), com.baturamobile.mvp.v3.BaseContractV3{

    var dataBundle : Bundle? = null

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        dataBundle?.let {
            outState?.putAll(dataBundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras != null) {
            dataBundle = intent.extras
        } else if (savedInstanceState != null) {
            dataBundle = savedInstanceState
        }
    }

    open fun getViewDelegate() : com.baturamobile.mvp.v3.ViewDelegateV3?{
        return  null
    }

    override fun getContext(): Context {
        return this
    }

    open fun getActivityDelegate() : com.baturamobile.mvp.v3.ActivityDelegateV3?{
        return null
    }

    open fun setupViews(){}


    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        setupViews()
        getViewDelegate()?.currentMainContainerSet(findViewById<View>(android.R.id.content))
    }


    override fun attachBaseContext(newBase: Context?) {
        if (getActivityDelegate() != null){
            super.attachBaseContext(getActivityDelegate()!!.attachBaseContext(newBase!!))
        }else{
            super.attachBaseContext(newBase)
        }

    }

    override fun loading(loading: Boolean) {
        getViewDelegate()?.loading(loading)
    }

    override fun onError(error: String?, throwable: Throwable?) {
        var stringError: String? = null
        if (error != null && !error.isEmpty()) {
            stringError = error
        } else if (throwable != null) {
            stringError = throwable.message
        }
        getViewDelegate()?.launchError(stringError)
    }

    override fun onSuccess() {
        getViewDelegate()?.onSuccess()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item != null && item.itemId == android.R.id.home) {
            checkFinishMethod()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    }

    override fun getBundle(): Bundle? {
        return if (dataBundle != null){
            dataBundle
        }else{
            intent.extras
        }
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

    override fun getArguments(): Bundle? {
        return null
    }
}

abstract class MVPActivityV3<out T : com.baturamobile.mvp.v3.BasePresenterV3<out com.baturamobile.mvp.v3.BaseContractV3>> : com.baturamobile.mvp.v3.BaseActivityV3(),com.baturamobile.mvp.v3.BaseContractV3 {
    override fun getContext(): Context {
        return this
    }

    init{
        injectDI()
        getPresenter()?.injectLifeCycle(lifecycle)
        getViewDelegate()?.injectLifeCycle(lifecycle)
        getActivityDelegate()?.injectLifeCycle(lifecycle)
    }

    abstract fun injectDI()

    abstract fun getPresenter(): T?

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getPresenter()?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

abstract class BaseFragmentV3 : Fragment(), com.baturamobile.mvp.v3.BaseContractV3{


    open fun setupViews(){}

    override fun finish() {
        activity?.finish()
    }

    abstract fun getFragmentDelegate() : com.baturamobile.mvp.v3.FragmentDelegateV3?


    open fun getViewDelegate() : com.baturamobile.mvp.v3.ViewDelegateV3?{
        return  null
    }

    override fun onError(error: String?, throwable: Throwable?) {
        var stringError: String? = null
        if (error != null && !error.isEmpty()) {
            stringError = error
        } else if (throwable != null) {
            stringError = throwable.message
        }
        getViewDelegate()?.launchError(stringError)
    }

    override fun loading(loading: Boolean) {
        getViewDelegate()?.loading(loading)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentDelegate()?.injectLifeCycle(lifecycle)
        getViewDelegate()?.injectLifeCycle(lifecycle)
        if (view is ViewGroup) {
            getViewDelegate()?.currentMainContainerSet(view)
        }
        setupViews()

    }

    override fun getBundle(): Bundle? {
        activity?.let {
            if (activity is com.baturamobile.mvp.v3.BaseActivityV3){
                return (activity as com.baturamobile.mvp.v3.BaseActivityV3).getBundle()
            }
        }
        return null
    }

}

abstract class MVPFragmentV3<out T : com.baturamobile.mvp.v3.BasePresenterV3<out com.baturamobile.mvp.v3.BaseContractV3>> : com.baturamobile.mvp.v3.BaseFragmentV3(), com.baturamobile.mvp.v3.BaseContractV3 {


    abstract fun injectDI()

    abstract fun getPresenter(): T?

    override fun onSuccess() {
        getViewDelegate()?.onSuccess()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getPresenter()?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injectDI()
        getFragmentDelegate()?.injectLifeCycle(lifecycle)
        super.onViewCreated(view, savedInstanceState)
        getPresenter()?.injectLifeCycle(lifecycle)
    }

}


abstract class MVPViewGroup @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        inject()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    abstract fun inject()
}

abstract class MVPViewgroupBasePresenter<out T : com.baturamobile.mvp.v3.BaseViewGroupContract>(view : T){

    private val attachedViewGroup = WeakReference(view)

    fun isAttached() : Boolean{
        return attachedViewGroup.get() != null
    }

    fun getView() : T?{
        return attachedViewGroup.get()
    }


    fun onDetach(){}

    fun onAttach(){}
}



interface BaseViewGroupContract

abstract class BasePresenterV3<T : com.baturamobile.mvp.v3.BaseContractV3> () : LifecycleObserver{
    abstract fun loadData()


    var lifeCycle : Lifecycle? = null

    fun injectLifeCycle(lifeCycle: Lifecycle){
        this.lifeCycle = lifeCycle
        this.lifeCycle!!.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(){
        lifeCycle?.removeObserver(this)
        viewInterface = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause(){}

    private var viewInterface: WeakReference<T?>? = null

    fun getViewContract() : T?{
        return viewInterface?.get()
    }

     fun inject(contract : T){
         viewInterface = WeakReference(contract)
    }





    @CallSuper
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MvpUtils.ERROR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //loadData();
        }
        if (requestCode == MvpUtils.SUCESS_ACTIVITY_RESULT && resultCode == Activity.RESULT_OK) {
            getViewContract()?.finish()
        }
    }


}

interface BaseContractV3{
    fun onSuccess()

    fun getContext(): Context?

    fun loading(loading: Boolean)

    fun onError(error: String?, throwable: Throwable?)

    fun finish()

    fun getBundle() : Bundle?

    fun getArguments() : Bundle?
}

abstract class ViewDelegateV3(val activity:  WeakReference<Activity>) : LifecycleObserver{

    var mainContainer : View? = null
        private set

    abstract fun loading(loading: Boolean)
    abstract fun launchError(stringError: String?)
    @CallSuper
    open fun currentMainContainerSet(view:View?){
        mainContainer = view
    }
    abstract fun onSuccess()

    var lifeCycle : Lifecycle? = null

    fun injectLifeCycle(lifecycle: Lifecycle) {
        this.lifeCycle = lifecycle
        this.lifeCycle!!.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(){
        lifeCycle?.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause(){}

}

abstract class ActivityDelegateV3(val activity:  WeakReference<Activity>) : LifecycleObserver{

    var lifeCycle : Lifecycle? = null

    fun injectLifeCycle(lifecycle: Lifecycle) {
        this.lifeCycle = lifecycle
        this.lifeCycle!!.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(){
        lifeCycle?.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause(){}

    open fun attachBaseContext(newBase: Context) : Context{
        return newBase
    }
}

abstract class FragmentDelegateV3(val activity:  WeakReference<Activity>) : LifecycleObserver{

    var lifeCycle : Lifecycle? = null

    fun injectLifeCycle(lifecycle: Lifecycle) {
        this.lifeCycle = lifecycle
        this.lifeCycle!!.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(){
        lifeCycle?.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause(){}

    open fun attachBaseContext(newBase: Context) : Context{
        return newBase
    }
}