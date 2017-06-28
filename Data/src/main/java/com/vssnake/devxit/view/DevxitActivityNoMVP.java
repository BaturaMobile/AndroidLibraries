package com.vssnake.devxit.view;

/**
 * Created by vssnake on 21/02/2017.
 */

public abstract class DevxitActivityNoMVP extends DevxitActivity {
    @Override
    public DevxitPresenter getPresenter() {
        return null;
    }

    @Override
    public void initializeDependencieInjection() {

    }
}

