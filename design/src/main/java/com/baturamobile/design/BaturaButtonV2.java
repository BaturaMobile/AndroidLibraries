package com.baturamobile.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;

/**
 * Created by vssnake on 18/12/2017.
 */

public class BaturaButtonV2 extends BaturaButton {
    public BaturaButtonV2(Context context) {
        super(context);
    }

    public BaturaButtonV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
    }

    public BaturaButtonV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }

    void initAttrs(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditTextView);
            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if(Build.VERSION.SDK_INT >= 21) {
                drawableLeft = attributeArray.getDrawable(R.styleable.CustomEditTextView_drawableLeftCompat);
                drawableRight = attributeArray.getDrawable(R.styleable.CustomEditTextView_drawableRightCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.CustomEditTextView_drawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.CustomEditTextView_drawableTopCompat);
            } else {
                int drawableLeftId = attributeArray.getResourceId(R.styleable.CustomEditTextView_drawableLeftCompat, -1);
                int drawableRightId = attributeArray.getResourceId(R.styleable.CustomEditTextView_drawableRightCompat, -1);
                int drawableBottomId = attributeArray.getResourceId(R.styleable.CustomEditTextView_drawableBottomCompat, -1);
                int drawableTopId = attributeArray.getResourceId(R.styleable.CustomEditTextView_drawableTopCompat, -1);
                if(drawableLeftId != -1) {
                    drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
                }

                if(drawableRightId != -1) {
                    drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
                }

                if(drawableBottomId != -1) {
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                }

                if(drawableTopId != -1) {
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
                }
            }

            this.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            attributeArray.recycle();
        }

    }
}
