package com.baturamobile.design.adapter;

/**
 * Created by vssnake on 09/05/2017.
 */

public class ImageModel extends NoImageModel {

    private String imageURI;
    public ImageModel(String text,String imageURI) {
        super(text);
        this.imageURI = imageURI;
    }

    public String getImageURI() {
        return imageURI;
    }
}
