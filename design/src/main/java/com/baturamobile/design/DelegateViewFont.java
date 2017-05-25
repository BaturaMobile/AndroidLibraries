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
            if (attrs != null){
                TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DesignCustomFont);
                mStyle = attributes.getString(R.styleable.DesignCustomFont_BaturaFontType);

                attributes.recycle();
            }

            Typeface typeFace = null;

            if ( mStyle != null && !mStyle.isEmpty() )
            {
                typeFace = Typeface.createFromAsset( context.getAssets(), mStyle );
            }else if (!GenericData.GENERIC_FONT.isEmpty()){
                typeFace = Typeface.createFromAsset( context.getAssets(), GenericData.GENERIC_FONT );
            }
            if (typeFace != null){
                delegateTextView.setTypeface(typeFace);
            }
        }
    }
}
