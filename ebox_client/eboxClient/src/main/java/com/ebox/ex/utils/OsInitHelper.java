package com.ebox.ex.utils;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.RspPullBoxState;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.base.type.ItemRackBoxType;
import com.ebox.ex.network.model.req.ReqCheckLocalConfig;
import com.ebox.ex.network.request.CheckLocalConfigRequest;
import com.ebox.ex.network.request.PullBoxStateRequest;
import com.ebox.pub.boxctl.RackInfo;
import com.ebox.pub.service.global.GlobalField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2015/10/10.
 */
public class OsInitHelper {

    private OsInitListener mL;

    public OsInitHelper(OsInitListener mL) {
        this.mL = mL;
    }

    public interface OsInitListener{

        void updateSuccess();
        void checkSuccess();
        void updateFailed(String msg);
        void checkFailed(String msg);

    }

    private void clear(){

    }

    public OsInitHelper updateBoxState(){

        PullBoxStateRequest request=new PullBoxStateRequest(new ResponseEventHandler<RspPullBoxState>() {
            @Override
            public void onResponseSuccess(RspPullBoxState result) {
                if (result.isSuccess()) {
                    List<BoxInfoType> data = result.getData();
                    BoxDynSyncOp.osSyncAllBoxState(data);
                    if (mL != null) {
                        mL.updateSuccess();
                    }
                    SharePreferenceHelper.saveInitBoxState(true);
                } else {
                    if (mL != null) {
                        mL.updateFailed(result.getMsg());
                    }
                    SharePreferenceHelper.saveInitBoxState(false);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                if (mL != null) {
                    mL.updateFailed(error.getMessage());
                }
                SharePreferenceHelper.saveInitBoxState(false);
            }
        });

        RequestManager.addRequest(request,null);
        return this;
    }

    private  List<ItemRackBoxType> getRackBox() {

        ArrayList<ItemRackBoxType> rackBoxTypes = new ArrayList<ItemRackBoxType>();
        ItemRackBoxType item;
        if(GlobalField.boxInfoLocal != null)
        {
            for(int i = 0 ; i < GlobalField.boxInfoLocal.size(); i++)
            {
                RackInfo rackInfo = GlobalField.boxInfoLocal.get(i);
                item = new ItemRackBoxType();
                item.setRack_num(rackInfo.getCount());
                item.setRack(rackInfo.getBoardNum());

                rackBoxTypes.add(item);
            }
        }

        return rackBoxTypes;
    }


    public OsInitHelper checkLocalConfig()
    {
        ReqCheckLocalConfig checkLocalConfig=new ReqCheckLocalConfig();
        List<ItemRackBoxType> rackBox = getRackBox();
        checkLocalConfig.setRack(rackBox);

        CheckLocalConfigRequest request=new CheckLocalConfigRequest(checkLocalConfig, new ResponseEventHandler<BaseRsp>() {
            @Override
            public void onResponseSuccess(BaseRsp result) {
                if (result.isSuccess()) {
                    if (mL != null) {
                        mL.checkSuccess();
                    }
                    SharePreferenceHelper.saveCheckConifgState(true);
                }else {
                    if (mL != null) {
                        mL.checkFailed(result.getMsg());
                    }
                    SharePreferenceHelper.saveCheckConifgState(false);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                if (mL != null) {
                    mL.checkFailed(error.getMessage());
                }
                SharePreferenceHelper.saveCheckConifgState(false);
            }
        });

        RequestManager.addRequest(request,null);
        return this;
    }

}
