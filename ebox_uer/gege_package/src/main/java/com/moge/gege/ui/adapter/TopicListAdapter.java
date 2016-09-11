package com.moge.gege.ui.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespLikeResultModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TopicDislikeRequest;
import com.moge.gege.network.request.TopicFavoriteRequest;
import com.moge.gege.network.request.TopicLikeRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.customview.CarPoolListItem;
import com.moge.gege.ui.customview.GeneralListItem;
import com.moge.gege.ui.customview.MarriageDatingListItem;
import com.moge.gege.ui.customview.PetListItem;
import com.moge.gege.ui.customview.RentHouseListItem;
import com.moge.gege.ui.customview.SecondHandListItem;
import com.moge.gege.ui.customview.TogetherListItem;
import com.moge.gege.ui.customview.TopicListItem.ItemType;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.ScrollListView;
import com.moge.gege.util.widget.ScrollListView.OnItemClickListener;

public class TopicListAdapter extends BaseCachedListAdapter<TopicModel>
        implements OnItemClickListener
{
    private TopicListListener mListener;
    private boolean mIsShowTopicTypeInfo = false;
    private TopicModel mCurModel;
    private ScrollListView mListView;

    public TopicListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(TopicListListener listener)
    {
        mListener = listener;
    }

    public void setListView(ScrollListView listview)
    {
        mListView = listview;
    }

    public void setShowTopicTypeInfo(boolean isShow)
    {
        mIsShowTopicTypeInfo = isShow;
    }

    public int getItemViewType(int position)
    {
        TopicModel model = list.get(position);
        switch (model.getTopic_type())
        {
            case TopicType.CATEGORY_SECONDHAND_TOPIC:
                return 0;
            case TopicType.CATEGORY_RENTHOUSE_TOPIC:
                return 1;
            case TopicType.CATEGORY_MARRIAGE_TOPIC:
                return 2;
            case TopicType.CATEGORY_PET_TOPIC:
                return 3;
            case TopicType.CATEGORY_TOGETHER_TOPIC:
                return 4;
            case TopicType.CATEGORY_CARPOOL_TOPIC:
                return 5;
            default:
                return 6;
        }
    }

    public int getViewTypeCount()
    {
        return 7;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        TopicModel model = list.get(position);

        if (model.getTopic_type() == TopicType.CATEGORY_SECONDHAND_TOPIC)
        {
            SecondHandListItem itemView = null;

            if (convertView != null
                    && convertView instanceof SecondHandListItem)
            {
                itemView = (SecondHandListItem) convertView;
            }
            else
            {
                itemView = new SecondHandListItem(mContext, ItemType.SERVICE);
            }
            itemView.setData(model);
            itemView.avatarImage.setOnClickListener(new MyClickListener(model
                    .getAuthor_uid()));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    model));
            itemView.favoriteText
                    .setOnClickListener(new MyClickListener(model));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_RENTHOUSE_TOPIC)
        {
            RentHouseListItem itemView = null;

            if (convertView != null && convertView instanceof RentHouseListItem)
            {
                itemView = (RentHouseListItem) convertView;
            }
            else
            {
                itemView = new RentHouseListItem(mContext, ItemType.SERVICE);
            }
            itemView.setData(model);
            itemView.avatarImage.setOnClickListener(new MyClickListener(model
                    .getAuthor_uid()));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    model));
            itemView.favoriteText
                    .setOnClickListener(new MyClickListener(model));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_MARRIAGE_TOPIC)
        {
            MarriageDatingListItem itemView = null;

            if (convertView != null
                    && convertView instanceof MarriageDatingListItem)
            {
                itemView = (MarriageDatingListItem) convertView;
            }
            else
            {
                itemView = new MarriageDatingListItem(mContext,
                        ItemType.SERVICE);
            }
            itemView.setData(model);
            itemView.avatarImage.setOnClickListener(new MyClickListener(model
                    .getAuthor_uid()));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    model));
            itemView.favoriteText
                    .setOnClickListener(new MyClickListener(model));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_PET_TOPIC)
        {
            PetListItem itemView = null;

            if (convertView != null && convertView instanceof PetListItem)
            {
                itemView = (PetListItem) convertView;
            }
            else
            {
                itemView = new PetListItem(mContext, ItemType.SERVICE);
            }
            itemView.setData(model);
            itemView.avatarImage.setOnClickListener(new MyClickListener(model
                    .getAuthor_uid()));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    model));
            itemView.favoriteText
                    .setOnClickListener(new MyClickListener(model));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_TOGETHER_TOPIC)
        {
            TogetherListItem itemView = null;

            if (convertView != null && convertView instanceof TogetherListItem)
            {
                itemView = (TogetherListItem) convertView;
            }
            else
            {
                itemView = new TogetherListItem(mContext, ItemType.SERVICE);
            }
            itemView.setData(model);
            itemView.avatarImage.setOnClickListener(new MyClickListener(model
                    .getAuthor_uid()));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    model));
            itemView.favoriteText
                    .setOnClickListener(new MyClickListener(model));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (model.getTopic_type() == TopicType.CATEGORY_CARPOOL_TOPIC)
        {
            CarPoolListItem itemView = null;

            if (convertView != null && convertView instanceof CarPoolListItem)
            {
                itemView = (CarPoolListItem) convertView;
            }
            else
            {
                itemView = new CarPoolListItem(mContext, ItemType.SERVICE);
            }
            itemView.setData(model);
            itemView.avatarImage.setOnClickListener(new MyClickListener(model
                    .getAuthor_uid()));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    model));
            itemView.favoriteText
                    .setOnClickListener(new MyClickListener(model));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else
        {
            GeneralListItem itemView = null;

            if (convertView != null && convertView instanceof GeneralListItem)
            {
                itemView = (GeneralListItem) convertView;
            }
            else
            {
                itemView = new GeneralListItem(mContext);
            }
            itemView.setData(model);
            itemView.avatarImage.setOnClickListener(new MyClickListener(model
                    .getAuthor_uid()));
            itemView.likeCountText
                    .setOnClickListener(new MyClickListener(model));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
    }

    private void showTopicTypeInfo(TextView textView)
    {
        if (mIsShowTopicTypeInfo)
        {
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            textView.setVisibility(View.GONE);
        }
    }

    public class MyClickListener implements OnClickListener
    {
        TopicModel mModel;
        String mUid;

        MyClickListener(TopicModel model)
        {
            mModel = model;
        }

        MyClickListener(String uid)
        {
            mUid = uid;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener == null)
            {
                return;
            }

            if(!AppApplication.checkLoginState((Activity)mContext))
            {
                return;
            }

            mCurModel = mModel;

            switch (v.getId())
            {
                case R.id.avatarImage:
                    mListener.onAvatarClick(mUid);
                    break;
                case R.id.likeCountText:
                    if (mModel.isLiked())
                    {
                        doTopicDisLike(mModel.getTopic_type(), mModel.get_id(),
                                mModel.getBid());
                    }
                    else
                    {
                        doTopicLike(mModel.getTopic_type(), mModel.get_id(),
                                mModel.getBid());
                    }
                    break;
                case R.id.privateMsgText:
                    UIHelper.showChatActivity(mContext, mModel.getAuthor_uid());
                    break;
                case R.id.favoriteText:
                    doTopicFavorite(mModel.getTopic_type(), mModel.get_id(),
                            mModel.getBid());
                    break;
                default:
                    break;
            }

        }
    }

    public interface TopicListListener
    {
        public void onAvatarClick(String uid);

        public void onTopicClick(TopicModel model);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, int position, Object o)
    {
        if (mListener != null)
        {
            mListener.onTopicClick(list.get(position));
        }
    }

    private void doTopicLike(int topicType, String topicId, String boardId)
    {
        TopicLikeRequest request = new TopicLikeRequest(topicType, topicId,
                boardId, new ResponseEventHandler<RespLikeResultModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespLikeResultModel> request,
                            RespLikeResultModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mCurModel.setLiked(true);
                            mCurModel
                                    .setLike_count(result.getData().getCount());
                            notifyDataSetChanged();
                            mListView.notifyDataChange();
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

    private void doTopicDisLike(int topicType, String topicId, String boardId)
    {
        TopicDislikeRequest request = new TopicDislikeRequest(topicType,
                topicId, boardId,
                new ResponseEventHandler<RespLikeResultModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespLikeResultModel> request,
                            RespLikeResultModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mCurModel.setLiked(false);
                            mCurModel
                                    .setLike_count(result.getData().getCount());
                            notifyDataSetChanged();
                            mListView.notifyDataChange();
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

    private void doTopicFavorite(int topicType, String topicId, String boardId)
    {
        TopicFavoriteRequest request = new TopicFavoriteRequest(topicType,
                topicId, boardId, new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            ToastUtil
                                    .showToastShort(getString(R.string.favorite_success));
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
        RequestManager.addRequest(request, mContext);
    }

}
