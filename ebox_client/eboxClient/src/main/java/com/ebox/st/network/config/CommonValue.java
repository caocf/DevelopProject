package com.ebox.st.network.config;

import com.ebox.ex.network.model.enums.DotType;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;


/**
 * 公共配置项目
 * 
 * @author John
 * 
 */
public class CommonValue {
	public static String PackageName = "ebox";


	public static final String BASE_IP_DEBUG = "http://192.168.1.26:8080";
	public static final String BASE_IP_DEV = "http://115.231.94.49:8080";
	public static final String BASE_IP_RELEASE = "http://60.191.205.112:8090";
	
	
	public static String BASE_API = "";
	public static final String BASE_API_DEBUG = BASE_IP_DEBUG+"/smf/api/execute";
	public static final String BASE_API_DEV = BASE_IP_DEV+"/smf/api/execute";
	public static final String BASE_API_RELEASE = BASE_IP_RELEASE+"/smf/api/execute/";
	
	
	public static String DOWNLOAD_APP = "";
	public static final String DOWNLOAD_API_DEBUG = BASE_IP_DEBUG+"/smf/api/download";
	public static final String DOWNLOAD_API_DEV = BASE_IP_DEV+"/smf/api/download";
	public static final String DOWNLOAD_API_RELEASE = BASE_IP_RELEASE+"/smf/api/download";

	public static String DOWNLOAD_API = "";
	public static final String DOWNLOAD_APP_DEBUG = BASE_IP_DEBUG+"/smf/api/downloadapp";
	public static final String DOWNLOAD_APP_DEV = BASE_IP_DEV+"/smf/api/downloadapp";
	public static final String DOWNLOAD_APP_RELEASE = BASE_IP_RELEASE+"/smf/api/downloadapp";
	
	public static String UPLOAD_API = "";
	public static final String UPLOAD_API_DEBUG = BASE_IP_DEBUG+"/smf/api/upload";
	public static final String UPLOAD_API_DEV = BASE_IP_DEV+"/smf/api/upload";
	public static final String UPLOAD_API_RELEASE = BASE_IP_RELEASE+"/smf/api/upload";

	
	public static String PIC_API = "";
	public static final String BASE_API_EDBUG_PIC = BASE_IP_DEBUG+"/smf";
	public static final String BASE_API_EDV_PIC = BASE_IP_DEV+"/smf";
	public static final String BASE_API_RELEASE_PIC = BASE_IP_RELEASE+"/smf";
	
	
	
	static {
		new CommonValue();
	}

	private CommonValue() {
		switch (Constants.config) {
		case DEBUG:
			BASE_API = BASE_API_DEBUG;
			DOWNLOAD_APP = DOWNLOAD_APP_DEBUG;
			DOWNLOAD_API = DOWNLOAD_API_DEBUG;
			UPLOAD_API = UPLOAD_API_DEBUG;
			PIC_API=BASE_API_EDBUG_PIC;
			break;
		case DEV:
			BASE_API = BASE_API_DEV;
			DOWNLOAD_APP = DOWNLOAD_APP_DEV;
			DOWNLOAD_API = DOWNLOAD_API_DEV;
			UPLOAD_API = UPLOAD_API_DEV;
			PIC_API=BASE_API_EDV_PIC;
			break;
		case RELEASE:
			if(GlobalField.config.getDot() == DotType.YINCHUAN)
			{
				BASE_API = BASE_API_RELEASE;
				PIC_API=BASE_API_RELEASE_PIC;
				DOWNLOAD_APP = DOWNLOAD_APP_RELEASE;
				DOWNLOAD_API = DOWNLOAD_API_RELEASE;
				UPLOAD_API = UPLOAD_API_RELEASE;
//				BASE_API = "http://111.113.21.106:8090"+"/smf/api/execute/";
//				PIC_API="http://111.113.21.106:8090"+"/smf";
//				DOWNLOAD_APP = "http://111.113.21.106:8090"+"/smf/api/downloadapp";
//				DOWNLOAD_API = "http://111.113.21.106:8090"+"/smf/api/download";
//				UPLOAD_API = "http://111.113.21.106:8090"+"/smf/api/upload";
			}
			else
			{
				BASE_API = BASE_API_RELEASE;
				PIC_API=BASE_API_RELEASE_PIC;
				DOWNLOAD_APP = DOWNLOAD_APP_RELEASE;
				DOWNLOAD_API = DOWNLOAD_API_RELEASE;
				UPLOAD_API = UPLOAD_API_RELEASE;
			}
			break;
		}
	}
}
