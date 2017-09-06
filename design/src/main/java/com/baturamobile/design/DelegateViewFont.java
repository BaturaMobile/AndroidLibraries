package com.baturamobile.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vssnake on 09/05/2017.
 */

public class DelegateViewFont {



    TextView delegateTextView;
    private String mStyle = "";

    public DelegateViewFont(TextView textView){
        this.delegateTextView = textView;
    }


    protected void init(Context context, AttributeSet attrs)
    {

        if ( !delegateTextView.isInEditMode() )
        {
            String fontFamlityCompat = null;
            if (attrs != null){
                TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DesignCustomFont);
                mStyle = attributes.getString(R.styleable.DesignCustomFont_BaturaFontType);
                fontFamlityCompat = attributes.getString(R.styleable.AppCompatTextView_fontFamily);

                if (attributes.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_fontFamily)
                        || attributes.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_fontFamily)) {
                    int fontFamilyId = attributes.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_fontFamily)
                            ? android.support.v7.appcompat.R.styleable.TextAppearance_android_fontFamily
                            : android.support.v7.appcompat.R.styleable.TextAppearance_fontFamily;
                    fontFamlityCompat = attributes.getString(fontFamilyId);
                }
                attributes.recycle();
            }

            Typeface typeFace = null;

            if ( mStyle != null && !mStyle.isEmpty() )
            {
                typeFace = Typeface.createFromAsset( context.getAssets(), mStyle );
            }else if (!GenericData.GENERIC_FONT.isEmpty() && fontFamlityCompat == null){
                typeFace = Typeface.createFromAsset( context.getAssets(), GenericData.GENERIC_FONT );
            }
            if (typeFace != null){
                delegateTextView.setTypeface(typeFace);
            }
        }
    }
}
