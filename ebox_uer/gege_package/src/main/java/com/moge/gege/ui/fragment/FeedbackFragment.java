package com.moge.gege.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.FeedbackRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.ToastUtil;

public class FeedbackFragment extends BaseFragment
{
    public Context mContext;
    public EditText mFeedbackEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_feedback,
                container, false);
        initView(layout);
        return layout;
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        mContext = getActivity();
        this.setHeaderLeft(R.string.feedback, R.drawable.icon_back);
        this.setHeaderRightTitle(R.string.submit);

        mFeedbackEdit = (EditText) v.findViewById(R.id.feedbackEdit);
    }

    @Override
    protected void onHeaderLeftClick()
    {
        getActivity().finish();
    }

    @Override
    protected void onHeaderRightClick(View v)
    {
        String feedbackContent = mFeedbackEdit.getText().toString().trim();
        if (TextUtils.isEmpty(feedbackContent))
        {
            ToastUtil.showToastShort(R.string.submit_empty);
            return;
        }

        doFeedbackRequest(feedbackContent);
    }

    private void doFeedbackRequest(String content)
    {
        FeedbackRequest request = new FeedbackRequest(content,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result != null
                                && result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil.showToastShort(R.string.submit_success);
                            getActivity().finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        switch (requestCode)
        {
            case GlobalConfig.INTENT_TOPIC_DETAIL:
                break;
            default:
                break;
        }
    }

}
