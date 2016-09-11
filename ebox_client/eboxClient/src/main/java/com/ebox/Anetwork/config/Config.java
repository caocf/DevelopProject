package com.ebox.Anetwork.config;

public class Config {
	private String terminal_code;
	private Integer count;
    private Integer dot;//版本(0:默认;1:演示版本; 3:银川版本;4:恒喜版本)
	private String led_ip;
	private Integer led_port;
	private String led_open_time;
	private String led_close_time;
	private Integer serviceCtrl;
	private String uart485;
/*	private String areaCode;
	private String waterCode;
	private String gasCode;
	private String elecCode;*/
	private Integer time1;
	private Integer time2;
	private Integer backlight1;
	private Integer backlight2;
	private Integer water;
    /*	private Integer elec;
        private Integer gas;
        private Integer JnWater;
        private Integer JnFuel;
        private Integer TrafficFine;*/
	private Integer CameraCtrl; //0 表示system api； 1：jni
	private Integer isPlatform; //0 表示 Ti； 1：a31s
    private Integer maim_board; //0 v1.0(ttyO1 Ti)； 1：V2.0(ttyO3,Ti); 2:(>V3.0 ttyS3 a31s)
	private Integer scanTimer;	// 扫码开柜强制等待时间
    private Integer theme;//主题 0：默认，1：恒喜1,2：恒喜2

    private Integer NjUms; //0:不开启本地银联功能，1：南京市区银联功能，2：江宁区银联功能

	private String communityId;

	private Integer screen;//0：正常；1：分屏（一个屏幕分成两个）；2：多屏（双屏）
	private Integer web_nj;//0: 不显示，1:显示淘宝网

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	/*public Integer getElec() {
		return elec;
	}

	public void setElec(Integer elec) {
		this.elec = elec;
	}

	public Integer getGas() {
		return gas;
	}

	public void setGas(Integer gas) {
		this.gas = gas;
	}*/

	public String getTerminal_code() {
		return terminal_code;
	}

	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

    public Integer getDot(){return dot;}

    public void  setDot(Integer dot){this.dot = dot;}

    public Integer getMaim_board(){return maim_board;}

    public void setMaim_board(Integer maim_board){this.maim_board = maim_board;}

	public String getLed_ip() {
		return led_ip;
	}

	public void setLed_ip(String led_ip) {
		this.led_ip = led_ip;
	}

	public Integer getLed_port() {
		return led_port;
	}

	public void setLed_port(Integer led_port) {
		this.led_port = led_port;
	}

	public String getLed_open_time() {
		return led_open_time;
	}

	public void setLed_open_time(String led_open_time) {
		this.led_open_time = led_open_time;
	}

	public String getLed_close_time() {
		return led_close_time;
	}

	public void setLed_close_time(String led_close_time) {
		this.led_close_time = led_close_time;
	}

	public Integer getServiceCtrl() {
		return serviceCtrl;
	}

	public void setServiceCtrl(Integer serviceCtrl) {
		this.serviceCtrl = serviceCtrl;
	}
	
	public String getUart485() {
		return uart485;
	}

	public void setUart485(String uart485) {
		this.uart485 = uart485;
	}

	/*public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void ElecCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getWaterCode() {
		return waterCode;
	}

	public void setWaterCode(String waterCode) {
		this.waterCode = waterCode;
	}

	public String getGasCode() {
		return gasCode;
	}

	public void setGasCode(String gasCode) {
		this.gasCode = gasCode;
	}

	public String getElecCode() {
		return elecCode;
	}

	public void setElecCode(String elecCode) {
		this.elecCode = elecCode;
	}*/
	public Integer getTime1() {
		return time1;
	}

	public void setTime1(Integer time1) {
		this.time1 = time1;
	}
	public Integer getTime2() {
		return time2;
	}

	public void setTime2(Integer time2) {
		this.time2 = time2;
	}
	public Integer getBacklight1() {
		return backlight1;
	}

	public void setBacklight1(Integer backlight1) {
		this.backlight1 = backlight1;
	}
	public Integer getBacklight2() {
		return backlight2;
	}

	public void setBacklight2(Integer backlight2) {
		this.backlight2 = backlight2;
	}

	public Integer getWater() {
		return water;
	}

	public void setWater(Integer water) {
		this.water = water;
	}

	/*public Integer getJnWater() {
		return JnWater;
	}

	public void setJnWater(Integer jnWater) {
		JnWater = jnWater;
	}

	public Integer getJnFuel() {
		return JnFuel;
	}

	public void setJnFuel(Integer jnFuel) {
		JnFuel = jnFuel;
	}

	public Integer getTrafficFine() {
		return TrafficFine;
	}

	public void setTrafficFine(Integer trafficFine) {
		TrafficFine = trafficFine;
	}*/

	public Integer getCameraCtrl() {
		return CameraCtrl;
	}

	public void setCameraCtrl(Integer cameraCtrl) {
		CameraCtrl = cameraCtrl;
	}

	public Integer getIsPlatform() {
		return isPlatform;
	}

	public void setIsPlatform(Integer isPlatform) {
		this.isPlatform = isPlatform;
	}

	public Integer getScanTimer() {
		return scanTimer;
	}

	public void setScanTimer(Integer scanTimer) {
		this.scanTimer = scanTimer;
	}

    public Integer getTheme(){return theme;    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public Integer getNjUms(){return NjUms;}
    public void setNjUms(Integer njUms){this.NjUms = njUms;}

	public Integer getScreen() {
		return screen;
	}

	public void setScreen(Integer screen) {
		this.screen = screen;
	}

	public Integer getWeb_nj() {
		return web_nj;
	}

	public void setWeb_nj(Integer web_nj) {
		this.web_nj = web_nj;
	}
}
