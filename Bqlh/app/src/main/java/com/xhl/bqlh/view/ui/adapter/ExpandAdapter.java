package com.xhl.bqlh.view.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.AreaModel;

import java.util.List;

/**
 * Created by Sum on 16/4/23.
 */
public class ExpandAdapter implements ExpandableListAdapter {

    private List<AreaModel> mTypes;
    private Context mContext;

    public ExpandAdapter(Context context, List<AreaModel> types) {
        this.mTypes = types;
        this.mContext = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mTypes == null ? 0 : mTypes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        AreaModel model = mTypes.get(groupPosition);
        List<AreaModel> children = model.getChildren();
        return children == null ? 0 : children.size();
    }

    @Override
    public AreaModel getGroup(int groupPosition) {
        return mTypes.get(groupPosition);
    }

    @Override
    public AreaModel getChild(int groupPosition, int childPosition) {
        return mTypes.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_expand_business_type, null);
        TextView name = (TextView) view.findViewById(R.id.tv_type);
        name.setText(getGroup(groupPosition).getAreaName());
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_expand_business_type, null);
        TextView name = (TextView) view.findViewById(R.id.tv_type);
        name.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        name.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        name.setText(getChild(groupPosition, childPosition).getAreaName());
        return name;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
