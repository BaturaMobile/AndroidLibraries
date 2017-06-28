package com.vssnake.devxit.view;

/**
 * Created by vssnake on 08/02/2017.
 */

public interface DevxitViewState<T> extends DevxitView {

    void onViewStateChange(T newState);
}
