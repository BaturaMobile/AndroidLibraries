package com.baturamobile.mvp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.baturamobile.design.adapter.BaseAdapter;
import com.baturamobile.design.adapter.ImageAdapter;
import com.baturamobile.design.adapter.NoImageAdapter;
import com.baturamobile.design.adapter.NoImageModel;
import com.baturamobile.mvp.BasePresenter;
import com.baturamobile.mvp.BaseRecycleViewFragment;

import java.util.ArrayList;

/**
 * Created by vssnake on 03/07/2017.
 */

public class ListSelectableFragment<M extends NoImageModel, A extends BaseAdapter<M>>
        extends BaseRecycleViewFragment<A,BasePresenter,M>{


    public static final String KEY_RESULT = "keyResult";

    public static final String KEY_MODEL_LIST = "modelList";
    public static final String KEY_MODEL_SELECTED = "modelSelected";

    public static final String KEY_TYPE_LIST = "keyTypeList";

    public static final int NO_IMAGE_ADAPTER = 0;
    public static final int IMAGE_ADAPTER = 1;

    ArrayList<M> modelList;
    M modelSelected;
    int typeList;


    A adapter;

    public ListSelectableFragment(){

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MODEL_LIST,modelList);
        bundle.putSerializable(KEY_MODEL_SELECTED,modelSelected);
        bundle.putInt(KEY_TYPE_LIST,typeList);

        setArguments(bundle);

    }

    public static <M extends NoImageModel, A extends BaseAdapter<M>>ListSelectableFragment<M,A> instance(
            ArrayList<M> modelList,M modelSelected,int typeList){

        ListSelectableFragment<M,A> selectableFragment = new ListSelectableFragment<>();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MODEL_LIST,modelList);
        bundle.putSerializable(KEY_MODEL_SELECTED,modelSelected);
        bundle.putInt(KEY_TYPE_LIST,typeList);

        selectableFragment.setArguments(bundle);

        return selectableFragment;


    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public A getAdapter() {
        if (adapter == null){
            switch (typeList){
                case NO_IMAGE_ADAPTER:
                    adapter = (A) new NoImageAdapter<>();
                    break;
                case IMAGE_ADAPTER:
                    adapter = (A) new ImageAdapter<>();
                    break;
            }
        }
        return adapter;
    }


    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        formatArguments(getArguments());
        super.onViewCreated(view,savedInstanceState);

    }

    private void formatArguments(Bundle arguments){
        modelList = (ArrayList<M>) arguments.getSerializable(KEY_MODEL_LIST);
        modelSelected = (M) arguments.getSerializable(KEY_MODEL_SELECTED);
        typeList = arguments.getInt(KEY_TYPE_LIST,NO_IMAGE_ADAPTER);

        getAdapter().addItems(modelList,modelSelected);
    }

    @Override
    public void onHolderClick(M item, int position) {
        Intent intent = new Intent();
        intent.putExtra(KEY_MODEL_SELECTED,item);

        getActivity().setResult(Activity.RESULT_OK,intent);
        getActivity().finish();
    }
}
