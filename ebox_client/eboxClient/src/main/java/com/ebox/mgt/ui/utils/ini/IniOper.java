package com.ebox.mgt.ui.utils.ini;

import org.ini4j.Config;
import org.ini4j.Ini;

import java.nio.charset.Charset;

public class IniOper {
	private Ini ini;
	public IniOper()
	{
		ini = new Ini();
		Config cfg = new Config();
		cfg.setComment(true);
		cfg.setFileEncoding(Charset.forName("utf-8"));
		ini.setConfig(cfg);
	}
	
	public Ini getIni()
	{
		return ini;
	}
}
