package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.AddressModel;
import com.moge.gege.model.CommunityModel;
import com.moge.gege.model.DistrictModel;

public class AddressListAdapter extends BaseCachedListAdapter<AddressModel>
        implements OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private AddressListListener mListener;
    private String mAddressId;

    public AddressListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(AddressListListener listener)
    {
        mListener = listener;
    }

    public void setSelectAddress(String id)
    {
        mAddressId = id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_address, null);

            holder = new ViewHolder();
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.phoneText = (TextView) convertView
                    .findViewById(R.id.phoneText);
            holder.addressText = (TextView) convertView
                    .findViewById(R.id.addressText);
            holder.selectedImage = (ImageView) convertView
                    .findViewById(R.id.selectedImage);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        AddressModel model = list.get(position);
        holder.phoneText.setText(model.getMobile());
        DistrictModel districtModel = model.getDistrict();
        if (districtModel != null)
        {
            holder.nameText.setText(model.getName());
            holder.addressText.setText(districtModel.getProvince()
                    + districtModel.getCity() + districtModel.getName()
                    + model.getAddress());
        }

        CommunityModel communityModel = model.getCommunity();
        if (communityModel != null)
        {
            holder.nameText.setText(model.getDelivery_name());
            holder.addressText.setText(communityModel.getProvince()
                    + communityModel.getCity() + communityModel.getDistrict()
                    + communityModel.getAddress());
        }

        if (mAddressId.equals(model.get_id()))
        {
            holder.selectedImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.selectedImage.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder
    {
        TextView nameText;
        TextView phoneText;
        TextView addressText;
        ImageView selectedImage;
    }

    public interface AddressListListener
    {
        public void onAddressItemClick(AddressModel model);

        public void onAddressItemLongClick(AddressModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onAddressItemClick(list.get(position));
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id)
    {
        if (mListener != null)
        {
            mListener.onAddressItemLongClick(list.get(position));
            return true;
        }

        return false;
    }
}
