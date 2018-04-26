/*
 * Copyright (c) 2015, AIOI・SYSTEMS CO., LTD.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */

package com.baturamobile.aioistartcard;

import android.graphics.Bitmap;
import android.nfc.Tag;

import com.baturamobile.aioistartcard.imaging.DisplayPainter;
import com.baturamobile.aioistartcard.smarttag.SmartTagCore;


/**
 * Implements the smart-tag processes.
 */
public class SmartTag extends SmartTagCore {

	public static final int FN_SHOW_STATUS = 0;
	public static final int FN_CHANGE_LAYOUT = 1;
	public static final int FN_READ_DATA = 2;
	public static final int FN_WRITE_DATA = 3;
	public static final int FN_DRAW_TEXT = 5;
	public static final int FN_DRAW_CAMERA_IMAGE = 7;
	public static final int FN_SAVE_LAYOUT = 8;
	public static final int FN_CLEAR_DISPLAY = 9;

	public static final int FN_SHOW_STATUS27 = 20;
	public static final int FN_SHOW_STATUS29 = 21;
	public static final int FN_SHOW_IMAGE29 = 22;

	private String mDrawText = "";
	private int mSaveNumber = 0;
	private int mLayoutNo = 0;
	private int mFunctionNo = FN_SHOW_STATUS;
	private Bitmap mCameraImage = null;
	private Bitmap mImage = null;
	private Exception mLastError = null;
	private String mWriteText = "";
	private String mReadText = "";

	public SmartTag(){
		setMaxBlocks(12);
	}

	public String getReadText(){
		return mReadText;
	}
	public void setWriteText(String text){
		mWriteText = text;
	}

	public Exception getLastError(){
		return mLastError;
	}

	public void setCameraImage(Bitmap bitmap){
		mCameraImage = bitmap;
	}

	public void setImage(Bitmap bitmap){
		mImage = bitmap;
	}

	public int getFunctionNo() {
		return mFunctionNo;
	}

	public void setFunctionNo(int functionNo) {
		this.mFunctionNo = functionNo;
	}

	public void setLayoutNo(int number){
		mLayoutNo = number;
	}

	public void setSaveNo(int number){
		mSaveNumber = number;
	}

	public void setDrawText(String drawText) {
		this.mDrawText = drawText;
	}

	@Override
	public void selectTarget(byte[] idm, Tag tag){
		super.selectTarget(idm, tag);
		mLastError = null;
	}

	public void startSession(){
		mLastError = null;

		try{
			connect();
		}catch(Exception e){
			mLastError = e;
			Common.addLoge(e.toString());
			return;
		}
		try{
			waitForIdle();
			executeFunction();

			if(SmartTagCore.getProductType(mIdm) == SmartTagCore.SMARTCARD_29){
				waitForIdle();	//waits until the process finished.
			}

		}catch(Exception e){
			mLastError = e;
			Common.addLoge(e.toString());

		}finally{
			try{
				close();
			}catch(Exception e){
				Common.addLoge(e.toString());
			}
			if(mLastError == null){
				startPollingForRelease();
			}
		}
	}

    private void executeFunction() throws Exception{

    	switch(mFunctionNo){

    		case SmartTag.FN_SHOW_STATUS:
    			showStatus();
    			break;
    		case SmartTag.FN_SHOW_STATUS27:
    			showStatusFor27();
    			break;
    		case SmartTag.FN_SHOW_STATUS29:
    			showStatusFor29();
    			break;
    		case SmartTag.FN_DRAW_CAMERA_IMAGE:
	    		showImage(mCameraImage);
	    		break;
			case SmartTag.FN_SHOW_IMAGE29:
				showImage(mImage);

	    	case SmartTag.FN_SAVE_LAYOUT:
	    		saveLayout(mSaveNumber);
	    		break;

	    	case SmartTag.FN_CHANGE_LAYOUT:
	        	selectLayout(mLayoutNo);
	    		break;

	    	case SmartTag.FN_DRAW_TEXT:
	    		drawText();
	    		break;

	    	case SmartTag.FN_WRITE_DATA:
	    		saveUrl();
	    		break;

	    	case SmartTag.FN_READ_DATA:
	    		readUrl();
	    		break;

	    	case SmartTag.FN_CLEAR_DISPLAY:
	    		clearDisplay();
	    		break;
    	}
    }

    /**
     * Shows the smart-tag status to the display.
     */
    private void showStatus() throws Exception{
    	DisplayPainter display = new DisplayPainter();
    	int y = 0;
    	display.putText("SMART-TAG", 0, 0, 24);
    	display.putText("[ST1020]", 0, y+=24, 12);
    	display.putText("Battery: " + getBatteryStateText(mBattery), 0, y+=24, 12);
    	display.putText(String.format("Firmware ver.: %02X", mVersion), 0, y+=12, 12);
    	display.putText("ID: " + Common.makeHexText(mIdm), 0, y+=12, 12);

    	byte[] imageData = display.getLocalDisplayImage();

		showImage(imageData);
    }

    /**
     * Shows the smart-tag status to the display.(for 2.7inch)
     * @throws Exception
     */
    private void showStatusFor27() throws Exception{
    	DisplayPainter display = new DisplayPainter(DisplayPainter.DISPLAY_TYPE_264X176);
    	int y = 40;
    	display.putText("SMART-TAG", 60, y, 24);
    	display.putText("[ST1027]", 100, y+=24, 12);
    	display.putText("Battery: " + getBatteryStateText(mBattery), 60, y+=24, 12);
    	display.putText(String.format("Firmware ver.: %02X", mVersion), 60, y+=12, 12);
    	display.putText("ID: " + Common.makeHexText(mIdm), 60, y+=12, 12);

    	byte[] imageData = display.getLocalDisplayImage();

		showImage(imageData, 0, 0, 264, 176, (byte)0, (byte)0);
    }

    private void showStatusFor29() throws Exception{
    	DisplayPainter display = new DisplayPainter(DisplayPainter.DISPLAY_TYPE_300X200);
    	int y = 40;
    	display.putText("SmartCard", 80, y, 30);
    	display.putText("[SC1029L]", 105, y+=30, 20);
    	display.putText(String.format("Firmware ver.: %02X", mVersion), 10, y+=30, 20);
    	display.putText("ID: " + Common.makeHexText(mIdm), 10, y+=20, 20);

    	byte[] imageData = display.getLocalDisplayImage();

		showImage(imageData, 0, 0, 300, 200, (byte)0, (byte)0);
    }

	/**
	 * Shows the texts to the display.
	 */
	private void drawText() throws Exception {

		DisplayPainter display = new DisplayPainter();
		String[] texts = mDrawText.split("\n");
		for(int i = 0; i < texts.length; i++){
			display.putText(texts[i], 0, i * 22 + 1, 20);
			if(i == 3){
				break;
			}
		}

		byte[] imageData = display.getLocalDisplayImage();

		showImage(imageData);
	}

	/**
	 * Saves an URL data.
	 * @throws Exception
	 */
	private void saveUrl() throws Exception{
		if(mWriteText == ""){
			return;
		}
		String wk = mWriteText + "\n";
		byte[] dataBytes = wk.getBytes("ASCII");
		writeUserData(0, dataBytes);
	}

	/**
	 * Load an URL data.
	 * @throws Exception
	 */
	private void readUrl() throws Exception{
		byte[] buffer = readUserData(0, 256);
		mReadText = getUserText(buffer);
	}

	private String getUserText(byte[] dataBytes){
		int index = -1;
		for(int i = 0; i < dataBytes.length; i++){
			if(dataBytes[i] == '\n'){
				index = i - 1;
				break;
			}
		}
		if(index == -1)
			return "";

		byte[] tmp = new byte[index + 1];
		System.arraycopy(dataBytes, 0, tmp, 0, tmp.length);

		String text = "";
		try{
			text = new String(tmp, "ASCII");
		}catch(Exception e){
			Common.addLoge(e.toString());
			return "";
		}
		return text;
	}

	private static String getBatteryStateText(int state){
		String msg = "---";

		switch(state){
		case SmartTag.BATTERY_NORMAL1:
			msg = "Fine";
			break;
		case SmartTag.BATTERY_NORMAL2:
			msg = "Normal";
			break;
		case SmartTag.BATTERY_LOW1:
			msg = "Low";
			break;
		case SmartTag.BATTERY_LOW2:
			msg = "Empty";
			break;
		}
		return msg;
	}
}
