package com.baturamobile.utils;

import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by virtu on 8/30/2017.
 */

public class CustomPagerSnapHelper extends PagerSnapHelper {

    SnapSwipeInterface snapSwipeInterface;

    RecyclerView mRecycleView;

    public CustomPagerSnapHelper(SnapSwipeInterface snapSwipeInterface){
        this.snapSwipeInterface = snapSwipeInterface;
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {

        View view = super.findSnapView(layoutManager);

        if (view != null && snapSwipeInterface != null && mRecycleView != null){
            snapSwipeInterface.onRecycleViewSwipe(mRecycleView.getChildAdapterPosition(view));
        }
        return view;
    }

    @Override
    public void attachToRecyclerView(RecyclerView recyclerView){
        mRecycleView = recyclerView;
        super.attachToRecyclerView(recyclerView);
    }


    public interface SnapSwipeInterface{
        void onRecycleViewSwipe(int position);
    }
}
