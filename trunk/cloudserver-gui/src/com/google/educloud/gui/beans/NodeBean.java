package com.google.educloud.gui.beans;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.clients.EduCloudNodeClient;
import com.google.educloud.api.entities.Node;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class NodeBean {

	public List<Node> getNodes(HttpSession session) throws EduCloudServerException {
		EduCloudAuthorization auth = (EduCloudAuthorization)session.getAttribute("CLOUD_AUTHENTICATION");
		EduCloudNodeClient nodeClient = EduCloudFactory.createNodeClient(auth);

		return nodeClient.decribeNodes();
	}
}
