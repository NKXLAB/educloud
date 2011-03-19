package com.google.educloud.api;

import com.google.educloud.api.clients.EduCloudNodeClient;
import com.google.educloud.api.clients.EduCloudTemplateClient;
import com.google.educloud.api.clients.EduCloudVMClient;

public class EduCloudFactory {

	public static EduCloudVMClient createVMClient(EduCloudConfig config) {

		EduCloudVMClient eduCloudVMClient = new EduCloudVMClient();
		eduCloudVMClient.setConfig(config);

		return eduCloudVMClient;
	}

	public static EduCloudTemplateClient createTemplateClient(
			EduCloudConfig config) {
		
		EduCloudTemplateClient eduCloudTemplateClient = new EduCloudTemplateClient();
		eduCloudTemplateClient.setConfig(config);

		return eduCloudTemplateClient;
	}

	public static EduCloudNodeClient createNodeClient(EduCloudConfig config) {
		
		EduCloudNodeClient eduCloudNodeClient = new EduCloudNodeClient();
		eduCloudNodeClient.setConfig(config);

		return eduCloudNodeClient;
	}
}
