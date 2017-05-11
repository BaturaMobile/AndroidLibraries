package com.baturamobile.design;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by vssnake on 08/05/2017.
 */

public class BaturaTextView extends AppCompatTextView {


    DelegateViewFont delegateViewFont;

    public BaturaTextView(Context context )
    {
        super( context );
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }

    public BaturaTextView(Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );

        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }

    public BaturaTextView(Context context, AttributeSet attrs )
    {
        super( context, attrs );

        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }
}
