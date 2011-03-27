package com.google.educloud.gui.servlet;

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

public class ControlVirtualMachineServlet extends HttpServlet {

	private static final long serialVersionUID = 2176426477092628127L;

	private static Logger LOG = Logger.getLogger(ControlVirtualMachineServlet.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		String parameterId = request.getParameter("id");
		String parameterAction = request.getParameter("action");
		HttpSession session = request.getSession();
		EduCloudAuthorization auth = (EduCloudAuthorization) session
				.getAttribute("CLOUD_AUTHENTICATION");
		int vmId = Integer.parseInt(parameterId);

		EduCloudVMClient vmClient = EduCloudFactory.createVMClient(auth);
		VirtualMachine virtualMachine;

		try {
			virtualMachine = vmClient.getVirtualMachine(vmId);

			if ("start".equals(parameterAction)) {
				vmClient.startVM(virtualMachine);
			} else if ("stop".equals(parameterAction)) {
				vmClient.stopVM(virtualMachine);
			}
		} catch (EduCloudServerException e1) {
			LOG.error("on control virtual machine", e1);
		}
	}

}
