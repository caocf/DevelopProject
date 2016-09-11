package com.moge.gege.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.util.widget.CustomDialog;

public class DialogUtil
{

    public static Dialog createConfirmDialog(
            Context context,
            int iconId,
            String title,
            String message,
            String positiveBtnName,
            String negativeBtnName,
            android.content.DialogInterface.OnClickListener positiveBtnListener,
            android.content.DialogInterface.OnClickListener negativeBtnListener)
    {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        dialog = builder.create();
        return dialog;
    }

    public static CustomDialog createConfirmDialog(
            Context context,
            String title,
            String message,
            String positiveBtnName,
            String negativeBtnName,
            android.content.DialogInterface.OnClickListener positiveBtnListener,
            android.content.DialogInterface.OnClickListener negativeBtnListener)
    {
        return new CustomDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnName, positiveBtnListener)
                .setNegativeButton(negativeBtnName, negativeBtnListener)
                .create();
    }

    public static Dialog createSingleChoiceDialog(Context context,
            String title, CharSequence[] items,
            DialogInterface.OnClickListener clickListener)
    {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, 0, clickListener);
        builder.setNegativeButton("取消", null);
        dialog = builder.create();
        return dialog;
    }

    public static Dialog createMenuDialog(Context context, String title,
            int arrayResId, View.OnClickListener clickListener)
    {
        return createMenuDialog(context, title, context.getResources()
                .getStringArray(arrayResId), clickListener);
    }

    public static Dialog createMenuDialog(Context context, String title,
            CharSequence[] items, View.OnClickListener clickListener)
    {
        LinearLayout menuLayout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.custom_menu_dialog, null);
        Dialog menuDialog = new Dialog(context, R.style.CustomDialog_Menu);

        LinearLayout itemLayout;

        for (int i = 0; i < items.length; i++)
        {
            CharSequence item = items[i];

            if (item.equals("取消"))
            {
                itemLayout = (LinearLayout) LayoutInflater.from(context)
                        .inflate(R.layout.custom_menu_cancel, null);
            }
            else
            {
                itemLayout = (LinearLayout) LayoutInflater.from(context)
                        .inflate(R.layout.custom_menu_item, null);
            }

            TextView itemText = (TextView) itemLayout
                    .findViewById(R.id.itemText);
            itemText.setText(item);
            itemText.setTag(Integer.valueOf(i));
            itemText.setSingleLine();
            itemText.setEllipsize(TextUtils.TruncateAt.END);
            itemText.setPadding(10, 0, 10, 0);
            itemText.setOnClickListener(clickListener);
            menuLayout.addView(itemLayout);
        }

        WindowManager.LayoutParams localLayoutParams = menuDialog.getWindow()
                .getAttributes();
        localLayoutParams.x = 0;
        localLayoutParams.y = -1000;
        localLayoutParams.gravity = 80;
        menuLayout.setMinimumWidth(10000);
        menuDialog.onWindowAttributesChanged(localLayoutParams);
        menuDialog.setCanceledOnTouchOutside(true);
        menuDialog.setCancelable(true);
        menuDialog.setContentView(menuLayout);

        return menuDialog;
    }

    public static Dialog createProgressDialog(Context context, int resId)
    {
        return createProgressDialog(context,
                context.getResources().getString(resId));
    }

    public static Dialog createProgressDialog(Context context, String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_progress_dialog, null);
        TextView mTextView = (TextView) view.findViewById(R.id.msgText);
        mTextView.setText(msg);
        builder.setView(view);
        return builder.create();
    }
}
