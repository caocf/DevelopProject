package com.ebox.pub.utils;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRImageUtil {

	private static final int QR_WIDTH = 300;
	private static final int QR_HEIGHT = 300;

	// 要转换的地址或字符串,可以是中文
	public static Bitmap createQRImage(String url) {
		Bitmap bitmap = null;
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			bitMatrix = updateBit(bitMatrix, 5); // 生成新的bitMatrix
			int height = bitMatrix.getHeight();
			int width = bitMatrix.getWidth();
			// QR_WIDTH = width;
			// QR_HEIGHT = height;
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			bitmap = Bitmap
					.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			// 显示到一个ImageView上面
			// sweepIV.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private static BitMatrix updateBit(BitMatrix matrix, int margin) {
		int tempM = margin * 2;
		int[] rec = matrix.getEnclosingRectangle(); // 获取二维码图案的属性
		int resWidth = rec[2] + tempM;
		int resHeight = rec[3] + tempM;
		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
		resMatrix.clear();
		for (int i = margin; i < resWidth - margin; i++) { // 循环，将二维码图案绘制到新的bitMatrix中
			for (int j = margin; j < resHeight - margin; j++) {
				if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
					resMatrix.set(i, j);
				}
			}
		}
		return resMatrix;
	}

	/**
	 * 生成二维码图片方法(透明的)
	 * */
	public static Bitmap Create2DCode(String str) {
		int QR_WIDTH = 300, QR_HEIGHT = 300;
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE,
					QR_WIDTH, QR_HEIGHT);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static int IMAGE_HALFWIDTH = 30;

	/**
	 * 生成二维码 中间插入小图片
	 * 
	 * @param str
	 *            内容
	 * @return Bitmap
	 * @throws WriterException
	 */
	public static Bitmap createQRImage(String str, Bitmap icon) {
		// 缩放一个40*40的图片
		Bitmap bitmap = null;
		icon = zoomBitmap(icon, IMAGE_HALFWIDTH);
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		// hints.put(EncodeHintType.MARGIN, 1);
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix;
		try {
			matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE,
					QR_WIDTH, QR_WIDTH, hints);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			// 二维矩阵转为一维像素数组,也就是一直横着排了
			int halfW = width / 2;
			int halfH = height / 2;
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (x > halfW - IMAGE_HALFWIDTH
							&& x < halfW + IMAGE_HALFWIDTH
							&& y > halfH - IMAGE_HALFWIDTH
							&& y < halfH + IMAGE_HALFWIDTH) {
						pixels[y * width + x] = icon.getPixel(x - halfW
								+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
					} else {
						if (matrix.get(x, y))
						{
							pixels[y * width + x] = 0xff000000;
						} else { // 无信息设置像素点为白色
							pixels[y * width + x] = 0xffffffff;
						}
					}

				}
			}
			bitmap = Bitmap
					.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			// 通过像素数组生成bitmap
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	private static Bitmap zoomBitmap(Bitmap icon, int h) {
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2 * h / icon.getWidth();
		float sy = (float) 2 * h / icon.getHeight();
		m.setScale(sx, sy);
		// 重新构造一个2h*2h的图片
		return Bitmap.createBitmap(icon, 0, 0, icon.getWidth(),
				icon.getHeight(), m, false);
	}
}
