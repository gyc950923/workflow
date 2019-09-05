package com.online.engine.common;

import java.util.ResourceBundle;

public class SysConfigUtil {

	private static final ResourceBundle sysConfig = ResourceBundle.getBundle("sysconfig-engine");

	public static String getString(String paramName) {
		return sysConfig.getString(paramName);
	}
}
