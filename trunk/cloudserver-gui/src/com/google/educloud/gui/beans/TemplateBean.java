package com.google.educloud.gui.beans;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.clients.EduCloudTemplateClient;
import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class TemplateBean {

	private String name;

	private String description;

	private String filename;

	private String size;

	private String ostype;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	public List<Template> getTemplates(HttpSession session) throws EduCloudServerException {
		EduCloudAuthorization auth = (EduCloudAuthorization)session.getAttribute("CLOUD_AUTHENTICATION");
		EduCloudTemplateClient templateClient = EduCloudFactory.createTemplateClient(auth);
		return templateClient.describeTemplates();
	}

	public void createTemplate(HttpSession session) throws EduCloudServerException {
		EduCloudAuthorization auth = (EduCloudAuthorization)session.getAttribute("CLOUD_AUTHENTICATION");
		EduCloudTemplateClient templateClient = EduCloudFactory.createTemplateClient(auth);

		Template template = new Template();
		template.setName(name);
		template.setOsType(ostype);
		template.setFilename(filename);

		templateClient.createTemplate(template);
	}

}
