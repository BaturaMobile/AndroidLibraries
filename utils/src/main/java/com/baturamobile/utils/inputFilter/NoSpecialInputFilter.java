package com.baturamobile.utils.inputFilter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by vssnake on 05/12/2017.
 */

public class NoSpecialInputFilter implements InputFilter {

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {

        if (source.equals("")) { // for backspace
            return source;
        }
        if (source.toString().isEmpty()){
            return "";
        }
        String sourceString = source.toString();
        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < sourceString.length(); i++) {
            if (Character.isLetter(sourceString.charAt(i))
                    || Character.isSpaceChar(sourceString.charAt(i))
                    || Character.isDigit(sourceString.charAt(i))) {
                resultBuilder.append(sourceString.charAt(i));
            }
        }

        return resultBuilder.toString();
    }
}
