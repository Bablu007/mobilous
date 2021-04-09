package com.mobilous.ext.plugin.config;

import java.text.MessageFormat;

/**
 * This is where you define the constants.
 *
 * @author yanto
 */
public enum PluginServiceConfig {
	/* 
	 * NOTE: DO NOT MODIFY the callback URL!
	 */
	APPEXE_CALLBACK_URL("https://{0}:8181/commapi/extsvc/authenticate?servicename={1}"),

	// TODO Please set the correct values accordingly.
	MY_DOMAIN_NAME("tslsampoorna.mobilous.com"),
	MY_SERVICE_NAME("tsl_agrico"),
	MY_API_KEY("4c797465796d58796c676470526f796f5a337a694f416a326c7477494b4334566530346a4f58732f443533"),
	MY_SECRET_KEY("1486619509456027346"),
	MY_API_USERNAME("user"),
	MY_API_PASSWORD("password");

	public static final String MY_CALLBACK_URL = MessageFormat.format(PluginServiceConfig.APPEXE_CALLBACK_URL.getValue(), PluginServiceConfig.MY_DOMAIN_NAME.getValue(), PluginServiceConfig.MY_SERVICE_NAME.getValue());

	private String value;

	PluginServiceConfig(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
