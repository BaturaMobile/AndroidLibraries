//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baturamobile.utils.callbacks


import android.support.annotation.CallSuper
import com.baturamobile.utils.log.LogStaticV2

open class CallbackV2<in T> {

    open fun onResponse(dataResponse: T) {}

    @CallSuper
    open fun onError(codeError: Int?, stringError: String?, exceptionError: Throwable?) {
        LogStaticV2.logInterfaceV2?.crashError(stringError,exceptionError)
    }
}
