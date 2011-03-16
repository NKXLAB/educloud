package com.google.educloud.cloudnode.virtualbox;

import javax.xml.ws.BindingProvider;

import org.virtualbox.service.IVirtualBox;
import org.virtualbox.service.IWebsessionManager;
import org.virtualbox.service.VboxPortType;
import org.virtualbox.service.VboxService;

public class VirtualBoxConnector {

	private static VboxPortType port;

	private static VboxPortType getPortType() {
		if (null == port) {
			VboxService svc = new VboxService();
			port = svc.getVboxServicePort();
			((BindingProvider) port).getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					"http://localhost:18083/");
		}

		return port;
	}

	private static VboxPortType getPortType(String url) {
		if (null == port) {
			VboxService svc = new VboxService();
			port = svc.getVboxServicePort();
			((BindingProvider) port).getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					url);
		}

		return port;
	}

	public static IVirtualBox connect(String url) {
		IWebsessionManager iWebsessionManager = new IWebsessionManager(getPortType(url));
		return iWebsessionManager.logon("foo", "bar");
	}

	public static IVirtualBox restoreSession(String session) {
		return new IVirtualBox(session, getPortType());
	}
}
