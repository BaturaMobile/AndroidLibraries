package com.baturamobile.utils.cluster;

import android.graphics.Bitmap;

import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by vssnake on 04/09/2017.
 */

public abstract class BaseClusterItem implements ClusterItem {

    protected Bitmap cachedBitmap;

    public float alpha = 1;

    private boolean enabled = true;


    protected abstract void  permuteResourceIcon();

    public Bitmap getCachedBitmap() {
        return cachedBitmap;
    }

    public boolean isEnabled()
    {
        return enabled;
    }
}
