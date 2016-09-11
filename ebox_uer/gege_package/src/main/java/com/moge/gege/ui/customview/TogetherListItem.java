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

public class TogetherListItem extends TopicListItem {
    private TextView togetherTimeText;
    private TextView togetherAddressText;

    private void initMyViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_together, serviceLayout, true);

        togetherTimeText = (TextView) findViewById(R.id.togetherTimeText);
        togetherAddressText = (TextView) findViewById(R.id.togetherAddressText);
    }

    public TogetherListItem(Context context, AttributeSet attribute) {
        super(context, attribute);
        initMyViews(context);
    }

    public TogetherListItem(Context context) {
        super(context, ItemType.TOPIC);
        initMyViews(context);
    }

    public TogetherListItem(Context context, ItemType type) {
        super(context, type);
        initMyViews(context);
    }

    public void setData(final TopicModel model) {
        super.setData(model);

        setItemValue(model, getContext(), togetherTimeText, togetherAddressText);
    }

    public void setData(final ServiceModel model) {
        super.setData(model);

        String address = model.getInfo().getIndoor() == 1 ? FunctionUtils.getString(R.string.indoor) : FunctionUtils.getString(R.string.outdoor);

        togetherTimeText
                .setText(Html.fromHtml(getContext()
                        .getResources()
                        .getString(
                                R.string.service_content,
                                FunctionUtils.getString(R.string.time),
                                TimeUtil.getDateTimeStr(model.getStart_time() * 1000)
                                        + "~"
                                        + TimeUtil.getDateTimeStr(model
                                        .getEnd_time() * 1000))));

        togetherAddressText.setText(Html.fromHtml(getContext().getResources().getString(R.string.service_content, FunctionUtils.getString(R.string.address), address)));

    }

    public static void setItemValue(TopicModel model, Context context,
                                    TextView togetherTimeText, TextView togetherAddressText) {
        String address = model.getInfo().getIndoor() == 1 ? FunctionUtils.getString(R.string.indoor) : FunctionUtils.getString(R.string.outdoor);

        togetherTimeText
                .setText(Html.fromHtml(context
                        .getResources()
                        .getString(
                                R.string.service_content,
                                FunctionUtils.getString(R.string.time),
                                TimeUtil.getDateTimeStr(model.getStart_time() * 1000)
                                        + "~"
                                        + TimeUtil.getDateTimeStr(model
                                        .getEnd_time() * 1000))));

        togetherAddressText.setText(Html.fromHtml(context.getResources().getString(R.string.service_content, FunctionUtils.getString(R.string.address), address)));


    }

}
