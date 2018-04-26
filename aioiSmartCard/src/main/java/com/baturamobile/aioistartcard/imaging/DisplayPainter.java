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

package com.baturamobile.aioistartcard.imaging;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;

/**
 * Implements functions to create a display image of smart-tag.
 * @author AIOI SYSTEMS CO., LTD.
 * @version 1.3.0
 */
public class DisplayPainter {

	public static final int DISPLAY_TYPE_200X96 = 1;
	public static final int DISPLAY_TYPE_264X176 = 2;
	public static final int DISPLAY_TYPE_300X200 = 4;

	private Bitmap mScreen = null;
	private Canvas mCanvas = null;

	public DisplayPainter(){
		mScreen = Bitmap.createBitmap(200, 96, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mScreen);
		clearDisplay();
	}

	public DisplayPainter(int displaySizeType){
		switch(displaySizeType){
		case DISPLAY_TYPE_200X96:
			mScreen = Bitmap.createBitmap(200, 96, Bitmap.Config.ARGB_8888);
			break;
		case DISPLAY_TYPE_264X176:
			mScreen = Bitmap.createBitmap(264, 176, Bitmap.Config.ARGB_8888);
			break;
		case DISPLAY_TYPE_300X200:
			mScreen = Bitmap.createBitmap(300, 200, Bitmap.Config.ARGB_8888);
			break;
		}
		mCanvas = new Canvas(mScreen);
		clearDisplay();
	}

	/**
	 * Clears the image.
	 */
	public void clearDisplay(){
		mCanvas.drawColor(Color.WHITE);
	}

	/**
	 * Returns a created image.
	 * @return Bitmap
	 */
	public Bitmap getPreviewImage(){
		return mScreen;
	}

	/**
	 * Draws a text.
	 * @param text
	 * @param x
	 * @param y
	 */
	public void putText(
			String text,
			int x,
			int y,
			int size){

		Paint paint = new Paint();
    	paint.setAntiAlias(false);
    	paint.setColor(Color.BLACK);
    	paint.setTextSize(size);
    	FontMetrics metrics = paint.getFontMetrics();
    	float top = y - metrics.top;
    	mCanvas.drawText(text, x, top, paint);
	}

	/**
	 * Draws a line.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param size
	 * @param inverted 反転する場合はtrue
	 * @param dashed 点線を引く場合はtrue、実線はfalse。
	 */
	public void putLine(
			int x1,
			int y1,
			int x2,
			int y2,
			int size,
			boolean invert,
			boolean dashed){

		Paint paint = new Paint();
    	paint.setAntiAlias(false);
    	if(invert){
    		paint.setColor(Color.WHITE);
    	}else{
    		paint.setColor(Color.BLACK);
    	}
    	paint.setStrokeWidth(size);
    	paint.setStyle(Paint.Style.STROKE);
    	if(dashed){
    		paint.setPathEffect(new DashPathEffect(new float[]{2.0f, 2.0f}, 0.0f));
    	}
    	mCanvas.drawLine(x1, y1, x2, y2, paint);
	}

	public void putLine(
			int x1,
			int y1,
			int x2,
			int y2,
			int size){
		putLine(x1, y1, x2, y2, size, false, false);
	}

	/**
	 * Draws a rectangle.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param border
	 */
	public void putRectangle(
			int x,
			int y,
			int width,
			int height,
			int border){

		Paint paint = new Paint();
    	paint.setAntiAlias(false);
    	paint.setColor(Color.BLACK);
    	paint.setStrokeWidth(border);
    	paint.setStyle(Paint.Style.STROKE);

    	mCanvas.drawRect(x, y, x + width - 1, y + height - 1, paint);

	}

	/**
	 * Draws a bitmap.
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param dither
	 */
	public void putImage(Bitmap bitmap, int x, int y, boolean dither ){
		if(bitmap == null)
			return;

		if(dither){
			bitmap = getDitheredImage(bitmap);
		}else{
			bitmap = getThresholdImage(bitmap);
		}

		Canvas canvas = new Canvas(mScreen);
		canvas.drawBitmap(bitmap, x, y, null);
	}



	/**
	 * Gets a block and white image with dithering.
	 * @return Bitmap
	 */
	private static Bitmap getDitheredImage(Bitmap bitmap){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		char[] src = getGrayPixels(bitmap);
		char[] dst = new char[width * height];

		//Floyd & steinberg
		double[][] df = {
			{      -1,       -1, 7.0/16.0},
			{3.0/16.0, 5.0/16.0, 1.0/16.0}
		};
		int dfRows = df.length;
		int dfCols = df[0].length;
		int xRange = (dfCols - 1) / 2;

		boolean d;
		double err;
		int xx, yy;

		int index = 0;

		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				index = x + y * width;
				char pixel = src[index];

				if(pixel > 127){
					d = true;
					dst[index] = 255;
				}else{
					d = false;
				}

				if(d){
					err = pixel - 255;
				}else{
					err = pixel;
				}

				for(int iy = 0; iy < dfRows; iy++){
					for(int ix = -xRange; ix <= xRange; ix++){
						xx = x + ix;

						if((xx < 0) | (xx > width - 1)){
							continue;
						}
						yy = y + iy;
						if(yy > height - 1){
							continue;
						}
						if(df[iy][ix + xRange] < 0){
							continue;
						}

						double wk = src[xx + yy * width] +
								err * df[iy][ix + xRange];
						src[xx + yy * width] =
							adjustByte(wk);
					}
				}
			}
		}

		return createBlackWhiteImage(dst, width, height);
	}

	private static char adjustByte(double value){
		if(value < 0){
			value = 0;
		}else if(value > 255){
			value = 255;
		}
		return (char)value;
	}

	/**
	 * カラー画像からグレイスケールのピクセル配列データを取得する
	 * Gets a grayscaled image from a colored image.
	 * @param bitmap
	 * @return
	 */
	private static char[] getGrayPixels(Bitmap bitmap){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] src = new int[width * height];
		bitmap.getPixels(src, 0, width, 0, 0, width, height);
		Point range = getPixelRange(src);
		float adjust = (float)255 / (range.y - range.x);

		char[] dst = new char[width * height];

		int index = 0;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				index = x + y * width;
				int pixel = src[index];

				char tmp = (char)getAverage(Color.red(pixel),
						Color.green(pixel),
						Color.blue(pixel));

				tmp -= range.x;
				dst[index] = (char)(tmp * adjust);
			}
		}
		return dst;
	}

	/**
	 * Creates a block and white image.
	 * @param pixels
	 * @return Bitmap
	 */
	private static Bitmap createBlackWhiteImage(char[] pixels, int width, int height){
		int[] pixelsInt = new int[width * height];

		int index;
		int value;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				index = x + y * width;
				value = pixels[index];
				pixelsInt[index] =
					Color.argb(255, value, value, value);
			}
		}
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bmp.setPixels(pixelsInt, 0, width, 0, 0, width, height);
		return bmp;
	}

	/**
	 * Convert to a black and white image with threshold.
	 * @param bitmap
	 * @return Bitmap
	 */
	private static Bitmap getThresholdImage(Bitmap bitmap){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		char[] src = getGrayPixels(bitmap);
		char[] dst = new char[width * height];

		int index = 0;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				index = x + y * width;
				char pixel = src[index];
				if(pixel > 127){
					dst[index] = 255;
				}else{
					dst[index] = 0;
				}
			}
		}

		return createBlackWhiteImage(dst, width, height);
	}

	/**
	 * Create a image data for smart-tag.
	 * @param bitmap
	 * @param dither
	 * @return byte[]
	 * @since 1.1.0
	 */
	public static byte[] getImage(Bitmap bitmap, boolean dither){

		if(dither){
			bitmap = getDitheredImage(bitmap);
		}else{
			bitmap = getThresholdImage(bitmap);
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

		int cols = (width + 7) / 8;
		int size = cols * height;
		byte[] result = new byte[size];

		int index = 0;
		byte bitPos = 7;
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int pixel = pixels[x + y * width];

				if(Color.red(pixel) == 0){
					byte color = 1;
					result[index] |= (color << bitPos);
				}

				bitPos--;
				if(bitPos < 0
						|| x == (width - 1)/*1ラインの最終ピクセル*/){
					index++;
					bitPos = 7;
				}
			}
		}

		return result;
	}

	/**
	 * Create a image data for smart-tag.
	 * @return
	 */
	public byte[] getLocalDisplayImage(){
		return getImage(mScreen, false);
	}

	private static int getAverage(int red, int green, int blue){
		//return (int)((float)(red + green + blue) / 3);
		//NTSC加重平均法
		return (int)(red * 0.298912f
				+ green * 0.586611f
				+ blue * 0.114478f);
	}


	private static Point getPixelRange(int[] pixels){

		int max = 0;
		int min = 255;
		for(int i = 0; i < pixels.length; i++){
			int pixel = pixels[i];

			int value = getAverage(Color.red(pixel),
					Color.green(pixel),
					Color.blue(pixel));
			max = Math.max(value, max);
			min = Math.min(value, min);
		}
		return new Point(min, max);
	}
}
