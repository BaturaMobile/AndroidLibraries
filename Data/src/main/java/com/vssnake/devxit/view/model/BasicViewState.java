package com.vssnake.devxit.view.model;

import com.vssnake.devxit.exceptions.ErrorBundle;

/**
 * Created by vssnake on 08/02/2017.
 */

public class BasicViewState {

    private ErrorBundle errorBundle;


    public boolean hasError(){
        return getErrorBundle() != null;
    }


    public ErrorBundle getErrorBundle() {
        return errorBundle;
    }

    public void setErrorBundle(ErrorBundle errorBundle) {
        this.errorBundle = errorBundle;
    }
}
