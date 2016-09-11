package com.moge.gege.ui.fragment;

import java.util.List;

import android.text.TextUtils;
import android.widget.BaseAdapter;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.NotifyModel;
import com.moge.gege.model.RespNotifyListModel;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.NotifyListRequest;
import com.moge.gege.network.request.ReadNotifyRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.NotifyAdapter;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class NotifyFragment extends MyBaseFragment
{
    private NotifyAdapter mAdapter;
    private String mMsgId = "";
    private String mNotifyType;

    public NotifyFragment setFragmentType(String type)
    {
        mNotifyType = type;
        return this;
    }

    protected BaseAdapter getAdatapter()
    {
        if (mAdapter == null)
        {
            mAdapter = new NotifyAdapter(mContext);
        }
        return mAdapter;
    }

    protected void initData()
    {
        showLoadingView();
        doNotifyListRequest(mNotifyType, mMsgId = "");
    }

    protected void loadMoreData()
    {
        doNotifyListRequest(mNotifyType, mMsgId);
    }

    protected int getDividerHeight()
    {
        return FunctionUtil.dip2px(getActivity(), 0.5);
    }

    protected void onListItemClick(int position)
    {
        NotifyModel model = mAdapter.getItem(position - 1);
        if (model != null)
        {
            UIHelper.showUrlRedirectBySpannable(mContext, model.getTitle());
        }
    }

    private void doNotifyListRequest(String notifyType, String msgId)
    {
        NotifyListRequest request = new NotifyListRequest(true, notifyType,
                msgId, new ResponseEventHandler<RespNotifyListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespNotifyListModel> request,
                            RespNotifyListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            List<NotifyModel> listModel = result.getData()
                                    .getMsgs();
                            if (listModel != null && listModel.size() > 0)
                            {
                                if (TextUtils.isEmpty(mMsgId))
                                {
                                    mAdapter.clear();
                                    doReadNotifyRequest(mNotifyType, listModel
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

    private void doReadNotifyRequest(String notifyType, String msgId)
    {
        ReadNotifyRequest request = new ReadNotifyRequest(notifyType, msgId,
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
