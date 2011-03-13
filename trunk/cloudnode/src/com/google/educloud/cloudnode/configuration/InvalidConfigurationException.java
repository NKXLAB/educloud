package com.google.educloud.cloudnode.configuration;

public class InvalidConfigurationException extends Exception {

	private static final long serialVersionUID = -6690159373682645774L;

	public InvalidConfigurationException(String string, Throwable e) {
		super(string, e);
	}

}
