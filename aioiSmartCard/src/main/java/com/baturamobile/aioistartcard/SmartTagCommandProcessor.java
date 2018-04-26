package com.baturamobile.aioistartcard;

import android.content.Intent;
import android.graphics.Bitmap;

import com.baturamobile.aioistartcard.imaging.DisplayPainter;

import static com.baturamobile.aioistartcard.MpHelper.FN_CLEAR_DISPLAY;
import static com.baturamobile.aioistartcard.MpHelper.FN_READ_DATA;
import static com.baturamobile.aioistartcard.MpHelper.FN_SHOW_IMAGE;
import static com.baturamobile.aioistartcard.MpHelper.FN_WRITE_DATA;
import static com.baturamobile.aioistartcard.SmartTagHelper.EXTRA_BITMAP;
import static com.baturamobile.aioistartcard.SmartTagHelper.EXTRA_FUNCTION_NO;
import static com.baturamobile.aioistartcard.SmartTagHelper.EXTRA_TEXT;
import static com.baturamobile.aioistartcard.imaging.DisplayPainter.DISPLAY_TYPE_300X200;

public class SmartTagCommandProcessor {

    SmartTag mSmartTag;
    public SmartTagCommandProcessor(SmartTag smartTag){
        this.mSmartTag = smartTag;
    }

    private int command = 0;

    public void processCommand(Intent intent){
        int functionNumber = intent.getIntExtra(EXTRA_FUNCTION_NO,FN_SHOW_IMAGE);

        command = functionNumber;
        switch (functionNumber){
            case FN_SHOW_IMAGE:
                showImage(intent);
                break;
            case FN_WRITE_DATA:
                writeData(intent);
                break;
            case FN_READ_DATA:
                readData();
                break;
            case FN_CLEAR_DISPLAY:
                clearDisplay();
                break;

        }
    }

    private void showImage(Intent intent){
        final Bitmap bitmap = intent.getParcelableExtra(EXTRA_BITMAP);
        if (bitmap != null ) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Common.addLogi("processBitmap");

                    Bitmap trimed = Bitmap.createScaledBitmap(bitmap, SmartTag.SCREEN_WIDTH, SmartTag.SCREEN_HEIGHT, false);

                    DisplayPainter painter = new DisplayPainter(DISPLAY_TYPE_300X200);
                    painter.putImage(trimed, 0, 0, true);
                    bitmap.recycle();
                    trimed.recycle();

                    Bitmap previewBitmap = painter.getPreviewImage();
                    mSmartTag.setImage(previewBitmap);
                    mSmartTag.setFunctionNo(SmartTag.FN_SHOW_IMAGE29);

                    Common.addLogi("bitMapProcessed");
                }
            }).start();
        }
    }

    private void writeData(Intent intent){
        final String text = intent.getStringExtra(EXTRA_TEXT);
        if (text != null) {
                    mSmartTag.setWriteText(text);
                    mSmartTag.setFunctionNo(SmartTag.FN_WRITE_DATA);
                    Common.addLogi("WriteSucess");
        }
    }

    private void readData(){

                    mSmartTag.setFunctionNo(SmartTag.FN_READ_DATA);

                    Common.addLogi("Read Queue");

    }

    private void clearDisplay(){
        mSmartTag.setFunctionNo(SmartTag.FN_CLEAR_DISPLAY);

        Common.addLogi("WriteSucess");
    }

    public int getCommand() {
        return command;
    }
}
