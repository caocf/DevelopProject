package com.moge.gege.ui.customview;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.ServiceInfoModel;
import com.moge.gege.model.ServiceModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.util.FunctionUtils;

public class RentHouseListItem extends TopicListItem
{
    private TextView communityText;
    private TextView houseTypeText;
    private TextView rentMoneyText;

    private void initMyViews(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_renthouse, serviceLayout, true);
        communityText = (TextView) findViewById(R.id.communityText);
        houseTypeText = (TextView) findViewById(R.id.houseTypeText);
        rentMoneyText = (TextView) findViewById(R.id.rentMoneyText);
    }

    public RentHouseListItem(Context context, AttributeSet attribute)
    {
        super(context, attribute);
        initMyViews(context);
    }

    public RentHouseListItem(Context context)
    {
        super(context, ItemType.TOPIC);
        initMyViews(context);
    }

    public RentHouseListItem(Context context, ItemType type)
    {
        super(context, type);
        initMyViews(context);
    }

    public void setData(final TopicModel model)
    {
        super.setData(model);

        setItemValue(model, getContext(), communityText, houseTypeText,
                rentMoneyText);
    }

    public void setData(final ServiceModel model)
    {
        super.setData(model);

        ServiceInfoModel infoModel = model.getInfo();
        if (infoModel == null)
        {
            return;
        }

        communityText.setText(Html.fromHtml(getContext().getResources()
                .getString(R.string.service_content,
                        FunctionUtils.getString(R.string.community),
                        infoModel.getCommunity_name())));

        houseTypeText.setText(Html.fromHtml(getContext().getResources()
                .getString(
                        R.string.service_content,
                        FunctionUtils.getString(R.string.house_type),
                        infoModel.getApartment_room() + "室"
                                + infoModel.getApartment_hall() + "厅"
                                + infoModel.getApartment_washroom() + "卫")));

        rentMoneyText.setText(Html.fromHtml(getContext().getResources()
                .getString(R.string.service_content,
                        FunctionUtils.getString(R.string.rent_money),
                        model.getPrice() + "")));
    }

    public static void setItemValue(TopicModel model, Context context,
            TextView communityText, TextView houseTypeText,
            TextView rentMoneyText)
    {
        ServiceInfoModel infoModel = model.getInfo();

        communityText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.community),
                infoModel.getCommunity_name())));

        houseTypeText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.house_type),
                infoModel.getApartment_room() + "室"
                        + infoModel.getApartment_hall() + "厅"
                        + infoModel.getApartment_washroom() + "卫")));

        rentMoneyText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.rent_money),
                model.getPrice() + "")));
    }

}
