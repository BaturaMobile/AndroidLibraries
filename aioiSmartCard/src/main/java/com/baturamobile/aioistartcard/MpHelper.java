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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class MpHelper {

	public final static String PACKAGE_NAME = "com.aioisystems.marbleport";
	public static final String MARBLE_PORT_ACTION = PACKAGE_NAME + ".EXECUTE";

	//Result code
	public static final int RESULT_ERROR_CANCEL = 1;

	//Function number
	public static final int FN_SHOW_STATUS = 0;
	public static final int FN_GET_STATUS = 1;
	public static final int FN_READ_DATA = 2;
	public static final int FN_WRITE_DATA = 3;
	public static final int FN_SHOW_IMAGE = 4;
	public static final int FN_SAVE_LAYOUT = 5;
	public static final int FN_CHANGE_LAYOUT = 6;
	public static final int FN_CLEAR_DISPLAY = 7;
	public static final int FN_CHANGE_SECURITY_CODE = 8;

	public static final int FN_SHOW_TEXT = 21;

	//Multiple functions
	public static final int FN_READ_SHOW = 31;
	public static final int FN_READ_CHANGE = 32;
	public static final int FN_READ_CLEAR = 33;
	public static final int FN_READ_WRITE = 34;

	public static final int FN_READ_WRITE_SHOW = 41;
	public static final int FN_READ_WRITE_CHANGE = 42;
	public static final int FN_READ_WRITE_CLEAR = 43;

	public static final int FN_WRITE_SHOW = 51;
	public static final int FN_WRITE_CHANGE = 52;
	public static final int FN_WRITE_CLEAR = 53;

	//Values for 'AUTO_CLOSE' option.
	public final static int CLOSE_TYPE_NONE = 0;
	public final static int CLOSE_TYPE_DELAY = 1;
	public final static int CLOSE_TYPE_IMMEDIATE = 2;

	//Values for 'EXTRA_DRAW_MODE' option.
	public final static int DRAW_MODE_WHITE = 0;
	public final static int DRAW_MODE_BLACK = 1;
	public final static int DRAW_MODE_TRANSPARENT = 2;
	public final static int DRAW_MODE_LAYOUT = 3;

	//Smart-Tag types
	public static final int SMART_TAG_TYPE_20 = 20;
	public static final int SMART_TAG_TYPE_27 = 27;
	public static final int SMART_TAG_TYPE_29 = 29;

	//Vibrate
	public static final int VIBRATE_OFF = 0;
	public static final int VIBRATE_ON = 1;

	//Function parameter key
	public final static String EXTRA_SMART_TAG_TYPE = "EXTRA_SMART_TAG_TYPE";
	public final static String EXTRA_FUNCTION_NO = "EXTRA_FUNCTION_NO";

	public final static String EXTRA_BITMAP = "EXTRA_BITMAP";
	public final static String EXTRA_DITHER = "EXTRA_DITHER";
	public final static String EXTRA_LOCATION_X = "EXTRA_LOCATION_X";
	public final static String EXTRA_LOCATION_Y = "EXTRA_LOCATION_Y";
	public final static String EXTRA_DRAW_MODE = "EXTRA_DRAW_MODE";

	public final static String EXTRA_START_ADDRESS = "EXTRA_START_ADDRESS";
	public final static String EXTRA_START_ADDRESS2 = "EXTRA_START_ADDRESS2";
	public final static String EXTRA_USER_DATA = "EXTRA_USER_DATA";
	public final static String EXTRA_READ_SIZE = "EXTRA_READ_SIZE";

	public final static String EXTRA_LAYOUT_NUMBER = "EXTRA_LAYOUT_NUMBER";

	public final static String EXTRA_DISPLAY_TEXT = "EXTRA_DISPLAY_TEXT";
	public final static String EXTRA_TEXT_SIZE = "EXTRA_TEXT_SIZE";

	//for changing security code
	public final static String EXTRA_NEW_SECURITY_CODE1 = "EXTRA_NEW_SECURITY_CODE1";
	public final static String EXTRA_NEW_SECURITY_CODE2 = "EXTRA_NEW_SECURITY_CODE2";
	public final static String EXTRA_NEW_SECURITY_CODE3 = "EXTRA_NEW_SECURITY_CODE3";

	//Options key
	public final static String EXTRA_AUTO_CLOSE = "EXTRA_AUTO_CLOSE";
	public final static String EXTRA_SHOW_PROGRESS = "EXTRA_SHOW_PROGRESS";

	public final static String EXTRA_RELAY_TAG = "EXTRA_RELAY_TAG";
	public final static String EXTRA_ACTIVE_TAG = "EXTRA_ACTIVE_TAG";

	public final static String EXTRA_BATTERY = "EXTRA_BATTERY";
	public final static String EXTRA_FIRMWARE_VERSION = "EXTRA_FIRMWARE_VERSION";
	public final static String EXTRA_IDM = "EXTRA_IDM";
	public final static String EXTRA_TAG_STATUS = "EXTRA_TAG_STATUS";

	public final static String EXTRA_TITLE = "EXTRA_TITLE";
	public final static String EXTRA_MSG_SCAN = "EXTRA_MSG_SCAN";
	public final static String EXTRA_MSG_PROCESSING = "EXTRA_MSG_PROCESSING";
	public final static String EXTRA_MSG_END = "EXTRA_MSG_END";
	public final static String EXTRA_MSG_IO_ERROR = "EXTRA_MSG_IO_ERROR";

	public final static String EXTRA_SECURITY_CODE1 = "EXTRA_SECURITY_CODE1";
	public final static String EXTRA_SECURITY_CODE2 = "EXTRA_SECURITY_CODE2";
	public final static String EXTRA_SECURITY_CODE3 = "EXTRA_SECURITY_CODE3";

	public final static String EXTRA_VIBRATE = "EXTRA_VIBRATE";
	public final static String EXTRA_VIBRATE_DURATION = "EXTRA_VIBRATE_DURATION";

	public final static String EXTRA_USE_FELICA = "EXTRA_USE_FELICA";
	public final static String EXTRA_UPDATE_DISPLAY = "EXTRA_UPDATE_DISPLAY";
	public final static String EXTRA_CHECK_STATUS_AFTER_PROCESS = "EXTRA_CHECK_STATUS_AFTER_PROCESS";

	/**
	 * Determine whether that have installed marblePORT.
	 * @param context
	 * @param versionCode: required version code
	 * @return true:installed, false:not installed
	 */
	public static boolean checkMarblePort(Context context, int versionCode){

		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent(MARBLE_PORT_ACTION);
		List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo info : list){
			ActivityInfo ai = info.activityInfo;
			if(ai.packageName.equals(PACKAGE_NAME)){
				try{
					PackageInfo pInfo = pm.getPackageInfo(ai.packageName, PackageManager.GET_META_DATA);
					if(pInfo.versionCode >= versionCode)
						return true;
				}catch(NameNotFoundException e){
					return false;
				}
			}
		}
		return false;
	}

	public static boolean checkMarblePort(Context context){
		return checkMarblePort(context, 110);
	}

	/**
	 * Open the page of marblePORT on the Android market.
	 * @param context
	 */
	public static void navigateToMarket(Context context){

		final String path = "market://details?id="
				+ PACKAGE_NAME;
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}
}
