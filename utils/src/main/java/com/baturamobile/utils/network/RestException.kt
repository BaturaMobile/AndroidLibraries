package com.baturamobile.utils.network

/**
 * Created by vssnake on 23/01/2018.
 */
class RestException(var codeError: Int, override val message : String) : Throwable(message)