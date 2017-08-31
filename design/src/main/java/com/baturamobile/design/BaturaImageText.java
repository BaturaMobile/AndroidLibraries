package com.baturamobile.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by vssnake on 02/08/2017.
 */

public class BaturaImageText extends LinearLayoutCompat implements View.OnClickListener {

    private AppCompatImageView mImageView;
    private BaturaTextView mTextView;

    private View mView;

    private String mStyle = "";

    private OnCheckedChangeListener mOnCheckedChangedListener;

    int mNormalColor;
    int mSelectedColor;
    Drawable mImageSrc;
    String mText;
    boolean mSelected = false;
    int unitsTextSize;

    public BaturaImageText(Context context) {
        super(context);

        initViews(null);
    }

    public BaturaImageText(Context context, AttributeSet attrs) {
        super(context, attrs);

        initViews(attrs);
    }

    public BaturaImageText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews(attrs);
    }



    private void initViews(AttributeSet attrs){

        setClickable(true);
        setOnClickListener(this);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflater.inflate(R.layout.batura_image_text,this,true);

        mImageView  = (AppCompatImageView) findViewById(R.id.bit_image);
        mTextView = (BaturaTextView) findViewById(R.id.bit_text);

        selectBackground();

        processAttributes(attrs);
    }

    private void selectBackground(){
        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};

        // Obtain the styled attributes. 'themedContext' is a context with a
        // theme, typically the current Activity (i.e. 'this')
        TypedArray ta = getContext().obtainStyledAttributes(attrs);

        // Now get the value of the 'listItemBackground' attribute that was
        // set in the theme used in 'themedContext'. The parameter is the index
        // of the attribute in the 'attrs' array. The returned Drawable
        // is what you are after
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);

        // Finally free resources used by TypedArray
        ta.recycle();

        // setBackground(Drawable) requires API LEVEL 16,
        // otherwise you have to use deprecated setBackgroundDrawable(Drawable) method.
        setBackground(drawableFromTheme);
    }

    private void processAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.BaturaImageText);
            mImageSrc = attributes.getDrawable(R.styleable.BaturaImageText_imageSrc);
            mText =  attributes.getString(R.styleable.BaturaImageText_titleText);
            mNormalColor = attributes.getColor(R.styleable.BaturaImageText_normalColor, Color.GRAY);
            mSelectedColor = attributes.getColor(R.styleable.BaturaImageText_selectedColor, Color.BLACK);
            unitsTextSize = attributes.getDimensionPixelSize(R.styleable.BaturaImageText_titleTextSize, 0);


            mTextView.setText(mText);

            if (unitsTextSize != 0){
                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitsTextSize);
            }


            mImageView.setImageDrawable(mImageSrc);


            deselected();


            attributes.recycle();
        }
    }

    public void selected(boolean selected){
        mSelected = selected;
        if (mSelected){
            selected();
        }else{
            deselected();
        }
    }

    private void selected(){

        mTextView.setTextColor(mSelectedColor);
        mImageView.setColorFilter(mSelectedColor);

    }

    private void deselected (){
        mTextView.setTextColor(mNormalColor);
        mImageView.setColorFilter(mNormalColor);
    }

    @Override
    public void onClick(View v) {
        if (!mSelected){
            selected();
        }else{
            deselected();
        }
        mSelected =! mSelected;
        triggerOnCheckedChanged();
    }


   @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateIsSelectable = this.mSelected;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;

        this.mSelected = ss.stateIsSelectable;

        if (mSelected){
            selected();
        }else{
            deselected();
        }

        super.onRestoreInstanceState(ss.getSuperState());



    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onConnectorTypeFChanged) {
        this.mOnCheckedChangedListener = onConnectorTypeFChanged;
    }

    private void triggerOnCheckedChanged(){
        if (mOnCheckedChangedListener != null){
            mOnCheckedChangedListener.onCheckedChanged(this,mSelected);
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(BaturaImageText baturaImageText,boolean checked);
    }

    static class SavedState extends BaseSavedState {
        boolean stateIsSelectable;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);

            int isSelectable = in.readInt();
            if (isSelectable == 0){
                stateIsSelectable = false;
            }else{
                stateIsSelectable = true;
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            if (stateIsSelectable){
                out.writeInt(1);
            }else{
                out.writeInt(0);
            }

        }

        //required field that makes Parcelables from a Parcel
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
