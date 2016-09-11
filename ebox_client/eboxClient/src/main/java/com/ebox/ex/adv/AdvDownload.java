package com.ebox.ex.adv;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.database.adv.AdvOp;
import com.ebox.ex.database.adv.AdvTable;
import com.ebox.ex.database.adv.AdvertiseData;
import com.ebox.ex.network.model.RspGetAdv;
import com.ebox.ex.network.model.base.type.Advertise;
import com.ebox.ex.network.model.base.type.AdvertiseClass;
import com.ebox.ex.network.model.enums.UpdateType;
import com.ebox.ex.network.request.RequestGetAdv;
import com.ebox.pub.file.AdvFileOp;
import com.ebox.pub.service.task.CheckTask;
import com.ebox.pub.service.task.report.helper.BackFillHelper;
import com.ebox.pub.utils.LogUtil;

import java.util.ArrayList;

public class AdvDownload implements ResponseEventHandler<RspGetAdv> {

	public void advDownload()
	{
		RequestManager.addRequest(new RequestGetAdv(this),null);
	}


	public boolean isAdvInRsp(AdvertiseData data, RspGetAdv rsp)
	{
		for(int i = 0; i < rsp.getData().size(); i++)
		{
			AdvertiseClass advClass = rsp.getData().get(i);

			for(int j = 0; j < advClass.getAdvertise().size(); j++)
			{
				if(data.getAdver_id().equals(advClass.getAdvertise().get(j).getAdv_id()))
				{
					return true;
				}
			}
		}
		
		return false;
	}


    private void saveAdvJsonToFile(RspGetAdv rsp)
    {

		String adv= JsonSerializeUtil.bean2Json(rsp);
		LogUtil.d("advToJson:",adv);
        AdvFileOp.writeJsonToFile(adv);
    }

	@Override
	public void onResponseSuccess(RspGetAdv result) {
		if (result.isSuccess()) {

			CheckTask.hasAdvDownload=true;

			saveAdvJsonToFile(result);

			// 更新AdvTable
			for(int i = 0; i < result.getData().size(); i++) {
				AdvertiseClass advClass = result.getData().get(i);
                for(int j = 0; j < advClass.getAdvertise().size(); j++)
                {
                    Advertise adv = advClass.getAdvertise().get(j);
                    AdvertiseData data = new AdvertiseData();
                    data.setAdver_id(adv.getAdv_id());
                    data.setContent(adv.getContent());
                    data.setContent_type(adv.getContent_type());
                    data.setType(advClass.getType());
                    data.setState(AdvTable.STATE_0);
                    data.setAudio_content(adv.getAudio_content());
                    AdvOp.addAdvertise(data);
                }

			}
			// 清理过期图片
			ArrayList<AdvertiseData> advList = AdvOp.getAllAdvertise();

			for(int i = 0; i < advList.size(); i++)
			{
				if(!isAdvInRsp(advList.get(i), result))
				{
					AdvOp.deleteAdvertise(advList.get(i));
					DownloadManager.deleteAdvFile(advList.get(i));
				}
			}

			BackFillHelper.instance().backFill(UpdateType.advertise);

		}
	}

	@Override
	public void onResponseError(VolleyError error) {
		LogUtil.e("update adv error"+error.getMessage());
	}
}
