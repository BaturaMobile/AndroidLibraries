package com.baturamobile.utils.inputFilter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Created by virtu on 11/29/2017.
 */

public class NoSpecialNumericInputFilter implements InputFilter {
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {

        if (source.equals("")) { // for backspace
            return source;
        }
        if (source.toString().isEmpty()){
            return "";
        }
        //{
            String sourceString = source.toString();
            StringBuilder resultBuilder = new StringBuilder();

            for (int i = 0; i < sourceString.length(); i++) {
                if (Character.isDigit(sourceString.charAt(i))) {
                    continue;
                }
                if (Character.isLetterOrDigit(sourceString.charAt(i))) {
                    resultBuilder.append(sourceString.charAt(i));
                }
            }
        
            String retChar = new String(ch);*/
            return resultBuilder.toString();
        //}
       // return "";
    }
}