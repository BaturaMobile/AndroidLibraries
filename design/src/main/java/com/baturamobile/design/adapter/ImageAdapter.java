package com.baturamobile.design.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baturamobile.design.BaturaTextView;
import com.baturamobile.design.R;
import com.squareup.picasso.Picasso;

/**
 * Created by vssnake on 09/05/2017.
 */

public class ImageAdapter<M extends WithImageModel> extends BaseAdapter<WithImageModel> {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_holder,parent,false);
        return new ImageAdapter.ImageViewHolder(itemView);
    }

    private class ImageViewHolder extends BaseViewHolder {

        BaturaTextView baturaTextView;
        AppCompatImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setupView(View view) {
            baturaTextView = view.findViewById(R.id.ih_text);
            imageView = view.findViewById(R.id.ih_image);
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
            Picasso.with(imageView.getContext()).load( getItemModel().getImageUri()).into(imageView);
        }



    }
}
