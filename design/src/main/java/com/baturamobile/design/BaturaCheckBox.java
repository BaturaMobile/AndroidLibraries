package com.baturamobile.design;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * Created by vssnake on 09/05/2017.
 */

public class BaturaCheckBox extends AppCompatCheckBox {

    DelegateViewFont delegateViewFont;


    public BaturaCheckBox(Context context )
    {
        super( context );
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }

    public BaturaCheckBox(Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );

        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }

    public BaturaCheckBox(Context context, AttributeSet attrs )
    {
        super( context, attrs );

        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }
}
