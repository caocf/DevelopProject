package com.moge.gege.ui.fragment;

import java.util.List;

import android.text.TextUtils;
import android.widget.BaseAdapter;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.NoticeModel;
import com.moge.gege.model.RespNoticeListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.NoticeListRequest;
import com.moge.gege.network.request.ReadNoticeRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.NoticeAdapter;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class NoticeFragment extends MyBaseFragment
{
    private NoticeAdapter mAdapter;
    private String mMsgId = "";
    private String mNoticeType;

    public NoticeFragment setFragmentType(String type)
    {
        mNoticeType = type;
        return this;
    }

    protected BaseAdapter getAdatapter()
    {
        if (mAdapter == null)
        {
            mAdapter = new NoticeAdapter(mContext);
        }
        return mAdapter;
    }

    protected void initData()
    {
        showLoadingView();
        doNoticeListRequest(mNoticeType, mMsgId = "");
    }

    protected void loadMoreData()
    {
        doNoticeListRequest(mNoticeType, mMsgId);
    }

    protected int getDividerHeight()
    {
        return FunctionUtil.dip2px(getActivity(), 0.5);
    }

    protected void onListItemClick(int position)
    {
        NoticeModel model = mAdapter.getItem(position - 1);
        if (model != null)
        {
            UIHelper.showUrlRedirectBySpannable(mContext, model.getTitle());
        }
    }

    private void doNoticeListRequest(String noticeType, String msgId)
    {
        NoticeListRequest request = new NoticeListRequest(true, noticeType,
                msgId, new ResponseEventHandler<RespNoticeListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespNoticeListModel> request,
                            RespNoticeListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            List<NoticeModel> listModel = result.getData()
                                    .getMsgs();
                            if (listModel != null && listModel.size() > 0)
                            {
                                if (TextUtils.isEmpty(mMsgId))
                                {
                                    mAdapter.clear();
                                    doReadNoticeRequest(mNoticeType, listModel
                                            .get(0).get_id());
                                }

                                mMsgId = listModel.get(listModel.size() - 1)
                                        .get_id();
                                mAdapter.addAll(result.getData().getMsgs());
                                mAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                showLoadEmptyView();
                            }
                        }
                        else
                        {
                            showLoadFailView(result.getMsg());
                        }

                        stopRefreshUI();
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        stopRefreshUI();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private void doReadNoticeRequest(String noticeType, String msgId)
    {
        ReadNoticeRequest request = new ReadNoticeRequest(noticeType, msgId,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {

                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        executeRequest(request);
    }
}
