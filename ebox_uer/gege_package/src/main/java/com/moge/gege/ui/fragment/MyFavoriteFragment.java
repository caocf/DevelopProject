package com.moge.gege.ui.fragment;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespTopicListModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.MyFavoriteRequest;
import com.moge.gege.network.request.TopicFavoriteRequest;
import com.moge.gege.network.request.TopicUnFavoriteRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.TopicDetailActivity;
import com.moge.gege.ui.adapter.MyFavoriteAdapter;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.CustomDialog;

public class MyFavoriteFragment extends MyBaseFragment
{
    private MyFavoriteAdapter mFavoriteAdapter;
    private String mNextCursor = "";

    protected BaseAdapter getAdatapter()
    {
        if (mFavoriteAdapter == null)
        {
            mFavoriteAdapter = new MyFavoriteAdapter(mContext);
        }
        return mFavoriteAdapter;
    }

    protected void initData()
    {
        showLoadingView();
        doMyFavoriteList(mNextCursor = "");

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                new CustomDialog.Builder(mContext)
                        .setTitle(R.string.general_tips)
                        .setMessage(R.string.delete_favorite_confirm)
                        .setPositiveButton(R.string.general_confirm,
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        TopicModel model = mFavoriteAdapter.getItem(position - 1);
                                        if (model != null) {

                                            doDeleteFavorite(model.getTopic_type(), model.get_id());
                                        }

                                        dialog.dismiss();
                                    }

                                })
                        .setNegativeButton(R.string.general_cancel,
                                new DialogInterface.OnClickListener()
                                {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        dialog.dismiss();
                                    }

                                }).create().show();

                return false;
            }
        });
    }

    protected void loadMoreData()
    {
        doMyFavoriteList(mNextCursor);
    }

    protected int getDividerHeight()
    {
        return FunctionUtil.dip2px(getActivity(), 0.5);
    }

    protected void onListItemClick(int position)
    {
        TopicModel model = mFavoriteAdapter.getItem(position - 1);
        if (model != null)
        {
            if(model.getTopic_type() != TopicType.BUSINESS_TOPIC)
            {
                Intent intent = new Intent(mContext, TopicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("board_id", "");
                bundle.putString("topic_id", model.get_id());
                bundle.putInt("topic_type", model.getTopic_type());
                intent.putExtras(bundle);
                this.startActivityForResult(intent,
                        GlobalConfig.INTENT_TOPIC_DETAIL);
            }
            else
            {
                UIHelper.showTradingDetailActivity(mContext, model.get_id());
            }
        }
    }

    private void doMyFavoriteList(String cursor)
    {
        MyFavoriteRequest request = new MyFavoriteRequest(cursor,
                new ResponseEventHandler<RespTopicListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTopicListModel> request,
                            RespTopicListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            List<TopicModel> listModel = result.getData()
                                    .getFavorites();

                            if (mNextCursor.equals(""))
                            {
                                mFavoriteAdapter.clear();
                            }

                            if (listModel != null && listModel.size() > 0)
                            {
                                mFavoriteAdapter.addAll(listModel);
                                mNextCursor = result.getData().getNext_cursor();
                            }
                            else
                            {
                                ToastUtil.showToastShort(R.string.no_more_data);
                                showLoadEmptyView();
                            }

                            mFavoriteAdapter.notifyDataSetChanged();
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

    private void doDeleteFavorite(int topicType, String topicId)
    {
        TopicUnFavoriteRequest request = new TopicUnFavoriteRequest(topicType,
                topicId, null, new ResponseEventHandler<BaseModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<BaseModel> request, BaseModel result)
            {
                if (result.getStatus() == ErrorCode.SUCCESS)
                {
                    ToastUtil
                            .showToastShort(getString(R.string.delete_success));
                    initData();
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
}
