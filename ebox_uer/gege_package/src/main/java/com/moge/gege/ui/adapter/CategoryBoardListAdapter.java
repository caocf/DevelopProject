package com.moge.gege.ui.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.model.BoardModel;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.FunctionUtils;

public class CategoryBoardListAdapter extends BaseExpandableListAdapter
{
    private List<BaseOptionModel> mGroupDataList;
    private HashMap<Integer, List<BoardModel>> mChildDataList;
    private Context mContext;

    public CategoryBoardListAdapter(Context context,
            List<BaseOptionModel> groupDataList,
            HashMap<Integer, List<BoardModel>> childDataList)
    {
        mContext = context;
        mGroupDataList = groupDataList;
        mChildDataList = childDataList;
    }

    @Override
    public int getGroupCount()
    {
        return mGroupDataList == null ? 0 : mGroupDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        if (mChildDataList != null && mChildDataList.get(groupPosition) != null)
        {
            return mChildDataList.get(groupPosition).size();
        }

        return 0;
    }

    @Override
    public BaseOptionModel getGroup(int groupPosition)
    {
        return mGroupDataList.get(groupPosition);
    }

    @Override
    public BoardModel getChild(int groupPosition, int childPosition)
    {
        return mChildDataList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent)
    {
        GroupViewiewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_board_category, null);

            holder = new GroupViewiewHolder();
            holder.categoryImage = (ImageView) convertView
                    .findViewById(R.id.categoryImage);
            holder.categoryNameText = (TextView) convertView
                    .findViewById(R.id.categoryNameText);
            holder.headerView = convertView.findViewById(R.id.headerView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (GroupViewiewHolder) convertView.getTag();
        }

        BaseOptionModel model = getGroup(groupPosition);
        RequestManager.loadImage(holder.categoryImage,
                RequestManager.getImageUrl(model.getIcon()),
                R.drawable.icon_default);
        holder.categoryNameText.setText(model.getName());

        if (groupPosition == 0)
        {
            holder.headerView.setVisibility(View.GONE);
        }
        else
        {
            holder.headerView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent)
    {
        ChildViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_board, null);

            holder = new ChildViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.memberCountText = (TextView) convertView
                    .findViewById(R.id.memberCountText);
            holder.topicCountText = (TextView) convertView
                    .findViewById(R.id.topicCountText);
            holder.distanceText = (TextView) convertView
                    .findViewById(R.id.distanceText);
            holder.joinBtn = (Button) convertView.findViewById(R.id.joinBtn);
            holder.line = (View) convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ChildViewHolder) convertView.getTag();
        }

        BoardModel model = getChild(groupPosition, childPosition);

        RequestManager.loadImage(holder.avatarImage,
                RequestManager.getImageUrl(model.getLogo()) + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default);
        holder.nameText.setText(model.getName());
        if (TextUtils.isEmpty(model.getDescript()))
        {
            holder.descText.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.descText.setVisibility(View.VISIBLE);
            holder.descText.setText(model.getDescript());
        }

        holder.memberCountText.setText(String.valueOf(model.getMember_count()));
        holder.topicCountText.setText(String.valueOf(model.getTopic_count()));
        holder.distanceText.setText(mContext.getResources().getString(
                R.string.km, FunctionUtils.getDouble(model.getDistance())));

        if (childPosition == getChildrenCount(groupPosition) - 1)
        {
            holder.line.setVisibility(View.GONE);
        }
        else
        {
            holder.line.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    static class GroupViewiewHolder
    {
        private View headerView;
        private ImageView categoryImage;
        private TextView categoryNameText;
    }

    static class ChildViewHolder
    {
        ImageView avatarImage;
        TextView nameText;
        TextView descText;
        TextView memberCountText;
        TextView topicCountText;
        TextView distanceText;
        Button joinBtn;
        View line;
    }

}
