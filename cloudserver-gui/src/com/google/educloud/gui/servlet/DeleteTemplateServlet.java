package com.google.educloud.gui.servlet;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.clients.EduCloudTemplateClient;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class DeleteTemplateServlet extends HttpServlet {

	private static final long serialVersionUID = -939376825028361077L;

	private static Logger LOG = Logger.getLogger(DeleteTemplateServlet.class);

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		String parameter = request.getParameter("templates");

		String[] templates = parameter.split(";");

		HttpSession session = request.getSession();
		EduCloudAuthorization auth = (EduCloudAuthorization) session
				.getAttribute("CLOUD_AUTHENTICATION");

		EduCloudTemplateClient templateClient = EduCloudFactory.createTemplateClient(auth);

		ArrayList<Integer> arrayList = new ArrayList<Integer>();

		for (int i = 0; i < templates.length; i++) {
			arrayList.add(Integer.parseInt(templates[i]));
		}

		try {
			templateClient.delete(arrayList);
		} catch (EduCloudServerException e1) {
			LOG.error("error on delete template", e1);
		}
	}
}
