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
import com.google.educloud.api.clients.EduCloudUserClient;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class DeleteUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1790615610883607696L;

	private static Logger LOG = Logger.getLogger(DeleteUserServlet.class);

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		String parameter = request.getParameter("users");

		String[] templates = parameter.split(";");

		HttpSession session = request.getSession();
		EduCloudAuthorization auth = (EduCloudAuthorization) session
				.getAttribute("CLOUD_AUTHENTICATION");

		EduCloudUserClient userClient = EduCloudFactory.createUserClient(auth);

		ArrayList<Integer> arrayList = new ArrayList<Integer>();

		for (int i = 0; i < templates.length; i++) {
			arrayList.add(Integer.parseInt(templates[i]));
		}

		try {
			userClient.delete(arrayList);
		} catch (EduCloudServerException e1) {
			LOG.error("error on delete user", e1);
		}
	}
}
