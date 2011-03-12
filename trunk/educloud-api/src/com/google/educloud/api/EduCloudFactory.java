package com.google.educloud.api;

import com.google.educloud.api.clients.EduCloudVMClient;

public class EduCloudFactory {

	public static EduCloudVMClient createVMClient(EduCloudConfig config) {

		EduCloudVMClient eduCloudVMClient = new EduCloudVMClient();
		eduCloudVMClient.setConfig(config);

		return eduCloudVMClient;
	}
}
