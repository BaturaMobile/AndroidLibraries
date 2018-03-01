package com.baturamobile.design.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vssnake on 08/05/2017.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {

    private List<T> arrayItems;

    T selectedItem;

    protected HolderClick<T> holderClick;

    public BaseAdapter(){
        arrayItems = new ArrayList<>();
    }

    public void addItems(List<T> arrayItems,T selectedItem){
        this.arrayItems = arrayItems;
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
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

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private T itemModel;
        public int position;

        Context context;

        public BaseViewHolder(View itemView) {
            super(itemView);
            setupView(itemView);
            context = itemView.getContext();
        }

        public abstract void setupView(View view);

        void bindView(T model, int position) {
            this.itemModel = model;
            this.position = position;

            refreshView();
        }

        public abstract void refreshView();


        public T getItemModel() {
            return itemModel;
        }

        @Override
        public void onClick(View v) {

            if (holderClick != null){
                holderClick.onHolderClick(itemModel,position);
            }
        }
    }

    public interface HolderClick<T>{
        void onHolderClick(T item,int position);
    }
}
