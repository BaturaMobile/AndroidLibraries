package com.baturamobile.design;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by vssnake on 10/05/2017.
 */

public class BaturaButton extends AppCompatButton {

    DelegateViewFont delegateViewFont;

    public BaturaButton(Context context )
    {
        super( context );
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }

    public BaturaButton(Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );

        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }

    public BaturaButton(Context context, AttributeSet attrs )
    {
        super( context, attrs );

        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }
}
