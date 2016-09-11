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
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.MyActivityRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.TopicDetailActivity;
import com.moge.gege.ui.adapter.MyFavoriteAdapter;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;

public class MyActivityFragment extends MyBaseFragment
{
    private MyFavoriteAdapter mActivityAdapter;
    private String mNextCursor = "";

    protected BaseAdapter getAdatapter()
    {
        if (mActivityAdapter == null)
        {
            mActivityAdapter = new MyFavoriteAdapter(mContext);
        }
        return mActivityAdapter;
    }

    protected void initData()
    {
        showLoadingView();
        doMyActivityList(mNextCursor = "");
    }

    protected void loadMoreData()
    {
        doMyActivityList(mNextCursor);
    }

    protected int getDividerHeight()
    {
        return FunctionUtil.dip2px(getActivity(), 0.5);
    }

    protected void onListItemClick(int position)
    {
        TopicModel model = mActivityAdapter.getItem(position - 1);
        if (model != null)
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
    }

    private void doMyActivityList(String cursor)
    {
        MyActivityRequest request = new MyActivityRequest(cursor,
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
                                    .getApplys();

                            if (mNextCursor.equals(""))
                            {
                                mActivityAdapter.clear();
                            }

                            if (listModel != null && listModel.size() > 0)
                            {
                                mActivityAdapter.addAll(listModel);
                                mActivityAdapter.notifyDataSetChanged();

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
