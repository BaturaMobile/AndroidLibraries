package com.baturamobile.design.recycleview;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baturamobile.design.BaturaTextView;
import com.baturamobile.design.R;
import com.baturamobile.design.adapter.BaseAdapter;
import com.baturamobile.mvp.BaseFragment;
import com.baturamobile.mvp.BasePresenter;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public abstract class BaseRecycleViewFragment<T extends BaseAdapter,P extends BasePresenter,M>
        extends BaseFragment<P> implements BaseRecycleViewView<M>, BaseAdapter.HolderClick<M> {

    RecyclerView recyclerView;

    protected AppCompatImageView emptyImage;

    protected BaturaTextView emptyText;


    public BaseRecycleViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_rv,container);

        recyclerView = (RecyclerView) view.findViewById(R.id.frv_rv);
        emptyImage = (AppCompatImageView) view.findViewById(R.id.frv_empty_image);
        emptyText = (BaturaTextView) view.findViewById(R.id.frv_empty_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getAdapter().holderClick(this);
        recyclerView.setAdapter(getAdapter());

        return view;

    }

    public abstract T getAdapter();



    @Override
    public void showCards(List<M> receiptModelList) {

        recyclerView.setVisibility(View.VISIBLE);
        getAdapter().addItems(receiptModelList);

        emptyImage.setVisibility(View.GONE);
        emptyText.setVisibility(View.GONE);
    }

    @Override
    @CallSuper
    public void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
