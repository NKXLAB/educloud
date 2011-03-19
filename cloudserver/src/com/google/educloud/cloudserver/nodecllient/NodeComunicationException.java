package com.google.educloud.cloudserver.nodecllient;


public class NodeComunicationException extends Exception {

	private static final long serialVersionUID = 3654681900968974840L;

	public NodeComunicationException(String msg) {
		super(msg);
	}

	public NodeComunicationException(String string, Throwable e) {
		super(string, e);
	}

}
