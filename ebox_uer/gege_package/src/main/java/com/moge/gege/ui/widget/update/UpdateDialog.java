package com.moge.gege.ui.widget.update;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class UpdateDialog extends Dialog
{
    public TextView posButton;

    private String updescText;

    private String versionName;

    public UpdateDialog(Context context, int theme)
    {
        super(context, theme);
    }

    // public void setUpdescText(String updescText)
    // {
    // this.updescText = updescText;
    // }
    //
    // public void setVersionName(String versionName)
    // {
    // this.versionName = versionName;
    // }
    //
    // @Override
    // protected void onCreate(Bundle savedInstanceState)
    // {
    // super.onCreate(savedInstanceState);
    // this.setContentView(R.layout.dialog_version_update);
    // initView();
    // }
    //
    // public void initView()
    // {
    // LinearLayout updateLayout = (LinearLayout) findViewById(R.id.rl_layout);
    // ViewUtil.setViewSize(updateLayout, 1000, 600);
    // ViewUtil.setViewPadding(updateLayout, 20, 80, 20, 20);
    //
    // TextView updateTitle = (TextView) findViewById(R.id.tv_title1);
    // updateTitle.setTextSize(ViewUtil.formateTextSize(54));
    // updateTitle.setText("��ƱTV" + versionName + "�滪������");
    // updateTitle.setTextColor(Color.BLACK);
    //
    // TextView updateContent = (TextView) findViewById(R.id.tv_title2);
    // updateContent.setTextSize(ViewUtil.formateTextSize(40));
    // ViewUtil.setViewSize(updateContent, 560, 220);
    // ViewUtil.setViewMargin(updateContent, 0, 0, 60, 35);
    // updateContent.setText(updescText);
    // updateContent.setTextColor(Color.BLACK);
    // updateContent.setFocusable(true);
    // updateContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    //
    // posButton = (TextView) findViewById(R.id.tv_update);
    // ViewUtil.setViewSize(posButton, 560, 110);
    // // posButton.setTextColor(Color.BLACK);
    // posButton.setTextSize(ViewUtil.formateTextSize(45));
    // posButton.setGravity(Gravity.CENTER);
    // posButton.setFocusable(true);
    // posButton.requestFocus();
    // }
    //
    // @Override
    // public void show()
    // {
    // super.show();
    // }
    //
    // @Override
    // public void dismiss()
    // {
    // super.dismiss();
    // }
}
