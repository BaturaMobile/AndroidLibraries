package com.baturamobile.design.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baturamobile.design.BaturaTextView;
import com.baturamobile.design.R;

/**
 * Created by vssnake on 08/05/2017.
 */

public class NoImageAdapter extends BaseAdapter<NoImageModel> {

    @Override
    public NoImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.no_image_holder,parent,false);
        return new NoImageViewHolder(itemView);
    }

    private class NoImageViewHolder extends BaseViewHolder {

        BaturaTextView baturaTextView;
        TextView textView;
        AppCompatImageView imageView;

        NoImageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView(View view) {
            textView = (TextView) view.findViewById(R.id.nih_text);

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
        }


    }


}
