package com.ebox.pub.ledctrl;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;
import com.ebox.ex.network.model.req.ReqLedRefresh;
import com.ebox.ex.network.model.base.type.LedInfo;
import com.ebox.ex.network.model.enums.UpdateType;
import com.ebox.ex.network.model.req.ReqBackFillUpdate;
import com.ebox.ex.network.model.base.type.LedConfigType;
import com.ebox.ex.network.model.RspLedConfig;
import com.ebox.ex.network.model.base.type.LedContentType;
import com.ebox.ex.network.model.RspLedContent;
import com.ebox.ex.network.request.RequestGetLedConfig;
import com.ebox.ex.network.request.RequestGetLedContent;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.pub.service.task.report.helper.BackFillHelper;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.file.FileOp;
import com.ebox.pub.service.global.GlobalField;

public class LedUtil{
	
	public void updateLedConfig(){
		RequestGetLedConfig request = new RequestGetLedConfig(new ResponseEventHandler<RspLedConfig>(){

			@Override
			public void onResponseSuccess(RspLedConfig result) {
				if (result.getStatus()==0 && result.getResult().size()>0)
				{
					LedConfigType ledConfig = result.getResult().get(0);
					GlobalField.config.setLed_open_time(ledConfig.getOpen_time());
					GlobalField.config.setLed_close_time(ledConfig.getClose_time());
					FileOp.saveTemp(GlobalField.config);
					
					ReqBackFillUpdate req = new ReqBackFillUpdate();
					req.setUpdate_type(UpdateType.led_config);
				}
			}

			@Override
			public void onResponseError(VolleyError error) {
				LogUtil.d(GlobalField.tag, error.getMessage());
				
			}
			
		});
		
		RequestManager.addRequest(request, null);
	}
	
	
	public void updateLedContent()
	{
		RequestGetLedContent request = new RequestGetLedContent(new ResponseEventHandler<RspLedContent>(){

			@Override
			public void onResponseSuccess(RspLedContent result) {
				if (result.getStatus()==0)
				{
					final List<LedContentType> ledContent = result.getResult();
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							LedCtrl led = new LedCtrl();
							
							if(led.init())
							{
								ReqLedRefresh refreshReq = new ReqLedRefresh();

								List<LedInfo> ledInfos = new ArrayList<LedInfo>();
								LedInfo ledInfo;
								for(int i = 0; i < ledContent.size(); i++)
								{
									ledInfo = new LedInfo();
									ledInfo.setEndx(ledContent.get(i).getEndx());
									ledInfo.setEndy(ledContent.get(i).getEndy());
									ledInfo.setIn(ledContent.get(i).getIn());
									ledInfo.setOut(ledContent.get(i).getOut());
									ledInfo.setShowTime(ledContent.get(i).getShow_time());
									ledInfo.setSpeed(ledContent.get(i).getSpeed());
									ledInfo.setStartx(ledContent.get(i).getStartx());
									ledInfo.setStarty(ledContent.get(i).getStarty());
									ledInfo.setText(ledContent.get(i).getText());
									ledInfos.add(ledInfo);
								}
								refreshReq.setContent(ledInfos);

								if(led.downloadText(refreshReq))
								{
									BackFillHelper.instance().backFill(UpdateType.led_content);
								}
								led.close();
							}
						}
					}).start();
				}
				
			}

			@Override
			public void onResponseError(VolleyError error) {
				LogUtil.d(GlobalField.tag, error.getMessage());
			}
			
		});
		
		RequestManager.addRequest(request, null);
	}
}
