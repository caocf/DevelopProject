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

public class MarriageDatingListItem extends TopicListItem
{
    private TextView genderText;
    private TextView jobText;
    private TextView ageText;

    private void initMyViews(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_marriage_dating, serviceLayout, true);

        genderText = (TextView) findViewById(R.id.genderText);
        jobText = (TextView) findViewById(R.id.jobText);
        ageText = (TextView) findViewById(R.id.ageText);
    }

    public MarriageDatingListItem(Context context, AttributeSet attribute)
    {
        super(context, attribute);
        initMyViews(context);
    }

    public MarriageDatingListItem(Context context)
    {
        super(context, ItemType.TOPIC);
        initMyViews(context);
    }

    public MarriageDatingListItem(Context context, ItemType type)
    {
        super(context, type);
        initMyViews(context);
    }

    public void setData(final TopicModel model)
    {
        super.setData(model);

        setItemValue(model, getContext(), genderText, jobText, ageText);
    }

    public void setData(final ServiceModel model)
    {
        super.setData(model);

        ServiceInfoModel infoModel = model.getInfo();
        if (infoModel == null)
        {
            return;
        }

        String gender = infoModel.getGender() == 1 ? FunctionUtils
                .getString(R.string.man) : FunctionUtils
                .getString(R.string.woman);

        genderText.setText(Html.fromHtml(getContext().getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.gender), gender)));
        jobText.setText(Html.fromHtml(getContext().getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.job2),
                infoModel.getProfession())));
        ageText.setText(Html.fromHtml(getContext().getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.age), infoModel.getAge())));

    }

    public static void setItemValue(TopicModel model, Context context,
            TextView genderText, TextView jobText, TextView ageText)
    {
        ServiceInfoModel infoModel = model.getInfo();
        String gender = infoModel.getGender() == 1 ? FunctionUtils
                .getString(R.string.man) : FunctionUtils
                .getString(R.string.woman);

        genderText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.gender), gender)));
        jobText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.job2),
                infoModel.getProfession())));
        ageText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.age), infoModel.getAge())));
    }

}
