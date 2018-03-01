package com.baturamobile.utils.callbacks;

import org.jetbrains.annotations.NotNull;

/**
 * Created by vssnake on 26/10/2016.
 */

public abstract class CallbackC<T> implements CallbackI<T> {
    @Override
    public void onResponse(@NotNull T dataResponse) {}

    @Override
    public void onError(int codeError, String stringError, Throwable ExceptionError) {}
}
