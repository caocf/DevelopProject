package com.moge.gege.ui.customview;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.OptionBuilder;
import com.moge.gege.model.ServiceInfoModel;
import com.moge.gege.model.ServiceModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.util.FunctionUtils;

public class SecondHandListItem extends TopicListItem
{
    private TextView conditionText;
    private TextView oldPriceText;
    private TextView newPriceText;

    private void initMyViews(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_secondhand, serviceLayout, true);

        conditionText = (TextView) findViewById(R.id.conditionText);
        oldPriceText = (TextView) findViewById(R.id.oldPriceText);
        newPriceText = (TextView) findViewById(R.id.newPriceText);
    }

    public SecondHandListItem(Context context, AttributeSet attribute)
    {
        super(context, attribute);
        initMyViews(context);
    }

    public SecondHandListItem(Context context)
    {
        super(context, ItemType.TOPIC);
        initMyViews(context);
    }

    public SecondHandListItem(Context context, ItemType type)
    {
        super(context, type);
        initMyViews(context);
    }

    public void setData(final TopicModel model)
    {
        super.setData(model);

        setItemValue(model, getContext(), conditionText, oldPriceText,
                newPriceText);
    }

    public void setData(final ServiceModel model)
    {
        super.setData(model);

        ServiceInfoModel infoModel = model.getInfo();
        if (infoModel == null)
        {
            return;
        }

        conditionText.setText(Html.fromHtml(getContext().getResources()
                .getString(
                        R.string.service_content,
                        FunctionUtils.getString(R.string.old_new),
                        OptionBuilder.instance().getGoodConditionByValue(
                                infoModel.getRecency()))));
        oldPriceText.setText(Html.fromHtml(getContext().getResources()
                .getString(R.string.service_content,
                        FunctionUtils.getString(R.string.old_price),
                        infoModel.getOriginal_price() + "")));
        newPriceText.setText(Html.fromHtml(getContext().getResources()
                .getString(R.string.service_content,
                        FunctionUtils.getString(R.string.now_price),
                        model.getPrice() + "")));
    }

    public static void setItemValue(TopicModel model, Context context,
            TextView conditionText, TextView oldPriceText, TextView newPriceText)
    {
        ServiceInfoModel infoModel = model.getInfo();

        conditionText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.old_new),
                OptionBuilder.instance().getGoodConditionByValue(
                        infoModel.getRecency()))));
        oldPriceText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.old_price),
                infoModel.getOriginal_price() + "")));
        newPriceText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.now_price),
                model.getPrice() + "")));
    }

}
