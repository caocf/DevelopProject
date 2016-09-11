package com.ebox.ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.RspGetOrganization;
import com.ebox.ex.network.model.base.type.OrganizationType;
import com.ebox.ex.network.request.RequestGetOrgnization;
import com.ebox.ex.ui.adapter.ImageAdapter;
import com.ebox.ex.ui.adapter.OrgnizationAdapter;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择快递公司
 */
public class RegisteNextActivity extends CommonActivity implements View.OnClickListener {

    private DialogUtil dialogUtil;
    private RspGetOrganization rsp;
    private long orgnizationId = -1;
    private long imageId = -1;
    private OrgnizationAdapter adapter;
    private ImageAdapter imageAdapter;

    private GridView gv_orgnization;
    private GridView gv_image;

    public final static String FINISH_STATE = "finish";
    private ArrayList<Long> imageIdList;
    private ArrayList<Long> areaIDList;
    private final static String TAG = "RegisterNextActivity  ";
    private int image_num=12;


    private Tip tip;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_registe_next);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        initViews();
        //获得快递公司信息
        TSGetOrgnizationInfo();


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
        if (tip != null) {
            tip.closeTip();
        }
    }


    private void initViews() {

        gv_orgnization = (GridView) this.findViewById(R.id.gv_origination);
        gv_image = (GridView) this.findViewById(R.id.ex_gv_arn_image);

        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog(this);
        initTitle();

    }

    private void showPrompt(int resId) {
        tip = new Tip(RegisteNextActivity.this,
                getResources().getString(resId),
                null);
        tip.show(0);
    }

    private void showPrompt(String msg) {
        tip = new Tip(RegisteNextActivity.this,
                msg,
                null);
        tip.show(0);
    }

    private Title title;
    private Title.TitleData data;

    private void initTitle() {
        title = (Title) findViewById(R.id.title);
        data = title.new TitleData();
        data.backVisibility = 1;
        data.backHomeVisibility = 0;
        data.tvContent = getResources().getString(R.string.ex_select_org);
        data.tvVisibility = true;
        title.setData(data, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.bt_ok:
//                TSRegister();
//                break;
        }
    }

    private class ImageItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if (imageIdList.size() > 0) {
                imageId = imageIdList.get(position);
                String imageName = organizations.get(position).getOrgnization_name();
                imageAdapter.setPositon(position);
                imageAdapter.notifyDataSetChanged();
                LogUtil.i(TAG + imageId + imageName);
                Intent result=new Intent();
                result.putExtra("id",imageId);
                result.putExtra("name",imageName);
                setResult(RESULT_OK,result);
                finish();
            }

        }
    }

    private class OrgItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
            if (areaIDList.size() > 0) {
                orgnizationId = areaIDList.get(position);
                String textName=organizations.get(position+image_num).getOrgnization_name();
                adapter.setPositon(position);
                adapter.notifyDataSetChanged();
                LogUtil.i(TAG + orgnizationId+textName);
                Intent result=new Intent();
                result.putExtra("id", orgnizationId);
                result.putExtra("name", textName);
                setResult(RESULT_OK, result);
                finish();
            }

        }
    }



    public static String BASE_URL;
    private ArrayList<String> imageList;
    private ArrayList<String> areasList;
    private  List<OrganizationType> organizations;

    private void TSGetOrgnizationInfo() {

        RequestGetOrgnization requestGetOrgnization = new RequestGetOrgnization(new ResponseEventHandler<RspGetOrganization>() {
            @Override
            public void onResponseSuccess(RspGetOrganization result) {
                dialogUtil.closeProgressDialog();
                if (result.isSuccess()) {
                    rsp = result;
                    imageList = new ArrayList<String>();
                    areasList = new ArrayList<String>();
                    areaIDList = new ArrayList<Long>();
                    imageIdList = new ArrayList<Long>();
                    BASE_URL = rsp.getData().getBase_url();
                     organizations = rsp.getData().getOrganizations();

                    for (int i = 0; i < rsp.getData().getOrganizations().size(); i++) {
                        String image_url = BASE_URL + organizations.get(i).getSlug() + ".jpg";
                        long orgnizationID = organizations.get(i).getOrgnization_id();
                        String orgnizationName = organizations.get(i).getOrgnization_name();

                        //获得常用的快递公司
                        if (!orgnizationName.contains("格格快递") && !orgnizationName.equals("魔格快递") && i < image_num) {
                            imageList.add(image_url);
                            imageIdList.add(orgnizationID);
                            LogUtil.i("RegisterNetxActivity+image_url" + image_url + "====" + orgnizationID);
                        }
                        //获得普通的快递公司
                        else if (!orgnizationName.contains("格格快递") && !orgnizationName.equals("魔格快递")) {
                            areasList.add(orgnizationName);
                            areaIDList.add(orgnizationID);
                            LogUtil.i("RegisterNetxActivity+orgnizationName" + orgnizationName + "====" + orgnizationID);
                        }

                    }

                    imageAdapter = new ImageAdapter(RegisteNextActivity.this, imageList);
                    gv_image.setAdapter(imageAdapter);
                    gv_image.setOnItemClickListener(new ImageItemClickListener());


                    adapter = new OrgnizationAdapter(RegisteNextActivity.this, areasList);
                    gv_orgnization.setAdapter(adapter);
                    gv_orgnization.setOnItemClickListener(new OrgItemClickListener());

                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                dialogUtil.closeProgressDialog();
            }
        });
        executeRequest(requestGetOrgnization);

        dialogUtil.showProgressDialog();
    }
}
