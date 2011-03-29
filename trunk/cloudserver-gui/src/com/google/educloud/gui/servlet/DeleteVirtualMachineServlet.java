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
import com.google.educloud.api.clients.EduCloudVMClient;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class DeleteVirtualMachineServlet extends HttpServlet {

	private static final long serialVersionUID = -939376825028361077L;

	private static Logger LOG = Logger.getLogger(DeleteVirtualMachineServlet.class);

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		String parameter = request.getParameter("vms");

		String[] vms = parameter.split(";");

		HttpSession session = request.getSession();
		EduCloudAuthorization auth = (EduCloudAuthorization) session
				.getAttribute("CLOUD_AUTHENTICATION");

		EduCloudVMClient vmClient = EduCloudFactory.createVMClient(auth);

		ArrayList<Integer> arrayList = new ArrayList<Integer>();

		for (int i = 0; i < vms.length; i++) {
			arrayList.add(Integer.parseInt(vms[i]));
		}

		try {
			vmClient.delete(arrayList);
		} catch (EduCloudServerException e1) {
			LOG.error("error on delete virtual machine", e1);
		}
	}
}
