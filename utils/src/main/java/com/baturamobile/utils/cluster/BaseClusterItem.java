package com.baturamobile.utils.cluster;

import android.graphics.Bitmap;

import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by vssnake on 04/09/2017.
 */

public abstract class BaseClusterItem implements ClusterItem {

    protected Bitmap cachedBitmap;


    protected abstract void  permuteResourceIcon();

    public Bitmap getCachedBitmap() {
        return cachedBitmap;
    }
}
