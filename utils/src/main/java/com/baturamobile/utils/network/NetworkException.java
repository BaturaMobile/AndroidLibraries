package com.baturamobile.utils.network;

/**
 * Created by vssnake on 22/12/2017.
 */

/**
 * Deprecated use {@link RestException}
 */
@Deprecated
public class NetworkException extends Throwable {

    public int codeError;

    public NetworkException(int error){
        codeError  = error;
    }
}
