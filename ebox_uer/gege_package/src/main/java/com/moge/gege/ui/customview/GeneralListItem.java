package com.moge.gege.ui.customview;

import android.content.Context;
import android.util.AttributeSet;

import com.moge.gege.model.TopicModel;

public class GeneralListItem extends TopicListItem
{
    private void initMyViews(Context context)
    {
        // LayoutInflater inflater = (LayoutInflater) context
        // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflater.inflate(R.layout.item_secondhand, serviceLayout, true);
    }

    public GeneralListItem(Context context, AttributeSet attribute)
    {
        super(context, attribute);
        initMyViews(context);
    }

    public GeneralListItem(Context context)
    {
        super(context, ItemType.TOPIC);
        initMyViews(context);
    }

    public void setData(final TopicModel model)
    {
        super.setData(model);
    }

}
