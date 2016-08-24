package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.AddressModel;
import com.xhl.world.ui.event.AddressEvent;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/12/28.
 */
public class AddressItemDataHolder extends RecyclerDataHolder {

    public AddressItemDataHolder(Object data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = View.inflate(context, R.layout.item_address, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        AutoUtils.auto(view);

        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        AddressViewHolder holder = (AddressViewHolder) vHolder;
        if (data instanceof AddressModel) {
            holder.onBindData((AddressModel) data);
        }
    }

    static class AddressViewHolder extends RecyclerViewHolder implements View.OnClickListener {

        @ViewInject(R.id.tv_location_people)
        private TextView tv_location_people;

        @ViewInject(R.id.tv_location_phone)
        private TextView tv_location_phone;

        @ViewInject(R.id.tv_location_details)
        private TextView tv_location_details;

        @ViewInject(R.id.check_default_address)
        private CheckBox check_default_address;

        @ViewInject(R.id.ripple_delete)
        private RelativeLayout ripple_delete;

        @ViewInject(R.id.ripple_edit)
        private RelativeLayout ripple_edit;

        @ViewInject(R.id.rl_select)
        private RelativeLayout rl_select;

        private AddressModel mAddress;

        public AddressViewHolder(View view) {
            super(view);

            x.view().inject(this, view);

            ripple_edit.setOnClickListener(this);
            ripple_delete.setOnClickListener(this);
            rl_select.setOnClickListener(this);

            check_default_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    check_default_address.setChecked(true);
                    post(AddressEvent.Type_Default_address);
                }
            });

        }

        private void post(int action) {
            AddressEvent event = new AddressEvent();
            event.position = getAdapterPosition();
            event.actionType = action;
            event.model = mAddress;
            EventBus.getDefault().post(event);
        }

        public void onBindData(AddressModel address) {
            mAddress = address;

            tv_location_people.setText(address.getConsigneeName());
            tv_location_details.setText(address.getAddress());
            tv_location_phone.setText(address.getTelephone());
            if (address.isDefault()) {
                check_default_address.setChecked(true);
            } else
                check_default_address.setChecked(false);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ripple_delete:
                    post(AddressEvent.Type_delete_address);//删除
                    break;

                case R.id.ripple_edit:
                    post(AddressEvent.Type_edit_address);//编辑
                    break;

                case R.id.rl_select:
                    post(AddressEvent.Type_select_address);//选择
                    break;
            }
        }
    }
}
