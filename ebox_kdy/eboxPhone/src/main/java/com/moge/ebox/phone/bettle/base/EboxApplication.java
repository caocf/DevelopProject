package com.moge.ebox.phone.bettle.base;

import java.util.Properties;

import tools.AppContext;
import tools.ImageCacheUtil;
import tools.Logger;
import tools.StringUtils;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;

import com.moge.ebox.phone.bettle.model.UserInfo;
import com.moge.ebox.phone.bettle.network.http.RestClient;
import com.moge.ebox.phone.config.GlobalConfig;

/**
 * 
 *
 * @author donal
 *
 */
public class EboxApplication extends AppContext {
	
	private String logTag = "EboxApplication";
	
	private NotificationManager mNotificationManager;
	
	private static boolean isLogin = false;

	private String sessionId = "";
	
	private static EboxApplication appContext;
	
	private static UserInfo mUser = null;

	/**
	 * 在任意位置获取应用程序Context
	 */
	public static EboxApplication getInstance() {
		return appContext;
	}	
	
	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}
	
	public void onCreate() {
		appContext = this;
		Logger.setDebug(true);
		Logger.getLogger().setTag(logTag);
		ImageCacheUtil.init(this); // com.nostra13.universalimageloader.core.ImageLoaderConfiguration 图片加载框架
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        super.onCreate();
	}
	
	/**
	 * 退出应用
	 * 
	 */
	public void exit() {
		//AppManager.getAppManager().finishAllActivity();
		sendBroadcast(new Intent(GlobalConfig.INTENT_ACTION_FINISH));
	}
	
	/**
	 * 用户是否登录
	 * @return
	 */
	public boolean isLogin() {
		boolean login = false;
		try {
			String loginStr = getProperty("user.login");
			if (StringUtils.empty(loginStr)) {
				login = false;
			}
			else {
				login = (loginStr.equals("1")) ? true : false;
			}
			
			if(login && mUser==null){
				UserInfo user = new UserInfo();
				user.get4App(appContext);
				this.mUser = user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return login;
	}

	/**
	 * 保存登录信息
	 * @param UserInfo
	 */
	@SuppressWarnings("serial")
	public void saveLoginInfo(final UserInfo user) {
		this.isLogin = true;
//		this.sessionId = user.sessionId;
		this.mUser = user;
		setProperties(new Properties(){
			{
				setProperty("user.login","1");
				user.save2App(this);


			}
		});

	}
	
	/**
	 * 修改登录信息
	 * @param UserInfo
	 */
	public void modifyLoginInfo(final UserInfo user) {
		this.mUser = user;
		setProperties(new Properties(){
			{
				user.save2App(this);
			}
		});		
	}
	
	/**
	 * 修改登录信息
	 * @param UserInfo
	 */
	public void modifyLoginPassword(final String password) {
		final UserInfo user = new UserInfo();
		user.get4App(appContext);
		user.password = password;
		this.mUser = user;
		setProperties(new Properties(){
			{
				user.save2App(this);
			}
		});		
	}

	/**
	 * 修改余额
	 * @param balance
	 */
	public void modifyLoginBalance(final String balance) {
		final UserInfo user = new UserInfo();
		user.get4App(appContext);
		user.balance = balance;
		this.mUser = user;
		setProperties(new Properties(){
			{
				user.save2App(this);
			}
		});		
	}

	/**
	 * 获取登录用户电话号码
	 * @return
	 */

	public String getLoginPhone() {
		if(mUser!=null)
			return mUser.telephone;
		else{
			mUser = new UserInfo();
			mUser.get4App(appContext);
			return mUser.telephone;
		}
	}
	
	/**
	 * 获取余额
	 * @return
	 */
	public String getLoginBalance() {
		if(mUser!=null)
			return mUser.balance;
		else{
			mUser = new UserInfo();
			mUser.get4App(appContext);
			return mUser.balance;
		}
	}
	
	/**
	 * 获取登陆用户信息
	 * @return
	 */
	public UserInfo getLoginUserInfo() {
		if(mUser!=null)
			return mUser;
		else{
			mUser = new UserInfo();
			mUser.get4App(appContext);
			return mUser;
		}
	}
	

	public String getLoginPassword() {
		if(mUser!=null)
			return mUser.password;
		else{
			mUser = new UserInfo();
			mUser.get4App(appContext);
			return mUser.password;
		}
	}
	
	public String getLoginSrc() {
		if(mUser!=null)
			return mUser.face_id;
		else{
			mUser = new UserInfo();
			mUser.get4App(appContext);
			return mUser.face_id;
		}
	}

	/**
	 * 注销时清除用户信息
	 * 
	 * */
	public void clearUserInfor(){
		if(mUser!=null){
			mUser.clear2App(appContext);
			mUser = null;
		}else{
			mUser = new UserInfo();
			mUser.clear2App(appContext);
			mUser = null;
		}
	}
	

	/**
	 * 退出登录
	 */
	public void setUserLogout() {
		this.isLogin = false;
		setProperties(new Properties(){
			{
				setProperty("user.login","0");
			}
		});	
		
		clearUserInfor();
		//清除Cookie
		RestClient.getIntance(appContext).clearCookieStore();
	}
	
	public String getResString(int resId){
		return this.getResources().getString(resId);
	}
}
