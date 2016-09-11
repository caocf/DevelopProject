package com.moge.gege.ui.customview;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.ServiceModel;
import com.moge.gege.model.SimpleTopicModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.TimeUtil;

public class TopicListItem extends MyLinearLayout
{
    private RelativeLayout topLayout;
    public ImageView avatarImage;
    private TextView nicknameText;
    private TextView fromCommunityText;
    private TextView timeText;
    public TextView topicTypeInfoText;
    private ImageView markImage;
    private TextView titleText;
    private TextView descText;
    public TextView likeCountText;
    private TextView postCountText;

    private LinearLayout attachmentLayout;
    private ImageView attachment1Image;
    private ImageView attachment2Image;
    private ImageView attachment3Image;

    public TextView privateMsgText;
    public TextView favoriteText;
    public LinearLayout serviceLayout;
    public LinearLayout footLayout;
    private View footLine;

    public enum ItemType
    {
        TOPIC, SERVICE;
    }

    private void initViews(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic, this, true);

        topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        nicknameText = (TextView) findViewById(R.id.nicknameText);
        fromCommunityText = (TextView) findViewById(R.id.fromCommunityText);
        topicTypeInfoText = (TextView) findViewById(R.id.topicTypeInfoText);
        markImage = (ImageView) findViewById(R.id.markImage);
        titleText = (TextView) findViewById(R.id.titleText);
        descText = (TextView) findViewById(R.id.descText);

        attachmentLayout = (LinearLayout) findViewById(R.id.attachmentLayout);
        attachment1Image = (ImageView) findViewById(R.id.attachment1Image);
        attachment2Image = (ImageView) findViewById(R.id.attachment2Image);
        attachment3Image = (ImageView) findViewById(R.id.attachment3Image);

        serviceLayout = (LinearLayout) findViewById(R.id.serviceLayout);
        footLayout = (LinearLayout) findViewById(R.id.footLayout);
        footLine = (View) findViewById(R.id.footLine);
    }

    public TopicListItem(Context context, AttributeSet attribute)
    {
        super(context, attribute);
        initViews(context);
    }

    public TopicListItem(Context context)
    {
        super(context);
        initViews(context);
    }

    public TopicListItem(Context context, ItemType type)
    {
        super(context);
        initViews(context);

        if (type == ItemType.SERVICE)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.foot_service, footLayout, true);

            privateMsgText = (TextView) findViewById(R.id.privateMsgText);
            favoriteText = (TextView) findViewById(R.id.favoriteText);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.foot_topic, footLayout, true);

            likeCountText = (TextView) findViewById(R.id.likeCountText);
            postCountText = (TextView) findViewById(R.id.postCountText);
        }

        timeText = (TextView) findViewById(R.id.timeText);
    }

    public void setData(final TopicModel model)
    {
        UserModel userModel = model.getAuthor();

        if (userModel != null)
        {
            setImage(avatarImage,
                    RequestManager.getImageUrl(userModel.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_avatar);

            nicknameText.setText(userModel.getNickname());
            timeText.setText(TimeUtil.timeToNow(model.getCrts()));

            // use for feeds list
            if (model.getBoard() != null)
            {
                fromCommunityText.setVisibility(View.VISIBLE);
                fromCommunityText.setText(getContext().getResources()
                        .getString(R.string.from) + model.getBoard().getName());
            }
            else
            {
                // use for service list
                if (model.getCommunity() != null)
                {
                    fromCommunityText.setVisibility(View.VISIBLE);
                    fromCommunityText.setText(getContext().getResources()
                            .getString(R.string.from)
                            + model.getCommunity().getName());
                }
                else
                {
                    fromCommunityText.setVisibility(View.GONE);
                }
            }

            if (likeCountText != null)
            {
                likeCountText.setText("" + model.getLike_count());
                if (model.isLiked())
                {
                    likeCountText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icon_liked, 0, 0, 0);
                }
                else
                {
                    likeCountText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.icon_like, 0, 0, 0);
                }
            }
            if (postCountText != null)
            {
                postCountText.setText("" + model.getPost_count());
            }

            this.topLayout.setVisibility(View.VISIBLE);
            this.footLayout.setVisibility(View.VISIBLE);
            footLine.setVisibility(View.VISIBLE);
        }
        else
        {
            this.topLayout.setVisibility(View.GONE);
            this.footLayout.setVisibility(View.GONE);
            footLine.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(model.getTitle()))
        {
            titleText.setVisibility(View.GONE);
        }
        else
        {
            titleText.setVisibility(View.VISIBLE);
            titleText.setText(model.getTitle());
        }

        if (TextUtils.isEmpty(model.getDescript()))
        {
            descText.setVisibility(View.GONE);
        }
        else
        {
            descText.setVisibility(View.VISIBLE);
            descText.setText(model.getDescript());
        }

        if (model.getMarked() == 0)
        {
            markImage.setVisibility(View.GONE);
        }
        else
        {
            markImage.setVisibility(View.VISIBLE);
        }

        AttachmentModel attachmentModel = model.getAttachments();
        if (!isAttachmentValid(attachmentModel))
        {
            attachmentLayout.setVisibility(View.GONE);
        }
        else
        {
            attachmentLayout.setVisibility(View.VISIBLE);
            showAttachment(attachmentModel.getImages());
        }

        setTopicTypeInfoValue(topicTypeInfoText, model.getTopic_type(),
                model.getNeed_type());
    }

    public void setData(final ServiceModel model)
    {
        UserModel userModel = model.getAuthor();

        setImage(avatarImage,
                RequestManager.getImageUrl(userModel.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default_avatar);

        nicknameText.setText(userModel.getNickname());
        timeText.setText(TimeUtil.timeToNow(model.getCrts()));
        if (TextUtils.isEmpty(model.getTitle()))
        {
            titleText.setVisibility(View.GONE);
        }
        else
        {
            titleText.setVisibility(View.VISIBLE);
            titleText.setText(model.getTitle());
        }

        if (TextUtils.isEmpty(model.getDescript()))
        {
            descText.setVisibility(View.GONE);
        }
        else
        {
            descText.setVisibility(View.VISIBLE);
            descText.setText(model.getDescript());
        }

        SimpleTopicModel topicModel = model.getTopic();
        if (topicModel != null)
        {
            if (topicModel.getMarked() == 0)
            {
                markImage.setVisibility(View.GONE);
            }
            else
            {
                markImage.setVisibility(View.VISIBLE);
            }
        }

        AttachmentModel attachmentModel = model.getAttachments();
        if (!isAttachmentValid(attachmentModel))
        {
            attachmentLayout.setVisibility(View.GONE);
        }
        else
        {
            attachmentLayout.setVisibility(View.VISIBLE);
            showAttachment(attachmentModel.getImages());
        }
    }

    private void setTopicTypeInfoValue(TextView topicTypeInfoText,
            int topicType, int needType)
    {
        switch (topicType)
        {
            case TopicType.GENERAL_TOPIC:
                topicTypeInfoText.setText(getContext().getResources()
                        .getString(R.string.topic));
                break;
            case TopicType.ACTIVITY_TOPIC:
                topicTypeInfoText.setText(getContext().getResources()
                        .getString(R.string.activity));
                break;
            case TopicType.BUSINESS_TOPIC:
                topicTypeInfoText.setText(getContext().getResources()
                        .getString(R.string.business));
                break;
            case TopicType.CATEGORY_CARPOOL_TOPIC:
                if (needType == 1)
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.find_car));
                }
                else
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.find_passenger));
                }
                break;
            case TopicType.CATEGORY_MARRIAGE_TOPIC:
                if (needType == 1)
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.marriage));
                }
                else
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.dating));
                }
                break;
            case TopicType.CATEGORY_TOGETHER_TOPIC:
                if (needType == 1)
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.fitness));
                }
                else
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.play));
                }
                break;
            case TopicType.CATEGORY_PET_TOPIC:
                if (needType == 1)
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.adoption));
                }
                else
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.dating2));
                }
                break;
            case TopicType.CATEGORY_SECONDHAND_TOPIC:
                if (needType == 1)
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.buy));
                }
                else
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.sell));
                }
                break;
            case TopicType.CATEGORY_RENTHOUSE_TOPIC:
                if (needType == 1)
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.rent_house2));
                }
                else
                {
                    topicTypeInfoText.setText(getContext().getResources()
                            .getString(R.string.lease_house));
                }
                break;
            default:
                break;
        }
    }

    private boolean isAttachmentValid(AttachmentModel attachmentModel)
    {
        if (attachmentModel == null || attachmentModel.getImages().size() == 0)
        {
            return false;
        }

        for (String url : attachmentModel.getImages())
        {
            if (!TextUtils.isEmpty(url))
            {
                return true;
            }
        }

        return false;
    }

    private void showAttachment(List<String> images)
    {
        ImageView imageViewArray[] = { attachment1Image, attachment2Image,
                attachment3Image };

        for (int i = 0; i < 3; i++)
        {
            if (i < images.size())
            {
                imageViewArray[i].setVisibility(View.VISIBLE);
                this.setImage(imageViewArray[i],
                        RequestManager.getImageUrl(images.get(i))
                                + GlobalConfig.IMAGE_STYLE150_150,
                        R.drawable.icon_default);
            }
            else
            {
                imageViewArray[i].setVisibility(View.GONE);
            }
        }
    }
}
