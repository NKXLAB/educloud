package com.google.educloud.cloudserver.configuration;

public class InvalidConfigurationException extends Exception {

	private static final long serialVersionUID = -4129139323347893663L;

	public InvalidConfigurationException(String string, Throwable e) {
		super(string, e);
	}

}
