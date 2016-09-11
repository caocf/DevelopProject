package com.ebox.Anetwork;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.ebox.pub.utils.BitmapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

	private static final String TAG = "BitmapLruCache";
	private String CACHE_PAHT;

	public BitmapLruCache(int maxSize) {
		super(maxSize);
		CACHE_PAHT= Environment.getExternalStorageDirectory()+"/BoxCache";

		File file = new File(CACHE_PAHT);
		if (!file.exists())
		{
			file.mkdir();
		}
	}

	@Override
	protected int sizeOf(String key, Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = get(url);

		if (bitmap==null) {
			bitmap = BitmapUtil.readBitmap(CACHE_PAHT+"/"+getFileName(url));
		}

		return bitmap;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);

		if (bitmap==null) {
			return;
		}

		File file = new File(CACHE_PAHT, getFileName(url));

		try {
			Log.d(TAG, "save to sdcard:"+file.toString());
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream outputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFileName(String url) {
		try {
			if (url != null) {
                int index = url.lastIndexOf("/");
                int last = url.indexOf("#W");
                return url.substring(index+1,last);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
