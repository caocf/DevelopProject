package com.moge.gege.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.RespTopicListModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.model.enums.MyPublishType;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.MyPublishRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.TopicDetailActivity;
import com.moge.gege.ui.adapter.MyFavoriteAdapter;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class MyPublishFragment extends MyBaseFragment
{
    private MyFavoriteAdapter mPublishAdapter;
    private String mNextCursor = "";
    private int mPublishType;

    public MyPublishFragment setFragmentType(int type)
    {
        mPublishType = type;
        return this;
    }

    protected BaseAdapter getAdatapter()
    {
        if (mPublishAdapter == null)
        {
            mPublishAdapter = new MyFavoriteAdapter(mContext);
        }
        return mPublishAdapter;
    }

    protected void initData()
    {
        showLoadingView();
        doMyPublishList(mPublishType, mNextCursor = "");
    }

    protected void loadMoreData()
    {
        doMyPublishList(mPublishType, mNextCursor);
    }

    protected int getDividerHeight()
    {
        return FunctionUtil.dip2px(getActivity(), 0.5);
    }

    protected void onListItemClick(int position)
    {
        TopicModel model = mPublishAdapter.getItem(position - 1);
        if (model != null)
        {
            Intent intent = new Intent(mContext, TopicDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("board_id", "");
            bundle.putString("topic_id", model.get_id());

            int topicType = TopicType.GENERAL_TOPIC;
            switch (mPublishType)
            {
                case MyPublishType.TOPIC:
                    topicType = TopicType.GENERAL_TOPIC;
                    break;
                case MyPublishType.ACTIVITY:
                    topicType = TopicType.ACTIVITY_TOPIC;
                    break;
                case MyPublishType.SERVICE:
                    topicType = TopicType.CATEGORY_TOPIC
                            + model.getService_type();
                    break;
                default:
                    break;
            }
            bundle.putInt("topic_type", topicType);
            intent.putExtras(bundle);
            this.startActivityForResult(intent,
                    GlobalConfig.INTENT_TOPIC_DETAIL);
        }
    }

    private void doMyPublishList(int publishType, String cursor)
    {
        MyPublishRequest request = new MyPublishRequest(publishType, cursor,
                new ResponseEventHandler<RespTopicListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespTopicListModel> request,
                            RespTopicListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            List<TopicModel> listModel = null;

                            switch (mPublishType)
                            {
                                case MyPublishType.TOPIC:
                                    listModel = result.getData().getTopics();
                                    break;
                                case MyPublishType.ACTIVITY:
                                    listModel = result.getData().getActivitys();
                                    break;
                                case MyPublishType.SERVICE:
                                    listModel = result.getData()
                                            .getLiving_services();
                                    break;
                                default:
                                    break;
                            }

                            if (mNextCursor.equals(""))
                            {
                                mPublishAdapter.clear();
                            }

                            if (listModel != null && listModel.size() > 0)
                            {
                                mPublishAdapter.addAll(listModel);
                                mPublishAdapter.notifyDataSetChanged();

                                mNextCursor = result.getData().getNext_cursor();
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

}
