package com.ebox.pub.websocket.helper;

import android.util.Log;

import com.ebox.Anetwork.BaseHttpNetCfg;
import com.ebox.AppApplication;
import com.ebox.ex.adv.AdvDownload;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoTable;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.enums.BoxUserState;
import com.ebox.ex.network.model.enums.PickupType;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOp.resultListener;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.boxctl.serial.RstCode;
import com.ebox.pub.file.CtrlFile;
import com.ebox.pub.service.NetworkCtrl;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.service.task.report.AutoReportManager;
import com.ebox.pub.service.task.report.helper.ServiceConfigHelper;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MD5Util;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.websocket.WebSocket;
import com.ebox.pub.websocket.WebSocketConnection;
import com.ebox.pub.websocket.WebSocketException;
import com.ebox.pub.websocket.WebSocketOptions;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * WebSocket 连接管理
 *
 * @author prin
 *
 */
public class WebSocketManager {
	private static final String TAG = "WebSocketManager";
	private static final String TERMINAL_CODE_KEY = "X-AUTH-TERMINAL-CODE";
	private static final String TIMESTAMP_KEY = "X-AUTH-TIMESTAMP";
	private static final String AUTH_SIGN_KEY = "X-AUTH-SIGN";
	private static final String SIGN_KEY = "Kg31du4nnZ0QC7qQVu0Ay5PkSVnOhuhccOIUu3cnRiOmHWI9L6H3zAIqXhy5QEEE";

	private static final String COMMAND_RESULT = "/command/result";
	private static final String COMMAND_RESTART = "/command/terminal/restart"; // 重启
	private static final String COMMAND_UPDATE = "/command/terminal/update"; // 更新
	private static final String COMMAND_OPEN = "/command/terminal/box/open"; // 开柜
	private static final String COMMAND_LOCK = "/command/terminal/box/lock"; // 远程锁定箱门

	private String r_command_id;

	public static WebSocketManager instance;
	private boolean mConnected = false;

	private WebSocketConnection mConnection = new WebSocketConnection();
	protected long last_health;

	private static final long keepAliveTime = 5 * 1000;
	private int lastTextMessageTime;
	private Thread heartBeatThread = null;

	private List<BasicNameValuePair> headers;

	public static WebSocketManager instance() {
		if (instance == null) {
			instance = new WebSocketManager();
		}

		return instance;
	}

	public void init() {
		Log.i(TAG, "websocket init");
		connect();
	}

	public void reconnect() {
		Log.i(TAG, "websocket reconnect");
		mConnection.disconnect();
		getHeaders();
		mConnection.reconnect(headers);
	}

	public void close() {
		Log.i(TAG, "websocket close");
		if (mConnection != null) {
			mConnection.disconnect();
		}
	}

	public boolean isConnected() {
		if(NetworkCtrl.IsConnectedToNetwork(AppApplication.globalContext) && mConnected)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private void connect() {
		Log.i(TAG, "websocket connect");
		try {
			getHeaders();

			WebSocketOptions options = new WebSocketOptions();
			options.setReconnectInterval(500);

			mConnection.connect(BaseHttpNetCfg.Ws_Server_Url
							+ BaseHttpNetCfg.Ws_Server_Suffix, null, mHandler, options,
					headers);
		} catch (WebSocketException e) {
			mConnected = false;
		}
	}

	private WebSocket.ConnectionHandler mHandler = new WebSocket.ConnectionHandler() {
		@Override
		public void onOpen() {
			Log.i(TAG, "websocket onOpen");
			mConnected = true;
			// 与websocket连接成功 开启心跳上报
			lastTextMessageTime = (int) (System.currentTimeMillis() / 1000);
			if(heartBeatThread ==null)
			{
				heartBeatThread = new Thread(new HeartBeatRunnable());
				heartBeatThread.start();
			}
		}

		@Override
		public void onClose(int code, String reason) {
			mConnected = false;
			Log.i(TAG, "websocket onClose");
			// 需要关闭重新启动websocket
			// reconnect();
		}

		@Override
		public void onTextMessage(String payload) {
			// 获得服务器端的数据
			Log.i(TAG, "websocket onTextMessage:"+payload);

			try {
				JSONObject payloadJO = new JSONObject(payload);
				int server_tp = payloadJO.getInt("tp");
				if (server_tp == 1) {
					int server_ts = payloadJO.getInt("ts");
					lastTextMessageTime = server_ts;
					// 继续上报心跳
				} else if (server_tp == 2) {
					// 如果有快递信息则处理数据 ----
					// 如果用户没有快递信息
					// 处理得到的数据
					doWsktResponse(payload);

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onRawTextMessage(byte[] payload) {

		}

		@Override
		public void onBinaryMessage(byte[] payload) {

		}

	};

	/**
	 * 处理返回的响应数据
	 */
	protected void doWsktResponse(String payload) {
		// STRestartTerminal重启终端
		// STUpdateVersion 更新版本
		// STOpenBox 开启箱门
		RtnRslt rslt = new RtnRslt();

		try {
			JSONObject responeJO = new JSONObject(payload);
			String r_command = responeJO.getString("command");
			r_command_id = responeJO.getString("command_id");
			int r_expried = responeJO.getInt("expried");
			responeJO.getString("version");
			responeJO.getInt("timestamp");
			JSONObject data = responeJO.getJSONObject("data");

			// 判断是否过期
			if (r_expried > (int) (System.currentTimeMillis() / 1000))
			{
				if (r_command.equals(COMMAND_OPEN)) {
					// 开柜
					doWsktOpenBox(data);
				}
				else if (r_command.equals(COMMAND_RESTART))
				{
					// 重新启动快递柜
					doWsktRestart(data);
				}
				else if (r_command.equals(COMMAND_UPDATE))
				{
					// 更新快递柜
					doWsktUpdate(data);
				}
				else if (r_command.equals(COMMAND_LOCK))
				{
					// 远程锁定箱门
					doWsktLockBox(data);
				} else {
					// 回送未知命令
					rslt.code = ErrorDesc.EXCEPTION_CODE_F0006;
					doCommandResult(r_command_id, rslt.code,
							ErrorDesc.getErrorDesc(rslt.code));
				}
				LogUtil.i("web socket data ["+data.toString()+"]");
			} else {
				// 报告command状态
				rslt.code = ErrorDesc.EXCEPTION_CODE_E0005;
				doCommandResult(r_command_id, rslt.code,
						ErrorDesc.getErrorDesc(rslt.code));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 锁定箱门
	 *
	 * @param data
	 */
	private void doWsktLockBox(JSONObject data) {
		// TODO Auto-generated method stub
		RtnRslt rslt = new RtnRslt();
		rslt.code = ErrorDesc.CODE_SUCCESS;

		String lockBoxCode;
		int box_user_type =-1;
		try {
			lockBoxCode = data.getString("box_code");
			box_user_type = data.getInt("box_user_state");
			// 下发同步只修改BoxUserState字段
			BoxInfo boxInfo = BoxUtils.getBoxByCode(lockBoxCode);
			if (box_user_type!=-1)
			{
				boxInfo.setBoxUserState(box_user_type);
			}else {
				boxInfo.setBoxUserState(BoxUserState.hardFault);
			}

			int sLock = BoxDynSyncOp.syncBoxUserState(boxInfo);

			if (sLock==-1) {

				doCommandResult(r_command_id, rslt.code, rslt.desc);
			}

			// 上报正确信息
			doCommandResult(r_command_id, rslt.code, rslt.desc);
			Log.i("tag", "--->lock");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rslt.code = ErrorDesc.EXCEPTION_CODE_E0025;
			doCommandResult(r_command_id, rslt.code, rslt.desc);
		}

	}

	/**
	 * 更新功能
	 *
	 * @param data
	 */
	private void doWsktUpdate(JSONObject data) {
		// 需要判断 更新的是什么
		RtnRslt rslt = new RtnRslt();
		rslt.code = ErrorDesc.CODE_SUCCESS;
		try {
			int update_type = data.getInt("update_type");
			switch (update_type) {
				case 1:
					new ServiceConfigHelper(0).undateConfig();
					break;

				case 2:
					// 2版本更新
					if (!CtrlFile.setNeedUpdate(1)) {
						rslt.code = ErrorDesc.EXCEPTION_CODE_F0001;
					}
					break;

				case 3:
					// 3广告更新
					new AdvDownload().advDownload();
					break;

				default:
					break;
			}

			// 上报正确信息
			doCommandResult(r_command_id, rslt.code, rslt.desc);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void doWsktRestart(JSONObject data) {
		Log.i("tag", "--->restart");
		// 需要判断 重启 的是app还是重启system
		RtnRslt rslt = new RtnRslt();
		rslt.code = ErrorDesc.CODE_SUCCESS;
		doCommandResult(r_command_id, rslt.code, rslt.desc);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			int restart_type = data.getInt("restart_type");
			switch (restart_type) {
				case 1:
					// 重启app
					AppApplication.getInstance().exitApp();
					break;

				case 2:
					// 重启系统
					AppApplication.getInstance().restartOs();
					break;

				default:
					break;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 开门
	 *
	 * @param data
	 */
	private void doWsktOpenBox(JSONObject data) {

		RtnRslt rslt = new RtnRslt();
		String ob_box_code = null;
		String ob_terminal_code = null;
		String ob_password = null;
		String ob_user_type=null;
		try {
			ob_box_code = data.getString("box_code");
			ob_terminal_code = data.getString("terminal_code");
			ob_password = data.getString("password");
			ob_user_type=data.getString("user_type");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (AppApplication.getInstance().getTerminal_code()
				.equals(ob_terminal_code))
		{
			// 根据box_code查询本地数据库LocalOrder表该箱门是否有订单
			codeItemQuery(ob_box_code, ob_password,ob_user_type);
		} else
		{
			// 不开门 报告系统内部错误
			rslt.code = ErrorDesc.EXCEPTION_CODE_F0001;
			doCommandResult(r_command_id, rslt.code,
					ErrorDesc.getErrorDesc(rslt.code));
		}

	}

	// 获得OrderLocalInfoTable表的所有订单信息
	private void codeItemQuery(String box_code, String box_password,String user_type) {

		LogUtil.i("open msg boxCode:"+box_code+",password:"+box_password+",userType:"+user_type);
		RtnRslt rslt = new RtnRslt();

		OrderLocalInfo orderLocal = OrderLocalInfoOp.getOrderByBoxCode(box_code);

		if (orderLocal != null)
		{
			LogUtil.i("order info recPwd:"+box_password+" localPwd:"+orderLocal.getPassword());
			// 有订单 校验password是否正确
			if (orderLocal.getPassword().equals(box_password))
			{
				openBox(orderLocal,box_code,user_type);
			}
			else
			{
				rslt.code = ErrorDesc.EXCEPTION_CODE_E0014;
				doCommandResult(r_command_id, rslt.code,ErrorDesc.getErrorDesc(rslt.code));
			}
		}else {
			LogUtil.i("local order not exist");
			rslt.code = ErrorDesc.EXCEPTION_CODE_E0027;
			doCommandResult(r_command_id, rslt.code, ErrorDesc.getErrorDesc(rslt.code));
		}
	}

	public void openBox(OrderLocalInfo order,String ob_box_code,String user_type) {
		//管理员取件
		if (user_type.equals(PickupType.manager))
		{
			//避免本地订单不存在
			if (order!=null)
			{
				if (order.getOrder_state() == OrderLocalInfoTable.STATE_CREATE)
				{
					rst = ErrorDesc.EXCEPTION_CODE_E0028;
					order.setUser_type(user_type);
					OrderLocalInfoOp.updateLocalOrderState(order);
					//释放箱门
					BoxDynSyncOp.boxRelease(ob_box_code);
					//执行取件任务
					AutoReportManager.instance().reportPickupOrder();
				}
				else if (order.getOrder_state() == OrderLocalInfoTable.STATE_WAIT_UPLOAD)
				{
					rst = ErrorDesc.EXCEPTION_CODE_E0026;
				}

			}else
				rst = ErrorDesc.EXCEPTION_CODE_E0027;

			LogUtil.i("manager fetch order, boxCode:"+ob_box_code+" desc:"+ErrorDesc.getErrorDesc(rst));

			doCommandResult(r_command_id, rst, ErrorDesc.getErrorDesc(rst));
			return;
		}
		// 开门
		BoxInfo box = BoxUtils.getBoxByCode(ob_box_code);
		BoxOp op = new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(box);
		op.setListener(new OpenBoxResultListener(ob_box_code, user_type));
		BoxCtrlTask.addBoxCtrlQueue(op);
	}

	private int rst;

	private class OpenBoxResultListener implements resultListener {

		private String box_code;
		private String user_type;

		public OpenBoxResultListener(String box_code,String user_type) {
			this.box_code = box_code;
			this.user_type=user_type;
		}

		@Override
		public void onResult(int result) {
			// TODO Auto-generated method stub
			if (result == RstCode.Success) {
				// 播放音效
				RingUtil.playRingtone(RingUtil.pickup_id);
				rst = ErrorDesc.CODE_SUCCESS;
				// 开门成功 向服务器返回成功 更新LocalOrder表订单为待同步状态

				OrderLocalInfo order = OrderLocalInfoOp.getOrderByBoxCode(box_code);
				//避免本地订单不存在
				if (order!=null)
				{
					order.setUser_type(user_type);//扫码开柜，用户取出
					OrderLocalInfoOp.updateLocalOrderState(order);
				}

				LogUtil.i("web socket open door success boxCode:"+box_code+" useType:"+user_type);

				doCommandResult(r_command_id, rst, ErrorDesc.getErrorDesc(rst));

			} else {
				// 开门失败 则返回开门失败的错误给服务器
				rst = ErrorDesc.EXCEPTION_CODE_E0023;
				doCommandResult(r_command_id, rst, ErrorDesc.getErrorDesc(rst));
			}
			synchronized (WebSocketManager.this) {
				WebSocketManager.this.notify();
			}
		}

	}

	/**
	 * 进行心跳包上报
	 */

	private class HeartBeatRunnable implements Runnable {

		@Override
		public void run() {
			int times = 0;
			while (true) {
				if(!mConnected)
				{
					break;
				}
				// 每5秒进入一次循环，每进入四次循环，发送一次心跳包
				times++;
				if(times > 4)
				{
					times = 1;
				}

				if(times%4 == 0)
				{
					String wsktHeartbeat = getWsktHeartbeat();
					mConnection.sendTextMessage(wsktHeartbeat);
				}
				int num = (int) (System.currentTimeMillis() / 1000)
						- lastTextMessageTime;
			/*	Log.i(TAG, "heartbeat check:last:"+lastTextMessageTime+
						" current:"+System.currentTimeMillis() / 1000+
						" num:"+num);*/
				// 做服务器数据回送计时
				if (num >= 40) {
					Log.i(TAG, "websocket heartbeat timeout");
					mConnected = false;
					break;
				}

				try {
					Thread.sleep(keepAliveTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			heartBeatThread = null;
		}

	}

	private String terminal_code;

	// 生成心跳包
	private int tp = 1;

	public String getWsktHeartbeat() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tp", tp);
			jsonObject.put("ts", (int) (System.currentTimeMillis() / 1000));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	/**
	 * 做command状态上报
	 */
	public void doCommandResult(String command_id, int errcode, String msg) {
		// int command_tp = 2;
		// String command_status = COMMAND_RESULT;
		int command_now = (int) (System.currentTimeMillis() / 1000);
		int pid = android.os.Process.myPid();
		String command_local_id = MD5Util.getMD5String(command_id + command_now
				+ pid);
		JSONObject commandJO = null;

		try {
			JSONObject resultJO = new JSONObject();

			JSONObject dataJO = new JSONObject();
			dataJO.put("command_id", command_id);
			dataJO.put("errcode", errcode);
			dataJO.put("msg", msg);
			dataJO.put("result", resultJO);

			commandJO = new JSONObject();
			commandJO.put("tp", 2);
			commandJO.put("command", COMMAND_RESULT);
			commandJO.put("command_id", command_local_id);
			commandJO.put("timestamp", command_now);
			commandJO.put("expried", 0);
			commandJO.put("version", "1.0");

			commandJO.put("data", dataJO);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mConnection.sendTextMessage(commandJO.toString());

	}

	private void getHeaders() {
		headers = new ArrayList<BasicNameValuePair>();

		terminal_code = AppApplication.getInstance().getTerminal_code();
		timestamp = System.currentTimeMillis() / 1000;
		String sign = mathSign();

		headers.add(new BasicNameValuePair(TERMINAL_CODE_KEY, terminal_code));
		headers.add(new BasicNameValuePair(TIMESTAMP_KEY, String
				.valueOf(timestamp)));
		headers.add(new BasicNameValuePair(AUTH_SIGN_KEY, sign));

	}

	private long timestamp;

	/**
	 * 生成Key
	 *
	 * @return
	 */
	private String mathSign() {
		// TODO Auto-generated method stub
		String signSplit = "terminal_code=" + terminal_code + "&timestamp="
				+ timestamp + "&key=" + SIGN_KEY;

		// 在获得时间戳的时候 出现时间错误 发现是模拟器的原因
		// SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss"
		// );
		// String d = format.format(System.currentTimeMillis());
		// Log.i("tag", "------------>time"+d);

		// try {
		// // Date date=format.parse(d);
		// Log.i("tag", "------------>time"+d);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// Log.i("tag", "-------->"+signSplit+"\n");
		return MD5Util.getMD5String(signSplit);
	}

}
