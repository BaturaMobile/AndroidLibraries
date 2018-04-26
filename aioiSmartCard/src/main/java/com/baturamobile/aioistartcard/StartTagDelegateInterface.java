package com.baturamobile.aioistartcard;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by vssnake on 26/06/2017.
 */

public interface StartTagDelegateInterface {

    void nfcShowProgress(int percent);

    void nfcMaxChanged(int max);

    void nfcVisibleProgress(int visibility);

    void nfcSetIndeterminate(boolean indeterminate);

    AppCompatActivity getActivity();

    void onNFCDataRead(String readText);

    void onNFCError(Exception exception);

    void onNFCSuccess();

    void onProcessing();
}
