/*
 * Copyright (c) 2013, AIOI・SYSTEMS CO., LTD.
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

import java.util.ArrayList;

/**
 * Imprements the creation of the command.
 * @author AIOI SYSTEMS CO., LTD.
 * @version 1.2.0
 */
public class CommandBuilder {
	public static final byte COMMAND_DATA_WRITE =  (byte) 0xB0;
	public static final byte COMMAND_CHANGE_SECURITY_CODE = (byte)0xBD;

	private static final byte COMMAND_DATA_READ = (byte)0xC0;
	private static final byte COMMAND_CLEAR = (byte)0xA1;
	private static final byte COMMAND_SHOW_DISPLAY_OLD = (byte)0xA0;
	private static final byte COMMAND_SAVE_LAYOUT = (byte)0xB2;
	private static final byte COMMAND_SHOW_DISPLAY = (byte)0xA2;
	private static final byte COMMAND_SHOW_DISPLAY3 = (byte)0xA3;

	public static final byte SECURITY_CODE_TYPE1 = 1;
	public static final byte SECURITY_CODE_TYPE2 = 2;
	public static final byte SECURITY_CODE_TYPE3 = 3;

	private int mMaxBlocks = 8;
	private byte mSeq = 0;

	private byte[] mSecCode1;
	private byte[] mSecCode2;
	private byte[] mSecCode3;

	public CommandBuilder(){
		mSecCode1 = new byte[]{0x30, 0x30, 0x30};
		mSecCode2 = new byte[]{0x30, 0x30, 0x30};
		mSecCode3 = new byte[]{0x30, 0x30, 0x30};
	}

	public int getMaxBlocks() {
		return mMaxBlocks;
	}

	public void setMaxBlocks(int maxBlocks) {
		this.mMaxBlocks = maxBlocks;
	}

	public void setSecurityCode1(byte[] code){
		mSecCode1 = code;
	}
	public void setSecurityCode2(byte[] code){
		mSecCode2 = code;
	}
	public void setSecurityCode3(byte[] code){
		mSecCode3 = code;
	}

	/**
	 * Creates a command when there is no function data.
	 * @param functionNo
	 * @param paramData
	 * @return
	 */
	public byte[] buildCommand(byte functionNo,
			byte[] paramData){
		ArrayList<byte[]> list = buildCommand(functionNo,
				paramData, null);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	/***
	 * Creates smart-tag command.
	 * Divides command into some frames if necessary.
	 * @param functionNo
	 * @param paramData
	 * @param innerData
	 * @return
	 * @throws Exception
	 */
	public ArrayList<byte[]> buildCommand(byte functionNo,
			byte[] paramData,
			byte[] functionData) {

		int dataBlocks;
		byte[] innerData = null;
		int splitCount = 0;
		if(functionData == null){
			dataBlocks = 0;
			splitCount = 1;
		}else{
			dataBlocks = functionData.length / 16;
			if( functionData.length % 16 > 0){
				//functionDataは16で割り切れるようにする
				dataBlocks++;
				innerData = new byte[dataBlocks * 16];
				System.arraycopy(functionData, 0, innerData, 0, functionData.length);
			}else{
				innerData = functionData;
			}
			//フレーム分割数
			splitCount = getSplitCount(dataBlocks);
		}

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		int offset = 0;
		int frameBlocks = 0;
		for(int i = 0; i < splitCount; i++){
			int dataLen;
			if(i == splitCount - 1){
				//last frame
				frameBlocks = getLastBlockCount(dataBlocks) + 1;
				//Common.addLogi(String.format("last block count = %d", frameBlocks));
				if(innerData == null){
					dataLen = 0;
				}else{
					dataLen = innerData.length - offset;
				}
			}else{
				frameBlocks = mMaxBlocks;
				dataLen = (frameBlocks - 1) * 16;
			}

			byte[] cmd = new byte[frameBlocks * 16];
			cmd[0] = functionNo;
			cmd[1] = (byte)splitCount;
			cmd[2] = (byte)(i + 1);
			cmd[3] = (byte)((frameBlocks - 1) * 16);

			if(functionNo == (byte)0xd0){
				cmd[4] = 0;
			}else{
				cmd[4] = getNextSeq();
			}

			//security code
			byte[] secCode = getSecurityCode(functionNo, paramData);
			if(secCode != null){
				cmd[5] = secCode[0];
				cmd[6] = secCode[1];
				cmd[7] = secCode[2];
			}else{
				cmd[5] = 0x30;
				cmd[6] = 0x30;
				cmd[7] = 0x30;
			}

			//set function parameter data.
			System.arraycopy(paramData, 0, cmd, 8, paramData.length);

			//set function data.
			if(innerData != null){
				System.arraycopy(innerData, offset, cmd, 16, dataLen);
			}
			//Common.addLogi("command: " +  Common.makeHexText(cmd));
			result.add(cmd);
			offset += dataLen;
		}
		return result;
	}

	/**
	 * Creates a command to write user data.
	 * Divides command into some frames if necessary.
	 * @param startAdress
	 * @param functionData
	 * @return
	 */
	public ArrayList<byte[]> buildDataWriteCommand(int startAdress, byte[] functionData) {
		int unit = mMaxBlocks * 16 - 16;
		int splitCount = (functionData.length + unit - 1) / unit;

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		int offset = 0;
		int dataLen = (mMaxBlocks - 1) * 16;//ヘッダを除いたデータのみのサイズ
		int frameBlocks = 0;
		for(int i = 0; i < splitCount; i++){
			if(i == splitCount - 1){
				//last block
				dataLen = functionData.length - offset;
				frameBlocks = (dataLen + 15) / 16;
				frameBlocks++;
			}else{
				frameBlocks = mMaxBlocks;
			}

			byte[] cmd = new byte[frameBlocks * 16];
			cmd[0] = COMMAND_DATA_WRITE;
			cmd[1] = (byte)splitCount;
			cmd[2] = (byte)(i + 1);
			cmd[3] = (byte)dataLen;
			cmd[4] = getNextSeq();

			//security code
			if(mSecCode2 != null){
				cmd[5] = mSecCode2[0];
				cmd[6] = mSecCode2[1];
				cmd[7] = mSecCode2[2];
			}else{
				cmd[5] = 0x30;
				cmd[6] = 0x30;
				cmd[7] = 0x30;
			}
			//Adress
			int adress = startAdress+offset;
			byte hAByte = (byte)(adress >> 8);
			byte lAByte = (byte)(adress & 0x00FF);

			//Length
			byte hLByte = (byte)(dataLen >> 8);
			byte lLByte = (byte)(dataLen & 0x00FF);

			byte[] paramData = new byte[]{
					hAByte,lAByte, hLByte, lLByte,
					0, 0, 0, 0
			};
			//set function parameter data.
			System.arraycopy(paramData, 0, cmd, 8, paramData.length);
			//set function data.
			if(functionData != null ){
				System.arraycopy(functionData, offset, cmd, 16, dataLen);
			}
			result.add(cmd);

			offset += dataLen;
		}
		return result;
	}

	public void setSeq(byte seq){
		this.mSeq = seq;
	}

	private int getSplitCount(int totalBlocks){
		int unit = mMaxBlocks - 1;
		return (totalBlocks + unit - 1) / unit;
	}

	private int getLastBlockCount(int dataBlocks){
		if(dataBlocks == 0)
			return 0;

		int mod = dataBlocks % (mMaxBlocks - 1);
		if(mod == 0){
			return mMaxBlocks - 1;
		}else{
			return mod;
		}
	}

	private byte getNextSeq(){
		byte result = mSeq;
		mSeq++;
		return result;
	}

	/**
	 * Returns the security code.
	 * @param functionNo
	 * @return
	 */
	private byte[] getSecurityCode(byte functionNo, byte[] paramData){
		byte[] code = null;
		switch(functionNo){
			case COMMAND_SHOW_DISPLAY_OLD:
			case COMMAND_SHOW_DISPLAY:
			case COMMAND_SHOW_DISPLAY3:
			case COMMAND_CLEAR:
			case COMMAND_SAVE_LAYOUT:
				code = mSecCode1;
				break;
			case COMMAND_DATA_WRITE:
				code = mSecCode2;
				break;
			case COMMAND_DATA_READ:
				code = mSecCode3;
				break;
			case COMMAND_CHANGE_SECURITY_CODE:
				code = getSecurityCodeByType(paramData[0]);
				break;
			default:
				code = new byte[]{0x30, 0x30, 0x30};
				break;
		}
		return code;
	}

	/**
	 * Returns the security code.
	 * @param type
	 * @return
	 */
	private byte[] getSecurityCodeByType(byte type){
		switch(type){
		case SECURITY_CODE_TYPE1:
			return mSecCode1;
		case SECURITY_CODE_TYPE2:
			return mSecCode2;
		case SECURITY_CODE_TYPE3:
			return mSecCode3;
		}
		return null;
	}
}
