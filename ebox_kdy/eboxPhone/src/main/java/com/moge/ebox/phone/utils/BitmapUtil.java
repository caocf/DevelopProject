package com.moge.ebox.phone.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class BitmapUtil
{
    private static final String LOG_TAG = "BitmapUtil";

    public static final int INT_ORIENTATION_HORIZONTAL = 0;
    public static final int INT_ORIENTATION_VERTICAL = 1;

    public static final int INT_FULL_SCLAE = 0;
    public static final int INT_UN_FULL_SCLAE = 1;
    public static final int INT_NO_SCLAE = 2;
    private static int m_iMaxBitmapSize = 1 * 1024 * 1024;

    private static int m_iDefaultMaxWidth = 300;

    private static int m_iDefaultMaxHeight = 400;

    public static void setMaxBitmapSize(int iSize)
    {
        m_iMaxBitmapSize = iSize;
    }

    public static void setDefaultMaxSize(int iMaxWidth, int iMaxHeight)
    {
        m_iDefaultMaxWidth = iMaxWidth;
        m_iDefaultMaxHeight = iMaxHeight;
    }

    public static Bitmap scaleBitmap(int idstWidth, int idstHeight,
            Bitmap srcBitmap, int scaleStyle)
    {
        if (null == srcBitmap)
        {
            return null;
        }

        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();

        Bitmap bitmap = null;

        try
        {
            switch (scaleStyle)
            {
                case INT_NO_SCLAE:
                {
                    bitmap = srcBitmap;
                    break;
                }
                case INT_FULL_SCLAE:
                {
                    bitmap = Bitmap.createScaledBitmap(srcBitmap, idstWidth,
                            idstHeight, true);
                    break;
                }
                case INT_UN_FULL_SCLAE:
                {
                    if (width > idstWidth && height > idstHeight)
                    {
                        float scale = 0;
                        float scaleWidth = (float) width / (float) idstWidth;
                        float scaleHeight = (float) height / (float) idstHeight;
                        if (scaleWidth > scaleHeight)
                        {
                            scale = scaleWidth;
                        }
                        else
                        {
                            scale = scaleHeight;
                        }
                        int tmpWidth = (int) ((float) width / scale);
                        int tmpHigth = (int) ((float) height / scale);
                        bitmap = Bitmap.createScaledBitmap(srcBitmap, tmpWidth,
                                tmpHigth, true);
                    }
                    else if (width <= idstWidth && height > idstHeight)
                    {
                        int tmpWidth = (int) ((float) idstHeight
                                * (float) width / (float) height);
                        bitmap = Bitmap.createScaledBitmap(srcBitmap, tmpWidth,
                                idstHeight, true);
                    }
                    else if (width > idstWidth && height <= idstHeight)
                    {
                        int tmpHeight = (int) ((float) idstWidth
                                * (float) height / (float) width);
                        bitmap = Bitmap.createScaledBitmap(srcBitmap,
                                idstWidth, tmpHeight, true);
                    }
                    else
                    {
                        float scale = 0;
                        float scaleWidth = (float) idstWidth / (float) width;
                        float scaleHeight = (float) idstHeight / (float) height;
                        if (scaleWidth > scaleHeight)
                        {
                            scale = scaleHeight;
                        }
                        else
                        {
                            scale = scaleWidth;
                        }
                        int tmpWidth = (int) ((float) width * scale);
                        int tmpHigth = (int) ((float) height * scale);
                        bitmap = Bitmap.createScaledBitmap(srcBitmap, tmpWidth,
                                tmpHigth, true);
                    }
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
        catch (OutOfMemoryError e)
        {
            LogUtil.w(LOG_TAG, e.getMessage());
        }

        return bitmap;
    }

    public static byte[] readStream(InputStream inStream) throws Exception
    {
        if (null == inStream)
        {
            LogUtil.w(LOG_TAG, "inStream is null!");
            return null;
        }

        int iTotalDataCount = inStream.available();
        if (0 == iTotalDataCount)
        {
            LogUtil.w(LOG_TAG, "iTotalDataCount is zero!");
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[iTotalDataCount];
        int iLen = 0;
        while ((iLen = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer, 0, iLen);

            if (outStream.size() > m_iMaxBitmapSize)
            {
                LogUtil.w(LOG_TAG, "Exceed max bitmap size:" + outStream.size()
                        + ",max:" + m_iMaxBitmapSize + ",iTotalDataCount="
                        + iTotalDataCount);
                return null;
            }
        }

        return outStream.toByteArray();
    }

    public static Bitmap readBitMap(Context ctx, int iResId)
    {
        if (null == ctx || iResId <= 0)
        {
            return null;
        }

        Bitmap bitmap = null;

        try
        {
            bitmap = BitmapFactory.decodeResource(ctx.getResources(), iResId,
                    initOptions());
        }
        catch (OutOfMemoryError e)
        {
            LogUtil.w(LOG_TAG, e.getMessage());
        }

        return bitmap;
    }

    public static Bitmap readAdaptiveBitMap(Context ctx, int resId,
            int toWidth, int toHeight)
    {
        if (null == ctx || resId <= 0)
        {
            return null;
        }

        Bitmap bitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try
        {
            BitmapFactory.decodeResource(ctx.getResources(), resId, options);
            int iSampleSize = calculateInSampleSize(options, toWidth, toHeight);

            options = initOptions();
            options.inSampleSize = iSampleSize;
            options.inJustDecodeBounds = false;

            InputStream is = ctx.getResources().openRawResource(resId);
            bitmap = readBitmap(is, options);
            is.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtil.logException(e);
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
            LogUtil.e(LOG_TAG, e.getMessage());
        }

        return bitmap;
    }

    public static Bitmap readBitmap(String strPathName)
    {
        return readBitmap(strPathName, null);
    }

    public static Bitmap readBitmap(String strPathName, Options option)
    {
        if (TextUtils.isEmpty(strPathName))
        {
            return null;
        }

        byte[] data = null;
        try
        {
            data = readStream(new FileInputStream(new File(strPathName)));

            if (null == data || data.length < 0)
            {
                return null;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        Bitmap bitmap = null;

        try
        {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                    null != option ? option : initOptions());
        }
        catch (OutOfMemoryError e)
        {
            LogUtil.w(LOG_TAG, e.getMessage());
        }

        return bitmap;
    }

    public static Bitmap readBitmap(InputStream is)
    {
        return readBitmap(is, null);
    }

    public static Bitmap readBitmap(InputStream is, Options option)
    {
        if (null == is)
        {
            return null;
        }

        Bitmap bitmap = null;

        try
        {
            bitmap = BitmapFactory.decodeStream(is, null,
                    null != option ? option : initOptions());
        }
        catch (OutOfMemoryError e)
        {
            LogUtil.w(LOG_TAG, e.getMessage());
        }

        return bitmap;
    }

    public static Bitmap readBitmapForFixMaxSize(String strPathName)
    {
        return readBitmapForFixMaxSize(strPathName, m_iDefaultMaxWidth,
                m_iDefaultMaxHeight);
    }

    public static Bitmap readBitmapForFixMaxSize(String strPathName,
            int iMaxWidth, int iMaxHeight)
    {
        if (TextUtils.isEmpty(strPathName))
        {
            LogUtil.w(LOG_TAG, "strPathName is empty!");
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try
        {
            BitmapFactory.decodeFile(strPathName, options);
            int iSampleSize = calculateInSampleSize(options, iMaxWidth,
                    iMaxHeight);

            options = initOptions();
            options.inSampleSize = iSampleSize;
            options.inJustDecodeBounds = false;

            LogUtil.d(LOG_TAG, "inSampleSize=" + options.inSampleSize
                    + ",strPathName=" + strPathName);

            Bitmap bmpReturn = BitmapFactory.decodeFile(strPathName, options);
            if (null != bmpReturn)
            {
                return bmpReturn;
            }
            else
            {
                LogUtil.e(LOG_TAG, "Read bitmap file error! File:"
                        + strPathName);
                try
                {
                    bmpReturn = BitmapFactory.decodeStream(new FileInputStream(
                            strPathName), null, options);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (null != bmpReturn)
                {
                    return bmpReturn;
                }
                else
                {
                    bmpReturn = BitmapFactory
                            .decodeStream(new FlushedInputStream(
                                    new FileInputStream(strPathName)));
                    if (null != bmpReturn)
                    {
                        return bmpReturn;
                    }
                    else
                    {
                        LogUtil.e(LOG_TAG, "Read bitmap file error! File:"
                                + strPathName);
                        return null;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtil.e(LOG_TAG, "Read bitmap file error! File:" + strPathName
                    + " " + e.getMessage());
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
            LogUtil.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    static class FlushedInputStream extends FilterInputStream
    {
        public FlushedInputStream(InputStream inputStream)
        {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException
        {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n)
            {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L)
                {
                    int b = read();
                    if (b < 0)
                    {
                        break; // we reached EOF
                    }
                    else
                    {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight)
    {
        // Raw height and width of image (ͼ��ԭʼ�Ŀ�͸�)
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            // Calculate ratios of height and width to requested height and
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        if (inSampleSize < 1)
        {
            inSampleSize = 1;
        }

        return inSampleSize;
    }

    public static BitmapFactory.Options initOptions()
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;

        opts.inTempStorage = new byte[12 * 1024];

        return opts;
    }
    
	public static Bitmap getStorePic(String path, int width, int heigth) {
		Options options = new Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		int scalex = options.outWidth / width;
		int scaley = options.outHeight / width;
		if (scalex > scaley) {
			options.inSampleSize = scalex;
		} else {
			options.inSampleSize = scaley;
		}
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap createReflectImages(Bitmap bImap)
    {
        if (null == bImap)
        {
            return null;
        }

        final int reflectionGap = 4;
        int width = bImap.getWidth();
        int height = bImap.getHeight();

        Bitmap bitmapWithReflection = null;
        try
        {
            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);
            Bitmap reflectionImage = Bitmap.createBitmap(bImap, 0, height / 2,
                    width, height / 2, matrix, false);
            bitmapWithReflection = Bitmap.createBitmap(width,
                    (height + height / 2), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapWithReflection);
            canvas.drawBitmap(bImap, 0, 0, null);
            Paint deafaultPaint = new Paint();
            canvas.drawRect(0, height, width, height + reflectionGap,
                    deafaultPaint);
            canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
            Paint paint = new Paint();
            LinearGradient shader = new LinearGradient(0, bImap.getHeight(), 0,
                    bitmapWithReflection.getHeight() + reflectionGap,
                    0x80ffffff, 0x00ffffff, TileMode.CLAMP);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.DST_IN));
            canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                    + reflectionGap, paint);
        }
        catch (OutOfMemoryError e)
        {
            LogUtil.w(LOG_TAG, e.getMessage());
        }

        return bitmapWithReflection;
    }

    /**
     * Bitmapתbyte[]
     * 
     * @param bm
     *            ͼƬ
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm)
    {
        if (null == bm)
        {
            return null;
        }

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        catch (OutOfMemoryError e)
        {
            LogUtil.w(LOG_TAG, e.getMessage());
        }

        return null;
    }

    public static Bitmap joinBitmap(Bitmap[] bitmaps, int orientation)
    {
        if (null == bitmaps)
        {
            return null;
        }

        int width = 0;
        int height = 0;

        try
        {
            if (INT_ORIENTATION_VERTICAL == orientation)
            {
                for (Bitmap bitmap : bitmaps)
                {
                    if (null == bitmap)
                    {
                        continue;
                    }
                    height += bitmap.getHeight();
                    width = Math.max(width, bitmap.getWidth());
                }
            }
            else
            {
                for (Bitmap bitmap : bitmaps)
                {
                    if (null == bitmap)
                    {
                        continue;
                    }
                    width += bitmap.getWidth();
                    height = Math.max(height, bitmap.getHeight());
                }
            }

            Bitmap newbmp = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newbmp);
            int offset = 0;

            if (INT_ORIENTATION_VERTICAL == orientation)
            {
                for (Bitmap bitmap : bitmaps)
                {
                    if (null == bitmap)
                    {
                        continue;
                    }
                    canvas.drawBitmap(bitmap, 0, offset, null);
                    offset += bitmap.getHeight();
                }
            }
            else
            {
                for (Bitmap bitmap : bitmaps)
                {
                    if (null == bitmap)
                    {
                        continue;
                    }
                    canvas.drawBitmap(bitmap, offset, 0, null);
                    offset += bitmap.getWidth();
                }
            }

            return newbmp;
        }
        catch (Exception e)
        {
            LogUtil.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    public static Drawable joinDrawable(Drawable[] drawables, int orientation)
    {
        if (null == drawables)
        {
            return null;
        }

        int width = 0;
        int height = 0;

        try
        {
            if (INT_ORIENTATION_VERTICAL == orientation)
            {
                for (Drawable drawable : drawables)
                {
                    if (null == drawable)
                    {
                        continue;
                    }
                    height += drawable.getIntrinsicHeight();
                    width = Math.max(width, drawable.getIntrinsicWidth());
                }
            }
            else
            {
                for (Drawable drawable : drawables)
                {
                    if (null == drawable)
                    {
                        continue;
                    }
                    width += drawable.getIntrinsicWidth();
                    height = Math.max(height, drawable.getIntrinsicHeight());
                }
            }

            Bitmap.Config config = drawables[0].getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            Bitmap newbmp = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(newbmp);
            int offset = 0;

            if (INT_ORIENTATION_VERTICAL == orientation)
            {
                for (Drawable drawable : drawables)
                {
                    if (null == drawable)
                    {
                        continue;
                    }
                    canvas.drawBitmap(drawableToBitmap(drawable), 0, offset,
                            null);
                    offset += drawable.getIntrinsicHeight();
                }
            }
            else
            {
                for (Drawable drawable : drawables)
                {
                    if (null == drawable)
                    {
                        continue;
                    }
                    canvas.drawBitmap(drawableToBitmap(drawable), offset, 0,
                            null);
                    offset += drawable.getIntrinsicWidth();
                }
            }

            return new BitmapDrawable(newbmp);
        }
        catch (Exception e)
        {
            LogUtil.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h)
    {
        if (null == drawable || w < 0 || h < 0)
        {
            return null;
        }

        try
        {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap oldbmp = drawableToBitmap(drawable);
            Matrix matrix = new Matrix();
            float scaleWidth = 1;
            float scaleHeight = 1;
            if (w > 0)
            {
                scaleWidth = ((float) w / width);
            }
            if (h > 0)
            {
                scaleHeight = ((float) h / height);
            }

            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                    matrix, true);
            return new BitmapDrawable(newbmp);
        }
        catch (Exception e)
        {
            LogUtil.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    static Bitmap drawableToBitmap(Drawable drawable)
    {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
    
    /**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	public static Bitmap createCircleImage(Bitmap source, int min)
	{
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}
    
}
