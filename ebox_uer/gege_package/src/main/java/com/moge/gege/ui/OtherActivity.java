package com.moge.gege.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.enums.UpdateResult;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.LogoutRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.widget.update.VersionUpdate;
import com.moge.gege.ui.widget.update.VersionUpdate.UpdateResultListener;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OtherActivity extends BaseActivity
{
    private Context mContext;

    @InjectView(R.id.versionLayout) RelativeLayout mVersionLayout;
    @InjectView(R.id.praiseLayout) RelativeLayout mPraiseLayout;
    @InjectView(R.id.aboutUsLayout) RelativeLayout mAboutUsLayout;
    @InjectView(R.id.contactUsLayout) RelativeLayout mContactUsLayout;
    @InjectView(R.id.inviteFriendLayout) RelativeLayout mInviteFriendLayout;
    @InjectView(R.id.feedbackLayout) RelativeLayout mFeedbackLayout;
    @InjectView(R.id.quitBtn) Button mQuitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        ButterKnife.inject(this);

        mContext = OtherActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderLeftTitle(R.string.my_others);

        mVersionLayout.setOnClickListener(mClickListener);
        mPraiseLayout.setOnClickListener(mClickListener);
        mAboutUsLayout.setOnClickListener(mClickListener);
        mContactUsLayout.setOnClickListener(mClickListener);
        mInviteFriendLayout.setOnClickListener(mClickListener);
        mFeedbackLayout.setOnClickListener(mClickListener);
        mQuitBtn.setOnClickListener(mClickListener);
    }

    private void initData()
    {
    }

    private OnClickListener mClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.versionLayout:
                    new VersionUpdate(OtherActivity.this,
                            new UpdateResultListener()
                            {
                                @Override
                                public void onNotUpdate(int result)
                                {
                                    if (result == UpdateResult.NO_UPDATE_VERSION)
                                    {
                                        ToastUtil
                                                .showToastShort(R.string.not_need_update);
                                    }
                                }
                            });
                    break;
                case R.id.praiseLayout:
                    Uri uri = Uri.parse("market://details?id="
                            + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    break;
                case R.id.aboutUsLayout:
                    gotoOtherDetailActivity(1);
                    break;
                case R.id.contactUsLayout:
                    gotoOtherDetailActivity(2);
                    break;
                case R.id.inviteFriendLayout:
                    gotoOtherDetailActivity(3);
                    break;
                case R.id.feedbackLayout:
                    gotoOtherDetailActivity(4);
                    break;
                case R.id.quitBtn:
                    DialogUtil.createConfirmDialog(mContext,
                            getString(R.string.general_tips),
                            getString(R.string.quit_confirm),
                            getString(R.string.general_confirm),
                            getString(R.string.general_cancel),
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,
                                        int which)
                                {
                                    dialog.dismiss();
                                    gotoQuitLogin();
                                }
                            }, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,
                                        int which)
                                {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void gotoQuitLogin()
    {
        doLogoutRequest();
    }

    private void doLogoutRequest()
    {
        LogoutRequest request = new LogoutRequest(
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            AppApplication.logout(mContext);
                            finish();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void gotoOtherDetailActivity(int type)
    {
        Intent intent = new Intent(mContext, OtherDetailActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

}
