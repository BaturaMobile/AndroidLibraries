package com.vssnake.devxit.observer;

/**
 * Created by vssnake on 27/02/2017.
 */

public interface ObserverInterface<T extends Object> {

    void onEvent(String event, T data);
}
