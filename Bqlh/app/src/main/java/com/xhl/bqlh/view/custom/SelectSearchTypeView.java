package com.xhl.bqlh.view.custom;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.type.SearchTyp;

/**
 * Created by Summer on 2016/7/18.
 */
public class SelectSearchTypeView extends PopupWindow {

    private TypeSelectListener listener;
    private Context mContext;

    public SelectSearchTypeView(Context context) {
        super(context);
        mContext = context;

        initView();
    }

    private void initView() {

        View contentView = View.inflate(mContext, R.layout.popup_select_search_type, null);

        View tvPor = contentView.findViewById(R.id.tv_product);
        tvPor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.select("商品", SearchTyp.search_product);
                }
                dismiss();
            }
        });
        View tvShop = contentView.findViewById(R.id.tv_shop);
        tvShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.select("店铺", SearchTyp.search_shop);
                }
                dismiss();
            }
        });

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);

        setTouchable(true);

        setBackgroundDrawable(new BitmapDrawable());

        setContentView(contentView);

    }

    public void setListener(TypeSelectListener listener) {
        this.listener = listener;
    }

    public interface TypeSelectListener {
        void select(String content, int type);
    }
}
