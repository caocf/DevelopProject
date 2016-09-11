package com.ebox.pub.service.task.report;

import android.util.Log;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.RspReportTerminalStatus;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.req.ReqReportTerminalStatus;
import com.ebox.ex.network.request.ReportBoxStateRequest;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by Android on 2015/9/1.
 */
public class TerminalBoxReport implements IReport, ResponseEventHandler<RspReportTerminalStatus> {


    private static final String TAG = "TerminalBoxReport";

    private ReqReportTerminalStatus mReq;

    private void readStatus() {
        if (!GlobalField.boxLocalInit) {
            return;
        }
        if (!GlobalField.deviceInit) {
            Log.i(TAG, "box info not write to sql");
            return;
        }
        // 上报服务端
        mReq = new ReqReportTerminalStatus();

        ArrayList<BoxInfoType> boxInfoList = new ArrayList<BoxInfoType>();
        ArrayList<BoxInfo> boxList = BoxDynSyncOp.getSyncBoxList();

        if (boxList != null)
        {
            for (int i = 0; i < boxList.size(); i++)
            {
                BoxInfo info = boxList.get(i);

                BoxInfoType boxInfo = new BoxInfoType();
                boxInfo.setBoxCode(BoxUtils.generateBoxCode(info.getBoardNum(), info.getBoxNum()));
                boxInfo.setDoorStatus(info.getDoorState());
                boxInfo.setBoxStatus(info.getBoxState() == null ? 0 : info.getBoxState());
                boxInfoList.add(boxInfo);
            }
        }
        mReq.setBoxes(boxInfoList);
    }


    @Override
    public void create() {
        readStatus();
    }

    @Override
    public void report() {
        if (!SharePreferenceHelper.getInitBoxState())
        {
            LogUtil.e("柜子未完成初始化操作，不能完成格口数据上报");
            return;
        }
        if (mReq!=null)
        {
            ReportBoxStateRequest request = new ReportBoxStateRequest(mReq,this);
            RequestManager.addRequest(request, null);
        }

    }

    @Override
    public int reportType() {
        return Type_TerminalStatusReport;
    }

    @Override
    public void onResponseError(VolleyError error) {
        LogUtil.e(TAG, "upload box door state error " + error.getMessage());
        mReq=null;
    }

    @Override
    public void onResponseSuccess(RspReportTerminalStatus result) {
        if (result==null) {
            return;
        }

        if (!result.isSuccess())
        {
            mReq=null;
            return;
        }
        LogUtil.i(TAG, "upload box state success ");

        SharePreferenceHelper.saveSyncBoxDoorState(true);

        BoxDynSyncOp.setBoxSync();

        mReq = null;
    }

}
