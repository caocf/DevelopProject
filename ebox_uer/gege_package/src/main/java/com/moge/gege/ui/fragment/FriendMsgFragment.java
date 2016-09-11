package com.moge.gege.ui.fragment;

import java.util.List;

import android.text.TextUtils;
import android.widget.BaseAdapter;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.FriendMsgModel;
import com.moge.gege.model.RespFriendMsgListModel;
import com.moge.gege.model.enums.FriendOperateResult;
import com.moge.gege.model.enums.FriendOperateType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.FriendMsgListRequest;
import com.moge.gege.network.request.FriendOperateRequest;
import com.moge.gege.network.request.ReadFriendMsgRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.adapter.FriendMsgAdapter;
import com.moge.gege.ui.adapter.FriendMsgAdapter.FriendMsgListListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class FriendMsgFragment extends MyBaseFragment implements
        FriendMsgListListener
{
    private FriendMsgAdapter mAdapter;
    private String mMsgId = "";
    private FriendMsgModel mCurMsgModel;

    protected BaseAdapter getAdatapter()
    {
        if (mAdapter == null)
        {
            mAdapter = new FriendMsgAdapter(mContext);
            mAdapter.setListener(this);
        }
        return mAdapter;
    }

    protected void initData()
    {
        showLoadingView();
        doFriendMsgListRequest(mMsgId = "");
    }

    protected void loadMoreData()
    {
        doFriendMsgListRequest(mMsgId);
    }

    protected int getDividerHeight()
    {
        return FunctionUtil.dip2px(getActivity(), 0.5);
    }

    protected void onListItemClick(int position)
    {

    }

    @Override
    public void onFriendMsgAvatarClick(FriendMsgModel model)
    {
        UIHelper.showUserCenterActivity(mContext, model.getFuid());
    }

    @Override
    public void onFriendMsgAgreeClick(FriendMsgModel model)
    {
        mCurMsgModel = model;
        doFriendOperateRequest(FriendOperateType.ACCEPT_APPLY, model.getFuid(),
                model.get_id());
    }

    @Override
    public void onFriendMsgDisagreeClick(FriendMsgModel model)
    {

    }

    private void doFriendMsgListRequest(String msgId)
    {
        FriendMsgListRequest request = new FriendMsgListRequest(true, msgId,
                new ResponseEventHandler<RespFriendMsgListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespFriendMsgListModel> request,
                            RespFriendMsgListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            List<FriendMsgModel> listModel = result.getData()
                                    .getMsgs();
                            if (listModel != null && listModel.size() > 0)
                            {
                                if (TextUtils.isEmpty(mMsgId))
                                {
                                    mAdapter.clear();
                                    doReadFriendMsgRequest(listModel.get(0)
                                            .get_id());
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
                        ToastUtil.showToastShort(error.getMessage());
                        stopRefreshUI();
                        showLoadFailView(null);
                    }

                });
        executeRequest(request);
    }

    private void doReadFriendMsgRequest(String msgId)
    {
        ReadFriendMsgRequest request = new ReadFriendMsgRequest(msgId,
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

    private void doFriendOperateRequest(int type, String uid, String msgId)
    {
        FriendOperateRequest request = new FriendOperateRequest(type, uid,
                msgId, new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        FriendOperateRequest operateRequest = (FriendOperateRequest) request;
                        if (result.getStatus() == ErrorCode.SUCCESS
                                || result.getStatus() == ErrorCode.FORBIDDEN)
                        {
                            if (operateRequest.getType() == FriendOperateType.ACCEPT_APPLY)
                            {
                                mCurMsgModel
                                        .setTreated(FriendOperateResult.AGREE);
                            }
                            else
                            {
                                mCurMsgModel
                                        .setTreated(FriendOperateResult.DISAGREE);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                        }

                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.e(error.getMessage());
                    }

                });
        executeRequest(request);
    }
}
