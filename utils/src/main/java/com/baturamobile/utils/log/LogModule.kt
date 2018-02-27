package com.baturamobile.utils.log

/**
 * Created by vssnake on 21/02/2018.
 */
interface LogInterfaceV2{

    fun log(priority: Int, TAG : String,message: String)

    fun crashError(message: String?,exception:Throwable?)

}

object  LogStaticV2{
    var logInterfaceV2 : LogInterfaceV2? = null
}