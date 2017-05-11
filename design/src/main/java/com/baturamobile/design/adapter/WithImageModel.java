package com.baturamobile.design.adapter;

/**
 * Created by vssnake on 08/05/2017.
 */

public class WithImageModel extends NoImageModel{

    private String imageUri;

    public WithImageModel(String imageUri,String text){
        super(text);
        this.imageUri = imageUri;

    }

    public String getImageUri() {
        return imageUri;
    }
}
