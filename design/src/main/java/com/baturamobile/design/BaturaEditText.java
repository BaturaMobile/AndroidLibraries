package com.baturamobile.design;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
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

        delegateViewFont = new DelegateViewFont(this);
        delegateViewFont.init(context,attrs);
    }

    public BaturaEditText(Context context, AttributeSet attrs )
    {
        super( context, attrs );

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

}
