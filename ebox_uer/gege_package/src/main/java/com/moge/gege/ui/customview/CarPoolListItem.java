package com.moge.gege.ui.customview;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.ServiceModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.TimeUtil;

public class CarPoolListItem extends TopicListItem
{
    private TextView startAddressText;
    private TextView endAddressText;
    private TextView startTimeText;

    private void initMyViews(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_carpool, serviceLayout, true);

        startAddressText = (TextView) findViewById(R.id.startAddressText);
        endAddressText = (TextView) findViewById(R.id.endAddressText);
        startTimeText = (TextView) findViewById(R.id.startTimeText);
    }

    public CarPoolListItem(Context context, AttributeSet attribute)
    {
        super(context, attribute);
        initMyViews(context);
    }

    public CarPoolListItem(Context context)
    {
        super(context, ItemType.TOPIC);
        initMyViews(context);
    }

    public CarPoolListItem(Context context, ItemType type)
    {
        super(context, type);
        initMyViews(context);
    }

    public void setData(final TopicModel model)
    {
        super.setData(model);

        setItemValue(model, getContext(), startAddressText, endAddressText,
                startTimeText);
    }

    public void setData(final ServiceModel model)
    {
        super.setData(model);

        startAddressText.setText(Html.fromHtml(getContext().getResources()
                .getString(R.string.service_content,
                        FunctionUtils.getString(R.string.start_address),
                        model.getStart_location())));
        endAddressText.setText(Html.fromHtml(getContext().getResources()
                .getString(R.string.service_content,
                        FunctionUtils.getString(R.string.end_address),
                        model.getEnd_location())));
        startTimeText
                .setText(Html.fromHtml(getContext().getResources().getString(
                        R.string.service_content,
                        FunctionUtils.getString(R.string.start_time),
                        TimeUtil.getDateTimeStr(model.getStart_time() * 1000))));

    }

    public static void setItemValue(TopicModel model, Context context,
            TextView startAddressText, TextView endAddressText,
            TextView startTimeText)
    {
        startAddressText.setText(Html.fromHtml(context.getResources()
                .getString(R.string.service_content,
                        FunctionUtils.getString(R.string.start_address),
                        model.getStart_location())));
        endAddressText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.end_address),
                model.getEnd_location())));
        startTimeText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.start_time),
                TimeUtil.getDateTimeStr(model.getStart_time() * 1000))));
    }

}
