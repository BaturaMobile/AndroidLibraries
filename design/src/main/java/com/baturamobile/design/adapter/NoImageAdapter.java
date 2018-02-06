package com.baturamobile.design.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baturamobile.design.BaturaTextView;
import com.baturamobile.design.R;

/**
 * Created by vssnake on 08/05/2017.
 */

public class NoImageAdapter<M extends NoImageModel> extends BaseAdapter<NoImageModel> {

    @Override
    public NoImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.no_image_holder,null,false);
        return new NoImageViewHolder(itemView);
    }

    private class NoImageViewHolder extends BaseViewHolder {

        BaturaTextView baturaTextView;
        AppCompatImageView tick_image;



        NoImageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView(View view) {
            tick_image = view.findViewById(R.id.nih_tick);
            baturaTextView = view.findViewById(R.id.nih_text);

            view.findViewById(R.id.nih_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holderClick != null) {
                        holderClick.onHolderClick(getItemModel(), position);
                    }
                }
            });

        }

        @Override
        public void refreshView() {
            baturaTextView.setText(getItemModel().getText());

            if (selectedItem != null && selectedItem.getText().equalsIgnoreCase(getItemModel().getText())){
                tick_image.setVisibility(View.VISIBLE);
                baturaTextView.setTextColor(ContextCompat.getColor(context,R.color.blue));
            }else{
                tick_image.setVisibility(View.INVISIBLE);
                baturaTextView.setTextColor(ContextCompat.getColor(context,android.R.color.black));
            }
        }


    }


}
