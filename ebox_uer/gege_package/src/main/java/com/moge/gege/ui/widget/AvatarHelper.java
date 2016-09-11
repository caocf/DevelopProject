package com.moge.gege.ui.widget;

import android.content.Context;

public class AvatarHelper
{
    private static Context mContext = null;

    public static void showPicturePicker(Context context)
    {
        mContext = context;

        // AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        // dialog.setTitle("ͼƬ��Դ");
        // dialog.setNegativeButton("ȡ��", null);
        // dialog.setItems(new String[] { "����", "���" },
        // new DialogInterface.OnClickListener()
        // {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which)
        // {
        // int REQUEST_CODE;
        // switch (which)
        // {
        //
        // // ����
        // case 0:
        // REQUEST_CODE = 0;
        // // �������
        // Intent openCameraIntent = new Intent(
        // MediaStore.ACTION_IMAGE_CAPTURE);
        // SharedPreferences share = getSharedPreferences(
        // "temp", Context.MODE_WORLD_WRITEABLE);
        // ImageTools.deletePhotoAtPathAndName(Environment
        // .getExternalStorageDirectory()
        // .getAbsolutePath(), share.getString(
        // "tempName", ""));
        //
        // String filename = String.valueOf(System
        // .currentTimeMillis()) + ".jpg";
        // Editor editor = share.edit();
        // editor.putString("tempName", filename);
        // editor.commit();
        //
        // Uri imguri = Uri.fromFile(new File(Environment
        // .getExternalStorageDirectory(),
        // filename));
        // // ����
        // openCameraIntent.putExtra(
        // MediaStore.EXTRA_OUTPUT, imguri);
        // mContext.startActivityForResult(
        // openCameraIntent, REQUEST_CODE);
        // break;
        //
        // // ���
        // case 1:
        // Intent openAlbumIntent = new Intent(
        // Intent.ACTION_GET_CONTENT);
        // openAlbumIntent
        // .setDataAndType(
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        // "image/*");
        // mContext.startActivityForResult(
        // openAlbumIntent, 0);
        // break;
        // }
        //
        // }
        // });
        // dialog.create().show();
    }
}
