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

public class PetListItem extends TopicListItem
{
    private TextView varietyText;
    private TextView petAgeText;
    private TextView petGenderText;

    private void initMyViews(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_pet, serviceLayout, true);

        varietyText = (TextView) findViewById(R.id.varietyText);
        petAgeText = (TextView) findViewById(R.id.petAgeText);
        petGenderText = (TextView) findViewById(R.id.petGenderText);
    }

    public PetListItem(Context context, AttributeSet attribute)
    {
        super(context, attribute);
        initMyViews(context);
    }

    public PetListItem(Context context)
    {
        super(context, ItemType.TOPIC);
        initMyViews(context);
    }

    public PetListItem(Context context, ItemType type)
    {
        super(context, type);
        initMyViews(context);
    }

    public void setData(final TopicModel model)
    {
        super.setData(model);

        setItemValue(model, getContext(), varietyText, petAgeText,
                petGenderText);
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
                .getString(R.string.male) : FunctionUtils
                .getString(R.string.female);

        varietyText.setText(Html.fromHtml(getContext().getResources()
                .getString(
                        R.string.service_content,
                        FunctionUtils.getString(R.string.variety),
                        OptionBuilder.instance().getPetBreedNameById(
                                infoModel.getBreed()))));
        petAgeText.setText(Html.fromHtml(getContext().getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.age), infoModel.getAge())));
        petGenderText.setText(Html.fromHtml(getContext().getResources()
                .getString(R.string.service_content,
                        FunctionUtils.getString(R.string.gender), gender)));

    }

    public static void setItemValue(TopicModel model, Context context,
            TextView varietyText, TextView petAgeText, TextView petGenderText)
    {
        ServiceInfoModel infoModel = model.getInfo();
        String gender = infoModel.getGender() == 1 ? FunctionUtils
                .getString(R.string.male) : FunctionUtils
                .getString(R.string.female);

        varietyText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.variety),
                OptionBuilder.instance().getPetBreedNameById(
                        infoModel.getBreed()))));
        petAgeText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.age), infoModel.getAge())));
        petGenderText.setText(Html.fromHtml(context.getResources().getString(
                R.string.service_content,
                FunctionUtils.getString(R.string.gender), gender)));
    }

}
