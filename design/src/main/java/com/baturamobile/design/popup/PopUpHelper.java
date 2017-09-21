package com.baturamobile.design.popup;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;


/**
 * Created by unai on 08/08/2016.
 */
public class PopUpHelper {
    public static Intent createPopupIntent(Context context,

                                           @NonNull int typePopUP,
                                           @Nullable int mainImageSrc,
                                           @Nullable int backgroundColorImage,
                                           String titleText,
                                           String description1,
                                           @Nullable String description2,
                                           String buttonText,
                                           String buttonText2,
                                           int backgroundColorID,

                                           int fontColorID){

        Intent intent = new Intent(context,PopUpScreen.class);



        intent.putExtra(PopUpScreen.KEY_TYPE_POP_UP,typePopUP);
        intent.putExtra(PopUpScreen.KEY_MAIN_IMAGE_SRC,mainImageSrc);
        intent.putExtra(PopUpScreen.KEY_MAIN_IMAGE_BACKGROUND_COLOR,backgroundColorImage);
        intent.putExtra(PopUpScreen.KEY_MAIN_TITLE_TEXT,titleText);
        intent.putExtra(PopUpScreen.KEY_DESCRIPTION_1_TEXT,description1);
        intent.putExtra(PopUpScreen.KEY_DESCRIPTION_2_TEXT,description2);
        intent.putExtra(PopUpScreen.KEY_BUTTON_1_TEXT,buttonText);
        intent.putExtra(PopUpScreen.KEY_BUTTON_2_TEXT,buttonText2);
        intent.putExtra(PopUpScreen.KEY_BACKGROUND_COLOR_ID, ContextCompat.getColor(context,backgroundColorID));
        intent.putExtra(PopUpScreen.KEY_FONTS_COLOR_ID, ContextCompat.getColor(context,fontColorID));

        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

        return intent;

    }

    public  static Intent createPopupIntent(Context context,

                                            @NonNull int typePopUP,
                                            @Nullable int mainImageSrc,
                                            String titleText,
                                            String description1,
                                            @Nullable String description2,
                                            String buttonText,
                                            String buttonText2,
                                            int backgroundColorID,
                                            int fontColorID,
                                            Intent okIntent){

        Intent intent = new Intent(context,PopUpScreen.class);



        intent.putExtra(PopUpScreen.KEY_TYPE_POP_UP,typePopUP);
        intent.putExtra(PopUpScreen.KEY_MAIN_IMAGE_SRC,mainImageSrc);
        intent.putExtra(PopUpScreen.KEY_MAIN_TITLE_TEXT,titleText);
        intent.putExtra(PopUpScreen.KEY_DESCRIPTION_1_TEXT,description1);
        intent.putExtra(PopUpScreen.KEY_DESCRIPTION_2_TEXT,description2);
        intent.putExtra(PopUpScreen.KEY_BUTTON_1_TEXT,buttonText);
        intent.putExtra(PopUpScreen.KEY_BUTTON_2_TEXT,buttonText2);
        intent.putExtra(PopUpScreen.KEY_BACKGROUND_COLOR_ID, ContextCompat.getColor(context,backgroundColorID));
        intent.putExtra(PopUpScreen.KEY_FONTS_COLOR_ID, ContextCompat.getColor(context,fontColorID));

        intent.putExtra(PopUpScreen.KEY_INTENT_OK,okIntent);

        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

        return intent;

    }








}
