package com.xhl.bqlh.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Sum on 16/3/8.
 */
public class PermissionCheck {

    //动态申请照相机权限
    public static void CameraReq(Activity context, int requestCode) {
        if (!CameraHasPermission(context)) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, requestCode);
        }
    }

    //检测照相机权限
    public static boolean CameraHasPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
}
