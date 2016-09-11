package com.ebox.pub.service.global;

import com.ebox.Anetwork.config.Config;
import com.ebox.Anetwork.config.Temp;
import com.ebox.ex.network.model.base.type.ServiceConfig;
import com.ebox.ex.network.model.base.type.ShowConfig;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.RackInfo;
import com.ebox.st.model.LicData;
import com.ebox.st.model.RefuseModel;

import java.util.ArrayList;

public class GlobalField {
	// image suffix config for qiniu
	public static final String IMAGE_STYLE480 = "/style480.png";
	public static final String IMAGE_STYLE300_300 = "/style300x300.png";
	public static final String IMAGE_STYLE300 = "/style300.png";
	public static final String IMAGE_STYLE150_150 = "/style150x150.png";
	public static final String IMAGE_STYLE150 = "/style150.png";
	public static final String IMAGE_STYLE90_90 = "/style90x90.png";
	public static int mShippingFee = 0; // 5￥
	public static int mLowestFee = 6000; // 60￥
	// 本地配置文件
	public static Config config;
	// 本地缓存文件
	public static Temp temp;
	// LED状态
	public static Boolean ledOpen = null;
	// 服务器配置文件
	public static ServiceConfig serverConfig;
	//服务配置展示信息
	public static ShowConfig showConfig;
	// 格口信息（本地）
	public static ArrayList<RackInfo> boxInfoLocal;
	public static boolean boxLocalInit = false;
	// 设备启动后
	public static boolean deviceInit = false;

	// 代理地址
	public static String hostUrl = "10.72.14.147";// 10.0.0.172
	// 代理端口
	public static int hostPort = 80;
	// ctwap代理地址
	public final static String ctwaphost = "10.0.0.200";
	// 会话ID
	public static String sessionID;
	// 主副板操作消息队列
	public static ArrayList<BoxOp> boxCtrlQueue = new ArrayList<BoxOp>();
	// 屏保时间
	public static int screenProtectTimes = 99;

	// 存储上一次打开的箱门，下次选择另外的门，避免某个门故障造成永远选择弹开这个故障门，造成无法存件
	public static int big_box_index = -1;
	public static int middle_box_index = -1;
	public static int little_box_index = -1;
	public static int tiny_box_index = -1;

	// 485异常检测
	public static int portOpCnt = 0;
	public static int portFailCnt = 0;
	public static int portFailAllCnt = 0;
	// 系统不可恢复异常 1:485无法通讯
	public static int systemErr = 0;

	// ums 交易
	public static boolean umsTransExsit = false;
	// 重要流程日志tag
	public static String tag = "EboxFlow";

	// 银联签名key
	public static String Umskey = "N52pQf7VfA1CZKOZZjt9rO3NXNLECNkEC1vl2Efy1zEg6IqdlRb6lAdFK7hvMPsP6w3VjL7e5DnSsQKWo0lSbMjjlYJjchg20iwX5heuAcLP45x2sN5vqMVpssupt8";

    //st 办证数据
    public static LicData licData = new LicData();

    //st 拒绝流程的数据
    public static RefuseModel refuseData = new RefuseModel();
}
