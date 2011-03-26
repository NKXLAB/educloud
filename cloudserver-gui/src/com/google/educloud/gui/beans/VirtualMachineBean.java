package com.google.educloud.gui.beans;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.clients.EduCloudTemplateClient;
import com.google.educloud.api.clients.EduCloudVMClient;
import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class VirtualMachineBean {

	private String name;

	private String description;

	private String template;

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTemplate(String template) {
		this.template= template;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getTemplate() {
		return template;
	}

	public void createVirtualMachine(HttpSession session) throws EduCloudServerException {
		EduCloudAuthorization auth = (EduCloudAuthorization)session.getAttribute("CLOUD_AUTHENTICATION");

		EduCloudVMClient vmClient = EduCloudFactory.createVMClient(auth);
		EduCloudTemplateClient templateClient = EduCloudFactory.createTemplateClient(auth);

		int tplId = Integer.parseInt(this.template);
		Template loadedTemplate = templateClient.getTemplate(tplId);

		VirtualMachine virtualMachine = new VirtualMachine();
		virtualMachine.setName(name);
		virtualMachine.setTemplate(loadedTemplate);

		vmClient.createVM(virtualMachine);
	}

	public List<VirtualMachine> getVirtualMachines(HttpSession session) throws EduCloudServerException {
		EduCloudAuthorization auth = (EduCloudAuthorization)session.getAttribute("CLOUD_AUTHENTICATION");
		EduCloudVMClient createVMClient = EduCloudFactory.createVMClient(auth);
		return createVMClient.describeInstances();
	}
}
