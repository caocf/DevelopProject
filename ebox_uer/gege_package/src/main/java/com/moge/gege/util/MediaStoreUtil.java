package com.moge.gege.util;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;

public class MediaStoreUtil
{
    private static HashMap<Integer, String> mThumbnailHash = new HashMap<Integer, String>();

    public static String getThumbPath(int key, String defalt)
    {
        if (mThumbnailHash == null || !mThumbnailHash.containsKey(key))
            return defalt;
        return mThumbnailHash.get(key);
    }

    public static void putThumbPath(Integer key, String value)
    {
        mThumbnailHash.put(key, value);
    }

    public static void clearThumb()
    {
        mThumbnailHash.clear();
    }

    public static void initThumbImage(Context context)
    {
        clearThumb();

        String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA };
        Cursor cursor = context.getContentResolver().query(
                Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

        if (cursor == null)
        {
            return;
        }

        if (cursor.moveToFirst())
        {
            int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
            do
            {
                putThumbPath(cursor.getInt(image_idColumn),
                        "file://" + cursor.getString(dataColumn));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    public static Cursor getMediaAlbum(Context context, String albumName)
    {
        String[] arrayOfString = { MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String str = "bucket_display_name = \"" + albumName + "\"";
        return context.getContentResolver().query(localUri, arrayOfString, str,
                null, "datetaken DESC");
    }

    public static Cursor getMediaAlbums(Context context)
    {
        String[] arrayOfString = { MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return context.getContentResolver().query(localUri, arrayOfString,
                null, null, null);
    }

    public static Cursor getMediaPhotos(Context context)
    {
        String[] arrayOfString = { MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA };
        Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return context.getContentResolver().query(localUri, arrayOfString,
                "_data LIKE \"%%\"", null, null);
    }

    public static String getThumbnailUri(Context context, long imageId)
    {
        Cursor localCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                context.getContentResolver(), imageId, Thumbnails.MINI_KIND,
                null);
        String str = null;
        if (localCursor != null)
        {
            int i = localCursor.getCount();
            str = null;
            if (i > 0)
            {
                localCursor.moveToFirst();
                str = localCursor
                        .getString(localCursor.getColumnIndex("_data"));
            }
        }
        return str;
    }
}
