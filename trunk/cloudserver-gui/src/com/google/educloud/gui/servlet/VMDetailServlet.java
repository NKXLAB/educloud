package com.google.educloud.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.clients.EduCloudVMClient;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;
import com.google.gson.Gson;

public class VMDetailServlet extends HttpServlet {

	private static final long serialVersionUID = -4071580709192430122L;

	private static Logger LOG = Logger.getLogger(VMDetailServlet.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		String parameter = request.getParameter("vmId");
		HttpSession session = request.getSession();
		EduCloudAuthorization auth = (EduCloudAuthorization) session
				.getAttribute("CLOUD_AUTHENTICATION");
		int vmId = Integer.parseInt(parameter);

		LOG.debug("Will return virtual machine #" + vmId);

		EduCloudVMClient vmClient = EduCloudFactory.createVMClient(auth);
		VirtualMachine virtualMachine;

		try {
			virtualMachine = vmClient.getVirtualMachine(vmId);

			Gson gson = new Gson();

			try {
				response.getOutputStream().print(gson.toJson(virtualMachine));
			} catch (IOException e) {
				LOG.error("on write virtual machine", e);
				throw new ServletException(e);
			}
		} catch (EduCloudServerException e1) {
			LOG.error("on load virtual machine", e1);
		}
	}

}
