package com.vssnake.devxit.utils;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.baturamobile.data.R;


/**
 * Created by vssnake on 22/02/2017.
 */

public class FragmentAnim {


    public static void replaceAnimDefaultTransition(FragmentManager fragmentManager,
                                                    @IdRes int containerId,
                                                    Fragment fragment){
        FragmentTransaction fragmentTransaction  = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commit();
    }
}
