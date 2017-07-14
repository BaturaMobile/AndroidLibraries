package com.baturamobile.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.content.res.AppCompatResources;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Patterns;

/**
 * Created by vssnake on 09/05/2017.
 */

public class BaturaEditText extends TextInputEditText{

    DelegateViewFont delegateViewFont;

    public BaturaEditText(Context context )
    {
        super( context );
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,null);
    }

    public BaturaEditText(Context context, AttributeSet attrs, int defStyleAttr )
    {
        super( context, attrs, defStyleAttr );
        initAttrs(context,attrs);
        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }

    public BaturaEditText(Context context, AttributeSet attrs )
    {
        super( context, attrs );
        initAttrs(context,attrs);

        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }


    /**
     * This method only check one use case at the time
     */
    public boolean isValid(){

        if (checkFlag(getInputType(),InputType.TYPE_CLASS_PHONE)){
            return isValidPhone();
        }
        if (checkFlag(getInputType(),InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)){
            return isValidEmail();
        }
        throw new UnsupportedOperationException();
    }

    private boolean checkFlag(int flags, int flag){
        return (flags & flag) == flag;
    }

    private boolean isValidEmail(){
        return Patterns.EMAIL_ADDRESS.matcher(getText()).matches();
    }

    private boolean isValidPhone(){
        return Patterns.PHONE.matcher(getText()).matches();
    }

    public void setEditable(boolean editable){
            this.setClickable(editable);
            this.setCursorVisible(editable);
            this.setFocusable(editable);
            this.setFocusableInTouchMode(editable);

    }



    void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomEditTextView);

            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;

            Boolean editable =
                    attributeArray.getBoolean(R.styleable.CustomEditTextView_editable,true);
            setEditable(editable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableLeft = attributeArray.getDrawable(R.styleable.CustomEditTextView_drawableLeftCompat);
                drawableRight = attributeArray.getDrawable(R.styleable.CustomEditTextView_drawableRightCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.CustomEditTextView_drawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.CustomEditTextView_drawableTopCompat);
            } else {
                final int drawableLeftId = attributeArray.getResourceId(R.styleable.CustomEditTextView_drawableLeftCompat, -1);
                final int drawableRightId = attributeArray.getResourceId(R.styleable.CustomEditTextView_drawableRightCompat, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.CustomEditTextView_drawableBottomCompat, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.CustomEditTextView_drawableTopCompat, -1);

                if (drawableLeftId != -1)
                    drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
                if (drawableRightId != -1)
                    drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
                if (drawableBottomId != -1)
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                if (drawableTopId != -1)
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
            }
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            attributeArray.recycle();
        }
    }

}
