package com.baturamobile.utils.cluster;

import android.content.Context;

import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by vssnake on 04/09/2017.
 */

public class BaseClusterRenderer  extends DefaultClusterRenderer<BaseClusterItem> {

    public BaseClusterRenderer(Context context, com.google.android.gms.maps.GoogleMap map, ClusterManager<BaseClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }
}
