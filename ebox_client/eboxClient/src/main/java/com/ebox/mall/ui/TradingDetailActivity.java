package com.ebox.mall.ui;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.android.volley.VolleyError;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.mall.warehouse.model.ImageModel;
import com.ebox.mall.warehouse.model.RespTradingDetailModel;
import com.ebox.mall.warehouse.model.TradingDetailModel;
import com.ebox.mall.warehouse.network.ErrorCode;
import com.ebox.mall.warehouse.network.request.TradingDetailRequest;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.FunctionUtils;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class TradingDetailActivity extends CommonActivity implements
        OnClickListener,ViewFactory,OnItemSelectedListener
{

    
    private TextView mTitleText;
    private TextView oldPriceText;
    private TextView newPriceText;
    private TextView goodsleft;
    private TextView sell_count;
    private TextView et_num;
    private TextView total_price;
    
    private Button btn_buy;
    private Button product_num_add;
    private Button product_num_del;

    private ImageView is;
	private Gallery gallery;
	private ImageAdapter mAdapter;

    private String mTradingId;
    private TradingDetailModel mTradingDetailModel;
    
    private  List<ImageModel> listImage = new ArrayList<ImageModel>();
    private DialogUtil dialogUtil;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_activity_tradingdetail);

        // receive external params
        mTradingId = getIntent().getStringExtra("trading_id");
        MGViewUtil.scaleContentView(this,R.id.rootView);
        initView();
        initData();
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		title.showTimer();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		title.stopTimer();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();
	}
	

    protected void initView()
    {
    	oldPriceText = (TextView) this.findViewById(R.id.oldPriceText);
        mTitleText = (TextView) this.findViewById(R.id.titleText);
        newPriceText = (TextView) this.findViewById(R.id.newPriceText);
        goodsleft = (TextView) this.findViewById(R.id.goodsleft);
        sell_count = (TextView) this.findViewById(R.id.sell_count);
        et_num = (TextView) this.findViewById(R.id.et_num);
        total_price = (TextView) this.findViewById(R.id.total_price);
        btn_buy = (Button) this.findViewById(R.id.btn_buy);
        btn_buy.setOnClickListener(this);
        product_num_add = (Button) this.findViewById(R.id.product_num_add);
        product_num_add.setOnClickListener(this);
        product_num_del = (Button) this.findViewById(R.id.product_num_del);
        product_num_del.setOnClickListener(this);
        is = (ImageView) findViewById(R.id.switcher);
		
		gallery = (Gallery) findViewById(R.id.gallery);
		mAdapter =  new ImageAdapter(this);
		gallery.setAdapter(mAdapter);
		gallery.setOnItemSelectedListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		
		initTitle();
    }
	private Title title;
	private TitleData data;
	private void initTitle() 
	{
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.pub_huozhan);
		data.tvVisibility=true;
		title.setData(data, this);
	}
    private void initData()
    {

        doTradingDetailRequest(mTradingId);
    }

    private void doTradingDetailRequest(String tradingId)
    {
    	dialogUtil.showProgressDialog();
        TradingDetailRequest request = new TradingDetailRequest(tradingId,
                new ResponseEventHandler<RespTradingDetailModel>()
                {
                    @Override
                    public void onResponseSuccess(RespTradingDetailModel result)
                    {
                    	dialogUtil.closeProgressDialog();
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mTradingDetailModel = result.getData().getTrading();
                            
                            for (String image : mTradingDetailModel.getAttachments().getImages())
                            {
                                ImageModel imageModel = new ImageModel();
                                imageModel.setImage(image);
                                imageModel.setUrl("");
                                listImage.add(imageModel);
                            }
                            
                            mAdapter.setData(listImage);
                            mAdapter.notifyDataSetChanged();

                            newPriceText.setText(getString(R.string.mall_money,
                                    FunctionUtils.getDouble(mTradingDetailModel
                                            .getDiscount_price() * 1.0 / 100)));
                            mTitleText.setText(mTradingDetailModel.getTitle());
                            
                            oldPriceText.setText(getString(R.string.mall_money,
                                    FunctionUtils.getDouble(mTradingDetailModel.getPrice() * 1.0 / 100)));
                            oldPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                            newPriceText
                                    .setText(getString(R.string.mall_money, FunctionUtils
                                            .getDouble(mTradingDetailModel.getDiscount_price() * 1.0 / 100)));
                            sell_count.setText(getString(
                                    R.string.mall_sell_count,
                                    mTradingDetailModel.getSale_num()));
                            goodsleft.setText(getString(
                                    R.string.mall_goodsleft,
                                   ( mTradingDetailModel.getNum()-mTradingDetailModel.getSale_num())));
                            et_num.setText("1");
                            total_price.setText(getString(R.string.mall_money,
                                    FunctionUtils.getDouble(mTradingDetailModel
                                            .getDiscount_price() * 1.0 / 100)));
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                    	dialogUtil.closeProgressDialog();
                        LogUtil.i(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, this);
    }

//    private void showTradingInfo(List<TradingInfoModel> listModel)
//    {
//        if (listModel == null || listModel.size() == 0)
//        {
//            return;
//        }
//
//        StringBuilder sb = new StringBuilder();
//        for (TradingInfoModel model : listModel)
//        {
//            sb.append(model.getName()).append(" : ").append(model.getInfo())
//                    .append("\n");
//        }
//
//        sb.deleteCharAt(sb.length() - 1);
//
//    }

    private void gotoPayActivity()
    {
        Intent intent = new Intent(this, TradingPayActivity.class);
        mTradingDetailModel.setSelected(true);
        mTradingDetailModel.setBuyNum( Integer.parseInt(et_num.getText().toString()));
        intent.putExtra("goods", mTradingDetailModel);
        startActivity(intent);
        finish();
    }
   

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_buy:
            	gotoPayActivity();
                break;
            case R.id.product_num_add:
            	float num =Float.parseFloat(et_num.getText().toString());
            	num++;
            	et_num.setText((int)num+"");
            	total_price.setText(getString(R.string.mall_money,
                        FunctionUtils.getDouble(mTradingDetailModel
                                .getDiscount_price() * num / 100)));
                break;
            case R.id.product_num_del:
            	num =Float.parseFloat(et_num.getText().toString());
            	if(num>1){
            		num--;
            	} else{
            		num=1;
            	}
            	et_num.setText((int)num+"");
            	total_price.setText(getString(R.string.mall_money,
                        FunctionUtils.getDouble(mTradingDetailModel
                                .getDiscount_price() * num / 100)));
                break;
            default:
                break;
        }
    }


    

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	public class ImageAdapter extends BaseAdapter {
		private List<ImageModel> mList;
		public ImageAdapter(Context c) {
			mContext = c;
		}
		
		public void setData(List<ImageModel> list) {
			mList = list;
		};

		public int getCount() {
			if (mList == null)
	        {
	            return 0;
	        }
	        	return mList.size();
			
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView view1 = new ImageView(mContext);
			
			RequestManager.loadImage(view1,
                    RequestManager.getImageUrl(mList.get(position).getImage()+GlobalField.IMAGE_STYLE90_90), 0);
			view1.setAdjustViewBounds(true);
			view1.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return view1;
		}

		private Context mContext;

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		RequestManager.loadImage(is,
                RequestManager.getImageUrl(listImage.get(position).getImage() +GlobalField.IMAGE_STYLE480), 0);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
