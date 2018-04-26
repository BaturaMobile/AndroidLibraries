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

package com.baturamobile.aioistartcard.smarttag;

import android.graphics.Bitmap;
import android.nfc.Tag;
import android.nfc.tech.NfcF;

import com.baturamobile.aioistartcard.imaging.DisplayPainter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Implements the low level process for the smart-tag.
 * @author AIOI SYSTEMS CO., LTD.
 * @version 1.5.0
 */
public class SmartTagCore {

	public static final int SCREEN_WIDTH = 300;
	public static final int SCREEN_HEIGHT = 200;
	private static final int MAX_WRITE_SIZE = 512;

	private static final byte COMMAND_DATA_READ = (byte)0xC0;
	private static final byte COMMAND_CHECK_STATUS = (byte)0xD0;
	private static final byte COMMAND_CLEAR = (byte)0xA1;
	private static final byte COMMAND_SHOW_DISPLAY_OLD = (byte)0xA0;
	private static final byte COMMAND_SAVE_LAYOUT = (byte)0xB2;
	private static final byte COMMAND_SHOW_DISPLAY = (byte)0xA2;
	private static final byte COMMAND_SHOW_DISPLAY3 = (byte)0xA3;

	public static final int BATTERY_NORMAL1 = 0;
	public static final int BATTERY_NORMAL2 = 1;
	public static final int BATTERY_LOW1 = 2;
	public static final int BATTERY_LOW2 = 3;
	public static final int BATTERY_UNKOWN = -1;

	public static final byte TAG_STS_UNKNOWN = (byte)0x70;
	public static final byte TAG_STS_INIT = 0;
	public static final byte TAG_STS_PROCESSED = (byte)0xF0;
	public static final byte TAG_STS_BUSY = (byte)0xF2;

	public static final int SMARTTAG_20 = 20;
	public static final int SMARTTAG_27 = 27;
	public static final int SMARTCARD_29 = 29;

	protected NfcF mFelica = null;
	protected byte[] mIdm = null;
	protected byte[] mSystemCode = null;
	protected byte mBattery = BATTERY_UNKOWN;
	protected byte mVersion = 0;
	protected byte mStatus = 0;
	protected Tag mTag = null;

	protected ProgressListener mProgressListener = null;
	protected TagActionListener mTagActionListener = null;

	private int mProgress = 0;
	private volatile boolean mStopPolling = false;

	private CommandBuilder mBuilder;

	public SmartTagCore(){
		mBuilder = new CommandBuilder();
		mBuilder.setMaxBlocks(12);
	}


	/**
	 * Gets the maximum transmittion block numbers.
	 * @return
	 */
	public int getMaxBlocks(){
		return mBuilder.getMaxBlocks();
	}

	/**
	 * Sets the maximum transmittion block numbers.
	 * @param value
	 */
	public void setMaxBlocks(int value){
		mBuilder.setMaxBlocks(value);
	}

	/**
	 * Returns the last smart-tag status.
	 * @return
	 */
	public byte getStatus(){
		return mStatus;
	}

	/**
	 * Gets the IDm.
	 */
	public byte[] getIdm(){
		return mIdm;
	}

	/**
	 * Gets the system code.
	 */
	public byte[] getSystemCode(){
		return mSystemCode;
	}

	/**
	 * Gets the firmware version of the smart-tag.
	 */
	public byte getVersion(){
		return mVersion;
	}

	/**
	 * Gets the battery state.
	 */
	public int getBatteryLevel(){
		return mBattery;
	}

	/**
	 * Register a callback to be invoked when progress is changed.
	 * @param listener
	 * @since 1.2.0
	 */
	public void setProgressListener(ProgressListener listener){
		mProgressListener = listener;
	}

	/**
	 * Register a callback to be invoked when a smart-tag was operated.
	 * @param listener
	 * @since 1.2.0
	 */
	public void setTagActionListener(TagActionListener listener){
		mTagActionListener = listener;
	}

	/**
	 * Removes a specified callback.
	 * @since 1.2.0
	 */
	public void removeProgressListener(){
		mProgressListener = null;
	}

	/**
	 * Removes a specified callback.
	 * @since 1.2.0
	 */
	public void removeTagActionListener(){
		mTagActionListener = null;
	}

	/**
	 * Sets a target IDm.
	 * @param idm
	 * @param tag
	 */
	public void selectTarget(byte[] idm, Tag tag){
		mIdm = idm;
		mFelica = NfcF.get(tag);
		mSystemCode = mFelica.getSystemCode();
		if(mSystemCode[0] == 0x00 && mSystemCode[1] == 0x00){
			mSystemCode[0] = (byte)0xff;
			mSystemCode[1] = (byte)0xff;
		}
		mTag = tag;
	}

	/**
	 * Gets a Tag object.
	 * @return
	 */
	public Tag getActiveTag(){
		return mTag;
	}

	/**
	 * Connects the smart-tag.
	 * @throws Exception
	 */
	public void connect() throws Exception{
		mFelica.connect();
	}

	/**
	 * Closes the smart-tag.
	 * @throws Exception
	 */
	public void close() throws Exception{
		mFelica.close();
	}

	/**
	 * Indicates whether the smart-tag.
	 * @return True if the IDm is the smart-tag; otherwise, false.
	 */
	public boolean isSmartTag(){
		if(mIdm == null) return false;
		if(mIdm.length < 8) return false;

		boolean result = false;

		if(mIdm[0] == (byte)0x03
				&& mIdm[1] == (byte)0xFE
				&& mIdm[2] == (byte)0x00
				&& mIdm[3] == (byte)0x1D){
			result = true;
		}else if(mIdm[0] == (byte)0x02
				&& mIdm[1] == (byte)0xFE
				&& mIdm[2] == (byte)0x00
				&& mIdm[3] == (byte)0x00){
			result = true;
		}
		return result;
	}

	/**
	 * Confirms the smart-tag status.
	 */
	public void checkStatus() throws Exception{
		//mBattery = SmartTag.BATTERY_UNKOWN;
		mVersion = -1;
		mStatus = TAG_STS_UNKNOWN;

		byte[] paramData = new byte[]{
				0, 0, 0, 0,
				0, 0, 0, 0
		};

		writeCommand(COMMAND_CHECK_STATUS, paramData);

		//read status
		byte[] blockData = readBlocks(2);
		if(blockData != null){
			mStatus = blockData[3];
			mBattery = blockData[5];
			mVersion = blockData[15];
			mBuilder.setSeq((byte)(blockData[4] + 1));

			DebugUtil.addLogd("status = " + mStatus);
		}
	}

	/**
	 * Waits until smart-tag can do a next task.
	 * @throws Exception
	 */
	public void waitForIdle() throws Exception{
		Exception error = null;
		for(int i = 0; i < 600; i++){

			try{
				checkStatus();
			}catch(Exception e){
				DebugUtil.addLoge(e.toString() + " in waitForIdle()");
				error = e;
				break;
			}
			if(mStatus != TAG_STS_BUSY
					&& mStatus != TAG_STS_UNKNOWN){
				return;
			}
			try{
				Thread.sleep(50);
			}catch(Exception ignore){

			}
		}
		if(mStatus == TAG_STS_UNKNOWN){
			throw error;
		}else{
			throw new SmartTagException(mStatus);
		}
	}

	/**
	 * Clears a smart-tag display.
	 */
	public void clearDisplay() throws Exception {
		byte[] paramData = new byte[]{
				0, 0, 0, 0,
				0, 0, 0, 0
		};
		writeCommand(COMMAND_CLEAR, paramData);
	}

	/**
	 * Registers the last shown image to the smart-tag.
	 * @param layoutNo
	 * @throws Exception
	 */
	public void saveLayout(int layoutNo) throws Exception{
		byte[] paramData = new byte[]{
				(byte)layoutNo, 0, 0, 0,
				0, 0, 0, 0
		};
		writeCommand(COMMAND_SAVE_LAYOUT, paramData);
	}

	/**
	 * Shows the registered image.
	 * @param layoutNo
	 */
	public void selectLayout(int layoutNo) throws Exception{
		byte[] paramData = new byte[]{
				1, 1, (byte)0xFF, (byte)0xFF,
				25, 3, (byte)layoutNo, 1
		};

		writeCommand(COMMAND_SHOW_DISPLAY_OLD, paramData);
	}

	/**
	 * Displays an image.(2inch)
	 * @param bitmap
	 * @param digher
	 * @throws Exception
	 */
	public void showImage(Bitmap bitmap, boolean dither) throws Exception {
		if(bitmap == null)
			return;

		DisplayPainter display = new DisplayPainter(DisplayPainter.DISPLAY_TYPE_300X200);
		display.putImage(bitmap, 0, 0, dither);

		byte[] imageData = display.getLocalDisplayImage();


		showImage(imageData, 0, 0, 300, 200, (byte)0, (byte)0);
	}



	public void showImage(Bitmap bitmap) throws Exception {
		showImage(bitmap, false);
	}

	/**
	 * Displays an image.(2inch)
	 * @param imageData
	 * @throws Exception
	 */
	public void showImage(byte[] imageData) throws Exception {
		byte[] paramData = new byte[]{
				1, 1, 0, 0,
				25, 0, 0, 3
		};
		writeCommand(COMMAND_SHOW_DISPLAY_OLD, paramData, imageData);
	}

	/**
	 * Displays an image.(2inch)
	 * @param imageData
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param drawMode
	 * @param layoutNo
	 * @throws Exception
	 * @since 1.3.0
	 */
	public void showImage(byte[] imageData,
			byte x, byte y,
			byte width, byte height,
			byte drawMode,
			byte layoutNo) throws Exception{

		byte[] paramData = new byte[]{
				x, y, width, height,
				0, drawMode, layoutNo, 3
		};
		writeCommand(COMMAND_SHOW_DISPLAY, paramData, imageData);
	}

	/**
	 * Displays an image.(2.7inch)
	 * @param imageData
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param drawMode
	 * @param layoutNo
	 * @throws Exception
	 * @since 1.4.0
	 */
	public void showImage(byte[] imageData,
			int x, int y,
			int width, int height,
			byte drawMode,
			byte layoutNo) throws Exception{

		byte[] paramData = null;
		byte[] pos = ConvertTo3Bytes(x, y);
		byte[] size = ConvertTo3Bytes(width, height);
        byte mode = (byte)(drawMode << 4);
        mode |= 0x03;

        paramData = new byte[]{
            pos[0], pos[1], pos[2], size[0],
            size[1], size[2], layoutNo, mode
        };
        writeCommand(COMMAND_SHOW_DISPLAY3, paramData, imageData);
	}

	/**
	 * Writes the user data to the free information area on the smart-tag.
	 * Divides and sends the command into some frames if necessary.
	 * @param startAddress
	 * @param data
	 * @throws Exception
	 */
	public void writeUserData(int startAddress, byte[] data) throws Exception{

		int splitCount = (data.length + MAX_WRITE_SIZE - 1) / MAX_WRITE_SIZE;

		int offset = 0;
		int dataLen = (data.length <= MAX_WRITE_SIZE ? data.length : MAX_WRITE_SIZE);

		mProgress = 0;
		if(mProgressListener != null){
			mProgressListener.onMaxChanged(getProgressMaxforWrite(data.length));
		}

		for(int i = 0; i < splitCount; i++){
			if(i == splitCount - 1){//last frame
				dataLen = data.length - offset;
			}
			byte[] framedata = Arrays.copyOfRange(data, offset, offset+dataLen);
			this.writeUserDataByFrame(startAddress, framedata);

			offset += dataLen;
			startAddress += dataLen;

			Thread.sleep(400);
			waitForIdle();
		}
	}

	/**
	 * Writes the user data up to 512-bytes.
	 * @param address
	 * @param data
	 * @throws Exception
	 */
	private void writeUserDataByFrame(int address, byte[] data) throws Exception{

		ArrayList<byte[]> list = mBuilder.buildDataWriteCommand(address, data);

		for(byte[] cmd: list){
			int blocks = (cmd.length + 15) / 16;
			//DebugUtil.addLogd("blocks:" + blocks);
			DebugUtil.addLogi("WWE: " +  DebugUtil.makeHexText(cmd));

			byte[] packet = createPacketForWrite(blocks, cmd);
			byte[] response = mFelica.transceive(packet);

			if(mProgressListener != null){
				mProgressListener.onProgress(mProgress + 1);
				mProgress++;
			}

			DebugUtil.addLogi("RES(Low): " +  DebugUtil.makeHexText(response));
		}
	}

	/**
	 * Reads the user data in the free information area on the smart-tag.
	 * @param  startAddress
	 * @param  sizeToRead
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] readUserData(int startAddress, int sizeToRead) throws Exception{
		byte[] result= new byte[sizeToRead];
		int maxReadLength = getMaxBlocks() * 16 - 16;
		int splitCount = (sizeToRead + maxReadLength - 1) / maxReadLength;

		int dataLen = (sizeToRead > maxReadLength ? maxReadLength:sizeToRead);
		int offset = 0;
		if(mProgressListener != null){
			mProgressListener.onMaxChanged(splitCount);
		}
		for(int i = 0; i < splitCount; i++){
			if(i == splitCount - 1){
				dataLen = sizeToRead - offset; //last frame
			}
			byte[] data = readUserDataByBlock(startAddress, dataLen);
			System.arraycopy(data, 0, result, offset, dataLen);

			offset += dataLen;
			startAddress += dataLen;

			if(mProgressListener != null){
				mProgressListener.onProgress(i + 1);
			}
		}
		return result;
	}

	/**
	 * Reads the user data in the free information area on the smart-tag.
	 * (Maximum concurrent transfer block number below)
	 * @param  readPos
	 * @param  readSize
	 * @return byte[]
	 * @throws Exception
	 */
	private byte[] readUserDataByBlock(int readPos, int readSize) throws Exception{
		//Address
		byte hAByte = (byte)(readPos >> 8);
		byte lAByte = (byte)(readPos & 0x00FF);

		//Length
		byte hLByte = (byte)(readSize >> 8);
		byte lLByte = (byte)(readSize & 0x00FF);

		//Send request for read.
		byte[] paramData = new byte[]{
			hAByte, lAByte, hLByte, lLByte,
			0, 0, 0, 0
		};
		writeCommand(COMMAND_DATA_READ, paramData);

		Thread.sleep(40);

		//Read data
		int blocks = (readSize + 15) / 16;

		byte[] data = readBlocks(blocks + 1);
		byte[] userData = Arrays.copyOfRange(data, 16, 16 + readSize);

		return userData;
	}

	/**
	 * Creates the write command of FeliCa.
	 */
	protected byte[] createPacketForWrite(int blocks, byte[] blockData){
		int len = 14 + 3 * blocks + 16 * blocks;
		byte[] packet = new byte[len];
		int pos = 0;
		packet[0] = (byte)len;
		packet[1] = (byte)0x08;
		pos = 2;
		//IDm
		System.arraycopy(mIdm, 0, packet, pos, 8);
		pos += 8;
		//service count
		packet[pos] = 0x01;
		pos++;
		//service code
		packet[pos] = 0x09;
		packet[pos + 1] = 0x00;
		pos+=2;
		//block count
		packet[pos] = (byte)blocks;
		pos++;
		//block list
		for(int i = 0; i < blocks; i++)
		{
			packet[pos] = (byte)0x00;
			packet[pos + 1] = (byte)i;
			packet[pos + 2] = (byte)0x04;
			pos += 3;
		}
		//block data
		System.arraycopy(blockData, 0, packet, pos, blockData.length);
		return packet;
	}

	/**
	 * Creates the read command of FeliCa.
	 */
	protected byte[] createPacketForRead(int blocks){
		int len = 14 + 3 * blocks;
		byte[] packet = new byte[len];
		int pos = 0;
		packet[0] = (byte)len;
		packet[1] = (byte)0x06;
		pos = 2;
		//IDm
		System.arraycopy(mIdm, 0, packet, pos, 8);
		pos += 8;
		//service count
		packet[pos] = 0x01;
		pos++;
		//service code
		packet[pos] = 0x09;
		packet[pos + 1] = 0x00;
		pos+=2;
		//block count
		packet[pos] = (byte)blocks;
		pos++;
		//block list
		for(int i = 0; i < blocks; i++)
		{
			packet[pos] = (byte)0x00;
			packet[pos + 1] = (byte)i;
			packet[pos + 2] = (byte)0x04;
			pos += 3;
		}

		return packet;
	}

	/**
	 * Returns the block-data from a reseponse command of FeliCa.
	 */
	protected byte[] getBlockData(byte[] response)
	{
		if(response.length < 13)
			return null;

		int blockCount = response[12];
		byte[] blockData = new byte[blockCount * 16];
		if(response.length < 13 + blockData.length)
			return null;

		System.arraycopy(response, 13, blockData, 0, blockData.length);

		return blockData;
	}

	/**
	 * Sends a reading command. （ReadWithoutEncription)
	 * @param blocks
	 */
	protected byte[] readBlocks(int blocks) throws Exception{
		//Read data
		byte[] packet = createPacketForRead(blocks);
		DebugUtil.addLogi("RWE(Low): " +  DebugUtil.makeHexText(packet));

		byte[] res = mFelica.transceive(packet);
		DebugUtil.addLogi("RES(Low): " +  DebugUtil.makeHexText(res));

		byte[] data = getBlockData(res);
		if(data == null)
			DebugUtil.addLogi("BlockData is null.");
		else
			DebugUtil.addLogi("BlockData: " + DebugUtil.makeHexText(data));

		return data;
	}

	/**
	 * Sends a writing command.
	 * @param functionNo
	 * @param paramData
	 * @param functionData
	 * @throws Exception
	 */
	private void writeCommand(byte functionNo,
			byte[] paramData,
			byte[] functionData) throws Exception {

		ArrayList<byte[]> list = mBuilder.buildCommand(functionNo, paramData, functionData);

		if(mProgressListener != null){
			mProgressListener.onMaxChanged(list.size());
		}

		for(int i = 0; i < list.size(); i++){
			if(i > 0){
				if(i == 1){
					Thread.sleep(15);
				}else{
					Thread.sleep(5);
				}
			}
			byte[] cmd = list.get(i);
			int blocks = (cmd.length + 15) / 16;

			DebugUtil.addLogi("WWE: " +  DebugUtil.makeHexText(cmd));
			DebugUtil.addLogi("blocks: " +  blocks);
			byte[] packet = createPacketForWrite(blocks, cmd);
			byte[] response = mFelica.transceive(packet);
			DebugUtil.addLogi("RES(Low): " +  DebugUtil.makeHexText(response));

			if(mProgressListener != null){
				mProgressListener.onProgress(i + 1);
			}
		}
	}

	/**
	 * Sends a writing command.
	 * @param functionNo
	 * @param paramData
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] writeCommand(
			byte functionNo,
			byte[] paramData) throws Exception {

		byte[] cmd = mBuilder.buildCommand(functionNo, paramData);
		DebugUtil.addLogi("WWE: " +  DebugUtil.makeHexText(cmd));

		byte[] packet = createPacketForWrite(1, cmd);

		byte[] response = mFelica.transceive(packet);
		DebugUtil.addLogi("RES(Low): " +  DebugUtil.makeHexText(response));
		return response;
	}


	private int getProgressMaxforWrite(int dataSize){
		final int unit = (getMaxBlocks() - 1) * 16;

		int count1 = dataSize / MAX_WRITE_SIZE;
		//DebugUtil.addLogi("count1 = " + count1);
		int count2 = 0;
		int total = 0;
		if(count1 > 0){
			//MAX_WRITE_SIZE以上の時
			count2 = (MAX_WRITE_SIZE + unit - 1) / unit;
			//DebugUtil.addLogi("count2 = " + count2);
			total = count1 * count2;
		}

		int mod = dataSize % MAX_WRITE_SIZE;
		if(mod > 0){
			count2 = (mod + unit - 1) / unit;
			total += count2;
		}
		//DebugUtil.addLogi("total = " + total);
		return total;
	}

	/**
	 * Starts the polling in order to detect whether the tag has a been released.
	 * @since 1.2.0
	 */
	public void startPollingForRelease(){
		try{
			new Thread(new Runnable() {
				@Override
				public void run() {
					pollForRelease();
				}
			}).start();

		}catch(Exception e){
			DebugUtil.addLoge("startPolling: " + e.toString());
		}
	}

	/**
	 * Requests to stop the polling.
	 * @since 1.2.0
	 */
	public void stopPolling(){
		mStopPolling = true;
	}

	/**
	 * Starts the polling in order to detect whether the tag has a been touched.
	 * @since 1.2.0
	 */
	private void pollForRelease(){
		DebugUtil.addLogi("Polling started.");

		try{
			if(!mFelica.isConnected()){
				connect();
			}
		}catch(Exception e){
			DebugUtil.addLoge("SmartTagCore#pollSmartTag(): " + e.toString());
			return;
		}

		mStopPolling = false;

		while(!mStopPolling){
			try{

				if(sendPollingCommand() == null){
					break;
				}

				Thread.sleep(400);
			}catch(IOException e){
				DebugUtil.addLoge("SmartTagCore#pollSmartTag(): " + e.toString());

				if(mTagActionListener != null){
					mTagActionListener.onReleased();
				}
				break;
			}catch(Exception e){
				DebugUtil.addLoge("SmartTagCore#pollSmartTag(): " + e.toString());
				break;
			}
		}

		try{
			close();
		}catch(Exception ignore){
		}

		DebugUtil.addLogi("Polling stopped.");
	}

	/**
	 * Sends a polling command.
	 * @throws Exception
	 * @since 1.2.0
	 */
	private byte[] sendPollingCommand() throws Exception{
		if(mSystemCode == null
				|| mSystemCode.length < 2){
			return null;
		}
		byte[] command = new byte[]{
				6, 0, mSystemCode[0], mSystemCode[1], 0, 0
		};

		return mFelica.transceive(command);
	}

	/**
	 * Converts two 12-bit numbers to the 3-bytes array.
	 * @param a
	 * @param b
	 * @return
	 */
	private static byte[] ConvertTo3Bytes(int a, int b)
    {
		byte[] result = new byte[3];
        result[0] = (byte)((a & 0x000FFF) >> 4);

        byte wk1 = (byte)((a & 0x0000000F) << 4);
        wk1 |= (char)((b & 0x00000F00) >> 8);
        result[1] = wk1;

        result[2] = (byte)(b & 0x000000FF);

        return result;
    }

	/**
	 * Returns the prodcut type from IDm.
	 * @param idm
	 * @return 0:unknown
	 */
	public static int getProductType(byte[] idm){
		if(idm == null) return 0;
		if(idm.length < 8) return 0;
		int type = 0;
		switch(idm[4]){
		case 0x12:
			type = SMARTTAG_27;
			break;
		case 0x31:
			type = SMARTCARD_29;
			break;
		}
		if(type == 0){
			int a = (idm[4] & 0xF0) >> 4;
			if(a == 0){
				type = SMARTTAG_20;
			}
		}
		return type;
	}
}
