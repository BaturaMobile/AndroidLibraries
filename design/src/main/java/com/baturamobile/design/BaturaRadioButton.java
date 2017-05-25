package com.baturamobile.design;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by vssnake on 25/05/2017.
 */

public class BaturaRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    DelegateViewFont delegateViewFont;

    public BaturaRadioButton(Context context) {
        super(context);
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }

    public BaturaRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }

    public BaturaRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }
}
