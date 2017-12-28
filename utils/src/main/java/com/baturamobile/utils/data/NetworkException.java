package com.baturamobile.utils.data;

/**
 * Created by vssnake on 22/12/2017.
 */

public class NetworkException extends Throwable {

    public int codeError;

    public NetworkException(int error){
        codeError  = error;
    }
}
