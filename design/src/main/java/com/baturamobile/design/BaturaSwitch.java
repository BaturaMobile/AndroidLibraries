package com.baturamobile.design;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

/**
 * Created by vssnake on 02/08/2017.
 */

public class BaturaSwitch extends SwitchCompat {

    DelegateViewFont delegateViewFont;

    public BaturaSwitch(Context context) {
        super(context);
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }

    public BaturaSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }

    public BaturaSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }
}
