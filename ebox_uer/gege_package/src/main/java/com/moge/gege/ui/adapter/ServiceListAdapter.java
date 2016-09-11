package com.moge.gege.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TopicFavoriteRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.ui.customview.CarPoolListItem;
import com.moge.gege.ui.customview.MarriageDatingListItem;
import com.moge.gege.ui.customview.PetListItem;
import com.moge.gege.ui.customview.RentHouseListItem;
import com.moge.gege.ui.customview.SecondHandListItem;
import com.moge.gege.ui.customview.TogetherListItem;
import com.moge.gege.ui.customview.TopicListItem.ItemType;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.ToastUtil;

public class ServiceListAdapter extends BaseCachedListAdapter<TopicModel>
        implements OnItemClickListener
{

    private ServiceListListener mListener;
    private boolean mIsShowTopicTypeInfo = false;
    private TopicModel mCurModel;

    public ServiceListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(ServiceListListener listener)
    {
        mListener = listener;
    }

    public void setShowTopicTypeInfo(boolean isShow)
    {
        mIsShowTopicTypeInfo = isShow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        TopicModel topicModel = list.get(position);

        if (topicModel.getTopic_type() == TopicType.CATEGORY_SECONDHAND_TOPIC)
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
            itemView.setData(topicModel);
            itemView.avatarImage.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.favoriteText.setOnClickListener(new MyClickListener(
                    topicModel));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (topicModel.getTopic_type() == TopicType.CATEGORY_RENTHOUSE_TOPIC)
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
            itemView.setData(topicModel);
            itemView.avatarImage.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.favoriteText.setOnClickListener(new MyClickListener(
                    topicModel));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (topicModel.getTopic_type() == TopicType.CATEGORY_MARRIAGE_TOPIC)
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
            itemView.setData(topicModel);
            itemView.avatarImage.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.favoriteText.setOnClickListener(new MyClickListener(
                    topicModel));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (topicModel.getTopic_type() == TopicType.CATEGORY_PET_TOPIC)
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
            itemView.setData(topicModel);
            itemView.avatarImage.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.favoriteText.setOnClickListener(new MyClickListener(
                    topicModel));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (topicModel.getTopic_type() == TopicType.CATEGORY_TOGETHER_TOPIC)
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
            itemView.setData(topicModel);
            itemView.avatarImage.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.favoriteText.setOnClickListener(new MyClickListener(
                    topicModel));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }
        else if (topicModel.getTopic_type() == TopicType.CATEGORY_CARPOOL_TOPIC)
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
            itemView.setData(topicModel);
            itemView.avatarImage.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.privateMsgText.setOnClickListener(new MyClickListener(
                    topicModel));
            itemView.favoriteText.setOnClickListener(new MyClickListener(
                    topicModel));
            showTopicTypeInfo(itemView.topicTypeInfoText);
            return itemView;
        }

        return convertView;
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

        MyClickListener(TopicModel model)
        {
            mModel = model;
        }

        @Override
        public void onClick(View v)
        {
            mCurModel = mModel;

            if(!AppApplication.checkLoginState((Activity) mContext))
            {
                return;
            }

            switch (v.getId())
            {
                case R.id.avatarImage:
                    UIHelper.showUserCenterActivity(mContext,
                            mModel.getAuthor_uid());
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

    public interface ServiceListListener
    {
        public void onServiceClick(TopicModel model);

        public void onFavoriteClick(TopicModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            // pushrefreshview have header view
            mListener.onServiceClick(list.get(position - 1));
        }
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
