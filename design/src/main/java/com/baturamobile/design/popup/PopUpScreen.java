package com.baturamobile.design.popup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baturamobile.design.BaturaTextView;
import com.baturamobile.design.R;


public class PopUpScreen extends AppCompatActivity {

    public static final String KEY_TYPE_POP_UP = "keyPopUp";
    public static final String KEY_MAIN_IMAGE_SRC = "keyImageSrc";
    public static final String KEY_MAIN_IMAGE_BACKGROUND_COLOR = "keyImageBackground";
    public static final String KEY_MAIN_TITLE_TEXT = "keyMainTitleText";
    public static final String KEY_DESCRIPTION_1_TEXT = "keyDescription1";
    public static final String KEY_DESCRIPTION_2_TEXT = "keyDescription2";
    public static final String KEY_BUTTON_1_TEXT = "keyButton1";
    public static final String KEY_BUTTON_2_TEXT = "keyButton2";
    public static final String KEY_BACKGROUND_COLOR_ID = "keyBackgroundColor";
    public static final String KEY_FONTS_COLOR_ID = "keyFontColor";
    public static final String KEY_BACK_DESACTIVATE = "keyBackActivate";

    public static final String KEY_INTENT_OK = "intentOK";


    public static final int BUTTONS_VERTICALY = 0;
    public static final int BUTTONS_HORIZONTALY = 1;

    public static final String OPTION_KEY = "keyOptionsClick";
    public static final int OPTIONS1_CLICKED = 0;
    public static final int OPTIONS2_CLICKED = 1;

    public int typePopUp;
    public int imageResourceID;
    public String titleText;
    public String description1;
    public String description2;
    public String button1Text;
    public String button2Text;
    public int backgroundColorID;
    public int getBackgroundColorImageID;
    public int fontsColorID;
    public boolean isBackDesactivate;
    Intent okayIntent;

    LinearLayout mainContainer;
    AppCompatImageView mainImage;
    TextView textViewTitle;
    TextView textViewDescription1;
    TextView textViewDescription2;
    LinearLayout buttonsContainer;
    BaturaTextView style1Button1;
    BaturaTextView style1Button2;
    BaturaTextView style2Button1;
    BaturaTextView style2Button2;



    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.popup_screen);


        mainContainer = findViewById(R.id.popup_main_container);
        mainImage = findViewById(R.id.popup_main_image);
        textViewTitle = findViewById(R.id.popup_title);
        textViewDescription1 = findViewById(R.id.popup_descip);
        textViewDescription2 = findViewById(R.id.popup_descip_2);
        buttonsContainer = findViewById(R.id.popup_style1_con);
        style1Button1 = findViewById(R.id.popup_style1_btn1);
        style1Button2 = findViewById(R.id.popup_style1_btn2);
        style2Button1 = findViewById(R.id.popup_style2_btn1);
        style2Button2 = findViewById(R.id.popup_style2_btn2);

        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            typePopUp = extras.getInt(KEY_TYPE_POP_UP);
            imageResourceID = extras.getInt(KEY_MAIN_IMAGE_SRC);
            titleText = extras.getString(KEY_MAIN_TITLE_TEXT);
            description1 = extras.getString(KEY_DESCRIPTION_1_TEXT);
            description2 = extras.getString(KEY_DESCRIPTION_2_TEXT);
            button1Text = extras.getString(KEY_BUTTON_1_TEXT);
            button2Text = extras.getString(KEY_BUTTON_2_TEXT);
            backgroundColorID = extras.getInt(KEY_BACKGROUND_COLOR_ID);
            fontsColorID = extras.getInt(KEY_FONTS_COLOR_ID);
            isBackDesactivate = extras.getBoolean(KEY_BACK_DESACTIVATE);
            okayIntent = extras.getParcelable(KEY_INTENT_OK);
            getBackgroundColorImageID = extras.getInt(KEY_MAIN_IMAGE_BACKGROUND_COLOR);
        }

        style1Button1.setOnClickListener(style1Button1clickListener);
        style1Button2.setOnClickListener(style1Button2clickListener);
        style2Button1.setOnClickListener(style1Button1clickListener);
        style2Button2.setOnClickListener(style2Button2clickListener);

        renderViews();
    }


    private void renderViews(){

       mainContainer.setBackgroundColor(backgroundColorID);

        style1Button1.setTextColor(backgroundColorID);

        if (imageResourceID > 0){
            mainImage.setImageResource(imageResourceID);
            mainImage.setVisibility(View.VISIBLE);
        }else{
            mainImage.setVisibility(View.GONE);
        }

        if (getBackgroundColorImageID > 0){
            mainImage.setBackgroundResource(getBackgroundColorImageID);
        }

        textViewTitle.setText(titleText);
        textViewDescription1.setText(description1);

        if (description2 != null && !description2.isEmpty()){
            textViewDescription2.setText(description2);
            textViewDescription2.setVisibility(View.VISIBLE);
        }else{
            textViewDescription2.setVisibility(View.GONE);
        }

        switch (typePopUp){
            case BUTTONS_VERTICALY:

                buttonsContainer.setVisibility(View.VISIBLE);
                style2Button1.setVisibility(View.GONE);
                style2Button2.setVisibility(View.GONE);
                style1Button1.setVisibility(View.VISIBLE);
                style1Button2.setVisibility(View.VISIBLE);

                style1Button1.setTextColor(fontsColorID);
                style1Button2.setTextColor(fontsColorID);

                style1Button1.setText(button1Text);
                style1Button2.setText(button2Text);

                break;
            case BUTTONS_HORIZONTALY:

                buttonsContainer.setVisibility(View.GONE);

                style2Button1.setVisibility(View.VISIBLE);
                style2Button2.setVisibility(View.VISIBLE);
                style1Button1.setVisibility(View.GONE);
                style1Button2.setVisibility(View.GONE);

                style2Button1.setTextColor(fontsColorID);
                style2Button2.setTextColor(fontsColorID);

                style2Button1.setText(button1Text);
                style2Button2.setText(button2Text);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        if (!isBackDesactivate){
            super.onBackPressed();
        }
    }


    private View.OnClickListener style1Button1clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setPopUpResult(OPTIONS1_CLICKED);
        }
    };

    private View.OnClickListener style1Button2clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setPopUpResult(OPTIONS2_CLICKED);
        }
    };

    private View.OnClickListener style2Button1clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setPopUpResult(OPTIONS1_CLICKED);
        }
    };

    private View.OnClickListener style2Button2clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setPopUpResult(OPTIONS2_CLICKED);
        }
    };


    private void setPopUpResult(int optionsSelected){
        Intent intent = new Intent();
        intent.putExtra(OPTION_KEY,optionsSelected);
        if (optionsSelected == OPTIONS2_CLICKED){
            if (okayIntent != null){
                startActivity(okayIntent);
            }else{
                if (getParent() == null) {
                    setResult(Activity.RESULT_OK, intent);
                } else {
                    getParent().setResult(Activity.RESULT_OK, intent);
                }
            }

        }

        finish();

    }
}
