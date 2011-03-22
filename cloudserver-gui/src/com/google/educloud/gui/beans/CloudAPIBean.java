package com.google.educloud.gui.beans;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.clients.EduCloudNodeClient;
import com.google.educloud.api.clients.EduCloudTemplateClient;
import com.google.educloud.api.clients.EduCloudVMClient;

public class CloudAPIBean {

	private EduCloudVMClient vmClient;
	private EduCloudTemplateClient templateClient;
    private EduCloudNodeClient nodeClient;

	public EduCloudTemplateClient getTemplateClient() {
		if (templateClient == null) {
			EduCloudConfig config = new EduCloudConfig();
			config.setHost("localhost");
			config.setPort(8000);
			config.setLogin("admin");
			config.setPass("123");
			EduCloudAuthorization auth = EduCloudFactory.createAuthorization(config);
			templateClient = EduCloudFactory.createTemplateClient(auth);
		}
		return  templateClient;
	}

	public EduCloudVMClient getVMClient() {
		if (vmClient == null) {
			EduCloudConfig config = new EduCloudConfig();
			config.setHost("localhost");
			config.setPort(8000);
			config.setLogin("admin");
			config.setPass("123");
			EduCloudAuthorization auth = EduCloudFactory.createAuthorization(config);
			vmClient = EduCloudFactory.createVMClient(auth);
		}
		return vmClient;
	}

	public EduCloudNodeClient getNodeClient() {
		if (nodeClient == null) {
			EduCloudConfig config = new EduCloudConfig();
			config.setHost("localhost");
			config.setPort(8000);
			config.setLogin("admin");
			config.setPass("123");
			EduCloudAuthorization auth = EduCloudFactory.createAuthorization(config);
			nodeClient = EduCloudFactory.createNodeClient(auth);
		}
		return  nodeClient;
	}
}
