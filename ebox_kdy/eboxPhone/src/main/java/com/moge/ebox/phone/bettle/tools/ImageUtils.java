package com.moge.ebox.phone.bettle.tools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint({ "SimpleDateFormat", "SdCardPath" })
public class ImageUtils {
	public static final String CACHE_IMAGE_FILE_PATH = Environment
			.getExternalStorageDirectory() + "/vikaa/image/";
	public static final String SDCARD_MNT = "/mnt/sdcard";
	public static final String SDCARD = "/sdcard";
	public static final String DCIM = "/DCIM/Camera/";
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
	public static Activity mContext;
	public static void saveImage(Context context, String fileName, Bitmap bitmap)
			throws IOException {
		saveImage(context, fileName, bitmap, 100);
	}

	public static void saveImage(Context context, String fileName,
			Bitmap bitmap, int quality) throws IOException {
		if ((bitmap == null) || (fileName == null) || (context == null))
			return;

		FileOutputStream fos = context.openFileOutput(fileName, 0);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
		byte[] bytes = stream.toByteArray();
		fos.write(bytes);
		fos.close();
	}

	public static void saveImageToSD(String filePath, Bitmap bitmap, int quality)
			throws IOException {
		if (bitmap != null) {
			FileOutputStream fos = new FileOutputStream(filePath);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
			fos.close();
		}
	}

	public static void saveImageFile(String imageUrl, String fileName) {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			return;
		}
		File dir = new File(CACHE_IMAGE_FILE_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, fileName);
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			in = url.openStream();
			fos = new FileOutputStream(file);
			int len = -1;
			byte[] data = new byte[1024];
			while ((len = in.read(data)) != -1) {
				fos.write(data, 0, len);
				fos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (fos != null)
					fos.close();
				if (in != null)
					in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String saveBitmapByte(byte[] data, String fileName) {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			return null;
		}
		File dir = new File(CACHE_IMAGE_FILE_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(data, 0, data.length);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (fos != null)
					fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file.getAbsolutePath();
	}

	public static Bitmap getBitmap(Context context, String fileName) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = context.openFileInput(fileName);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				fis.close();
			} catch (Exception localException) {
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			try {
				fis.close();
			} catch (Exception localException1) {
			}
		} finally {
			try {
				fis.close();
			} catch (Exception localException2) {
			}
		}
		return bitmap;
	}

	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}

	public static Bitmap getBitmapByPath(String filePath,
			BitmapFactory.Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				fis.close();
			} catch (Exception localException) {
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			try {
				fis.close();
			} catch (Exception localException1) {
			}
		} finally {
			try {
				fis.close();
			} catch (Exception localException2) {
			}
		}
		return bitmap;
	}

	public static Bitmap getBitmapByPath(String filePath,
			BitmapFactory.Options opts, int size) {
		Bitmap bitmap = null;
		try {
			int rotation = getExifOrientation(filePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			int bigOne = options.outWidth > options.outHeight ? options.outWidth
					: options.outHeight;
			if (bigOne > size) {
				options.inSampleSize = (bigOne / size);
			}
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(filePath, options);
			if (bitmap == null)
				return null;
			Matrix matrix = new Matrix();
			if (rotation != 0) {
				matrix.setRotate(rotation);
			}
			Bitmap finalBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			if (finalBitmap != bitmap) {
				bitmap.recycle();
			}
			return finalBitmap;
		} catch (Exception e) {
			Logger.i(e);
		}
		return null;
	}

	public static void compressBitmapAndWriteToFile(String filePath,
			BitmapFactory.Options opts, int size, String newFilePath) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			Logger.i(options.outWidth + ", " + options.outHeight);
			int bigOne = options.outWidth > options.outHeight ? options.outHeight
					: options.outWidth;
			Logger.i(String.valueOf(bigOne));
			if (bigOne > size) {
				int Ratio = Math.round(bigOne / size);
				Logger.i(String.valueOf(Ratio));
				options.inSampleSize = Ratio;
			}
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(filePath, options);
			Logger.i(bitmap.getWidth() + ", " + bitmap.getHeight());
			FileOutputStream fos = new FileOutputStream(newFilePath);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmapByImageUrl(String imageUrl,
			BitmapFactory.Options opts, int size) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(imageUrl);

			opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(url.openStream(), null, opts);
			int bigOne = opts.outWidth > opts.outHeight ? opts.outWidth
					: opts.outHeight;
			if (bigOne > size) {
				opts.inSampleSize = (bigOne / size);
			}
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(url.openStream(), null, opts);
			if (bitmap == null)
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static void saveImageFileInDirNamedFileNameWithImageUrl(
			String imageUrl, String dirpath, String fileName) {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			return;
		}
		File dir = new File(dirpath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, fileName);
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			int length = conn.getContentLength();
			Logger.i(String.valueOf(length));
			if (length == -1) {
				length = 260000;
			}
			int count = 0;
			int progress = 0;

			in = url.openStream();
			fos = new FileOutputStream(file);
			int len = -1;
			byte[] data = new byte[1024];
			while ((len = in.read(data)) != -1) {
				count += len;
				progress = (int) (count / length * 100.0F);

				fos.write(data, 0, len);
				fos.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (fos != null)
					fos.close();
				if (in != null)
					in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap getBitmapByFile(File file) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				fis.close();
			} catch (Exception localException) {
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			try {
				fis.close();
			} catch (Exception localException1) {
			}
		} finally {
			try {
				fis.close();
			} catch (Exception localException2) {
			}
		}
		return bitmap;
	}

	public static String getTempFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
		String fileName = format.format(new Timestamp(System
				.currentTimeMillis()));
		return fileName;
	}

	public static String getCamerPath() {
		return Environment.getExternalStorageDirectory() + File.separator
				+ "FounderNews" + File.separator;
	}

	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
		String filePath = null;

		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file:///sdcard" + File.separator;
		String pre2 = "file:///mnt/sdcard" + File.separator;

		if (mUriString.startsWith(pre1)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre2.length());
		}
		return filePath;
	}

	public static String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";
		String[] proj = { "_data" };

		Cursor cursor = context.managedQuery(uri, proj, null, null, null);

		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow("_data");
			if ((cursor.getCount() > 0) && (cursor.moveToFirst())) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	public static Bitmap loadImgThumbnail(Activity context, String imgName,
			int kind) {
		Bitmap bitmap = null;

		String[] proj = { "_id", "_display_name" };

		Cursor cursor = context.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
				"_display_name='" + imgName + "'", null, null);

		if ((cursor != null) && (cursor.getCount() > 0)
				&& (cursor.moveToFirst())) {
			ContentResolver crThumb = context.getContentResolver();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			bitmap = MethodsCompat.getThumbnail(crThumb, cursor.getInt(0),
					kind, options);
		}
		return bitmap;
	}

	public static Bitmap loadImgThumbnail(String filePath, int w, int h) {
		Bitmap bitmap = getBitmapByPath(filePath);
		return zoomBitmap(bitmap, w, h);
	}

	public static String getLatestImage(Activity context) {
		String latestImage = null;
		String[] items = { "_id", "_data" };

		Cursor cursor = context.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, items, null,
				null, "_id desc");

		if ((cursor != null) && (cursor.getCount() > 0)) {
			cursor.moveToFirst();
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				latestImage = cursor.getString(1);
			}

		}

		return latestImage;
	}

	public static int[] scaleImageSize(int[] img_size, int square_size) {
		if ((img_size[0] <= square_size) && (img_size[1] <= square_size))
			return img_size;
		double ratio = square_size / Math.max(img_size[0], img_size[1]);
		return new int[] { (int) (img_size[0] * ratio),
				(int) (img_size[1] * ratio) };
	}

	public static void createImageThumbnail(Context context,
			String largeImagePath, String thumbfilePath, int square_size,
			int quality) throws IOException {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;

		Bitmap cur_bitmap = getBitmapByPath(largeImagePath, opts);

		if (cur_bitmap == null)
			return;

		int[] cur_img_size = { cur_bitmap.getWidth(), cur_bitmap.getHeight() };

		int[] new_img_size = scaleImageSize(cur_img_size, square_size);

		Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0],
				new_img_size[1]);

		saveImageToSD(thumbfilePath, thb_bitmap, quality);
	}

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		Bitmap newbmp = null;
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidht = w / width;
			float scaleHeight = h / height;
			matrix.postScale(scaleWidht, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		}
		return newbmp;
	}

	public static Bitmap scaleBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int newWidth = 200;
		int newHeight = 200;

		float scaleWidth = newWidth / width;
		float scaleHeight = newHeight / height;

		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}

	public static Bitmap reDrawBitMap(Activity context, Bitmap bitmap) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int rWidth = dm.widthPixels;

		int width = bitmap.getWidth();
		float zoomScale;

		if (width >= rWidth)
			zoomScale = rWidth / width;
		else {
			zoomScale = 1.0F;
		}
		Matrix matrix = new Matrix();

		matrix.postScale(zoomScale, zoomScale);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != -1 ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		int color = -12434878;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(-12434878);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1.0F, -1.0F);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, height
				+ height / 2, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0.0F, height, width, height + 4, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0.0F, height + 4, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0.0F, bitmap.getHeight(),
				0.0F, bitmapWithReflection.getHeight() + 4, 1895825407,
				16777215, Shader.TileMode.CLAMP);
		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		canvas.drawRect(0.0F, height, width,
				bitmapWithReflection.getHeight() + 4, paint);

		return bitmapWithReflection;
	}

	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	public static String getImageType(File file) {
		if ((file == null) || (!file.exists())) {
			return null;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			String type = getImageType(in);
			return type;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException localIOException3) {
			}
		}
	}

	public static String getImageType(InputStream in) {
		if (in == null)
			return null;
		try {
			byte[] bytes = new byte[8];
			in.read(bytes);
			return getImageType(bytes);
		} catch (IOException e) {
		}
		return null;
	}

	public static String getImageType(byte[] bytes) {
		if (isJPEG(bytes)) {
			return "image/jpeg";
		}
		if (isGIF(bytes)) {
			return "image/gif";
		}
		if (isPNG(bytes)) {
			return "image/png";
		}
		if (isBMP(bytes)) {
			return "application/x-bmp";
		}
		return null;
	}

	private static boolean isJPEG(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == -1) && (b[1] == -40);
	}

	private static boolean isGIF(byte[] b) {
		if (b.length < 6) {
			return false;
		}
		return (b[0] == 71) && (b[1] == 73) && (b[2] == 70) && (b[3] == 56)
				&& ((b[4] == 55) || (b[4] == 57)) && (b[5] == 97);
	}

	private static boolean isPNG(byte[] b) {
		if (b.length < 8) {
			return false;
		}
		return (b[0] == -119) && (b[1] == 80) && (b[2] == 78) && (b[3] == 71)
				&& (b[4] == 13) && (b[5] == 10) && (b[6] == 26) && (b[7] == 10);
	}

	private static boolean isBMP(byte[] b) {
		if (b.length < 2) {
			return false;
		}
		return (b[0] == 66) && (b[1] == 77);
	}

	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			Logger.i(ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt("Orientation", -1);
			if (orientation != -1)
				switch (orientation) {
				case 6:
					degree = 90;
					break;
				case 3:
					degree = 180;
					break;
				case 8:
					degree = 270;
				case 4:
				case 5:
				case 7:
				}
		}
		return degree;
	}

	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int height = bmpOriginal.getHeight();
		int width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0.0F);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0.0F, 0.0F, paint);
		return bmpGrayscale;
	}

	public static Bitmap createScaledBitmap(Bitmap unscaledBitmap,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(),
				dstRect.height(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(2));
		return scaledBitmap;
	}

	public static Rect calculateSrcRect(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.CROP) {
			float srcAspect = srcWidth / srcHeight;
			float dstAspect = dstWidth / dstHeight;
			if (srcAspect > dstAspect) {
				int srcRectWidth = (int) (srcHeight * dstAspect);
				int srcRectLeft = (srcWidth - srcRectWidth) / 2;
				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth,
						srcHeight);
			}
			int srcRectHeight = (int) (srcWidth / dstAspect);
			int scrRectTop = (srcHeight - srcRectHeight) / 2;
			return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
		}

		return new Rect(0, 0, srcWidth, srcHeight);
	}

	public static Rect calculateDstRect(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			float srcAspect = srcWidth / srcHeight;
			float dstAspect = dstWidth / dstHeight;
			if (srcAspect > dstAspect) {
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			}
			return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
		}

		return new Rect(0, 0, dstWidth, dstHeight);
	}

	public static Bitmap decodeFile(String pathName, int dstWidth,
			int dstHeight, ScalingLogic scalingLogic) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateSampleSize(options.outWidth,
				options.outHeight, dstWidth, dstHeight, scalingLogic);
		Bitmap unscaledBitmap = BitmapFactory.decodeFile(pathName, options);
		return unscaledBitmap;
	}

	public static int calculateSampleSize(int srcWidth, int srcHeight,
			int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.CROP) {
			float srcAspect = srcWidth / srcHeight;
			float dstAspect = dstWidth / dstHeight;
			if (srcAspect > dstAspect) {
				return srcWidth / dstWidth;
			}
			return srcHeight / dstHeight;
		}

		float srcAspect = srcWidth / srcHeight;
		float dstAspect = dstWidth / dstHeight;
		if (srcAspect > dstAspect) {
			return srcHeight / dstHeight;
		}
		return srcWidth / dstWidth;
	}

	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5F);
	}

	public static int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5F);
	}

	public static int sp2px(Context context, float spValue) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scale + 0.5F);
	}

	@TargetApi(13)
	public static int getDisplayWidth(Context context) {
		int width = 0;
		WindowManager wm = (WindowManager) context.getSystemService("window");
		int sdk = Build.VERSION.SDK_INT;
		if (sdk < 13) {
			width = wm.getDefaultDisplay().getWidth();
		} else {
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
		}
		return width;
	}

	@TargetApi(13)
	public static int getDisplayHeighth(Context context) {
		int height = 0;
		WindowManager wm = (WindowManager) context.getSystemService("window");
		int sdk = Build.VERSION.SDK_INT;
		if (sdk < 13) {
			height = wm.getDefaultDisplay().getHeight();
		} else {
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			height = size.y;
		}
		return height;
	}

	@TargetApi(13)
	public static String getScreenSize(Context context) {
		String client_screen = null;
		WindowManager wm = (WindowManager) context.getSystemService("window");
		int sdk = Build.VERSION.SDK_INT;
		if (sdk < 13) {
			client_screen = wm.getDefaultDisplay().getWidth() + "x"
					+ wm.getDefaultDisplay().getHeight();
		} else {
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			client_screen = size.x + "x" + size.y;
		}
		return client_screen;
	}

	public static int getImageSizeWithFrameDP(int FrameDP) {
		float pix = dip2px(AppManager.getAppManager().currentActivity(),
				FrameDP);
		return (int) (getDisplayWidth(AppManager.getAppManager()
				.currentActivity()) - 2.0F * pix);
	}

	public static void PhotoChooseOption(Activity context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		CharSequence[] item = { "拍照", "相册" };
		builder.setItems(item, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
				case 0:
					String savePath = "";

					String storageState = Environment.getExternalStorageState();
					if (storageState.equals("mounted")) {
						savePath = Environment.getExternalStorageDirectory()
								.getAbsolutePath() + "/DCIM/Camera/";
						File savedir = new File(savePath);
						if (!savedir.exists()) {
							savedir.mkdirs();
						}

					}

					if (StringUtils.empty(savePath)) {
						Toast.makeText(mContext, "没有SD卡", 0).show();
						return;
					}

					String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
							.format(new Date());
					String fileName = timeStamp + ".jpg";
					File out = new File(savePath, fileName);
					Uri uri = Uri.fromFile(out);

					Intent intent = new Intent(
							"android.media.action.IMAGE_CAPTURE");
					intent.putExtra("output", uri);

					mContext.startActivityForResult(intent, 1);
					break;
				case 1:
					Intent intent1 = new Intent(
							"android.intent.action.GET_CONTENT");
					intent1.addCategory("android.intent.category.OPENABLE");
					intent1.setType("image/*");
					mContext.startActivityForResult(
							Intent.createChooser(intent1, "选择图片"), 0);
				}
			}
		});
		builder.create().show();
	}

	public static Bitmap getSmallBitmap(String filePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options, 600, 600);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	public static Bitmap getSmallBitmap(String filePath, int pixel) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options, pixel, pixel);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;

		if ((height > reqHeight) || (width > reqWidth)) {
			int heightRatio = Math.round(height / reqHeight);
			int widthRatio = Math.round(width / reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static enum ScalingLogic {
		CROP, FIT;
	}
}