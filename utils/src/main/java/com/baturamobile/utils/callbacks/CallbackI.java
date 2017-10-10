package com.baturamobile.utils.callbacks;

/**
 * Created by vssnake on 26/10/2016.
 */

public interface CallbackI<T> {

    void onResponse(T dataResponse);

    void onError(int codeError, String stringError, Throwable ExceptionError);
}
