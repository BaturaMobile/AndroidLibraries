package com.baturamobile.design;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StyleRes;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

/**
 * Created by vssnake on 25/05/2017.
 */

public class BaturaTextInputLayout extends TextInputLayout {


    private @StyleRes int styleError;

    public BaturaTextInputLayout(Context context) {
        super(context);
    }

    public BaturaTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaturaTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void redrawCustomFeatures(){
        if (styleError != 0) setErrorTextAppearance(styleError);
    }


    @Override
    public void setError(CharSequence error){
        //setErrorTextAppearance(android.support.design.R.style.TextAppearance_Design_Error);
        super.setError(error);
    }

    public void setCustomHintBottom(CharSequence text, @StyleRes int style){
        styleError = style;
        super.setError(text);
        setErrorTextAppearance(style);


    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.styleError = this.styleError;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        try{
            SavedState ss = (SavedState)state;
            super.onRestoreInstanceState(ss.getSuperState());

            this.styleError = ss.styleError;

            redrawCustomFeatures();
        }catch (Exception e){}

    }

    static class SavedState extends BaseSavedState {
        int styleError;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.styleError = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.styleError);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
