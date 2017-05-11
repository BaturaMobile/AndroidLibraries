package com.baturamobile.design.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vssnake on 08/05/2017.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {

    private List<T> arrayItems;

    protected HolderClick<T> holderClick;

    BaseAdapter(){
        arrayItems = new ArrayList<>();
    }

    protected void addItems(List<T> arrayItems){
        this.arrayItems = arrayItems;
    }

    public void holderClick(HolderClick<T> holderClick){
        this.holderClick = holderClick;
    }

    @Override
    public void onBindViewHolder(BaseAdapter.BaseViewHolder holder, int position) {
        holder.bindView(arrayItems.get(position),position);
    }

    @Override
    public int getItemCount() {
        return arrayItems.size();
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder{

        T itemModel;
        int position;

        BaseViewHolder(View itemView) {
            super(itemView);
            setupView(itemView);
        }

        abstract void setupView(View view);

        void bindView(T model, int position) {
            this.itemModel = model;
            this.position = position;
        }

        abstract void refreshView();


    }

    interface HolderClick<T>{
        void onHolderClick(T item,int position);
    }
}
