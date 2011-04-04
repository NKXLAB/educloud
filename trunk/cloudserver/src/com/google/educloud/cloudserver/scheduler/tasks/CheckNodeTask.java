package com.google.educloud.cloudserver.scheduler.tasks;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeClient;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.cloudserver.selector.AbstractNodeSelector;
import com.google.educloud.internal.entities.Node;


public class CheckNodeTask extends AbstractTask {

	private static Logger LOG = Logger.getLogger(CheckNodeTask.class);

	public static final String PARAM_NODE_ID = "PARAM_NODE_ID";

	@Override
	public String getType() {
		return "CHECKNODE";
	}

	@Override
	public void run() {

		markAsRunning();

		// load node details from database
		String parameter = getParameter(PARAM_NODE_ID);
		
		Node node = NodeDao.getInstance().findNodeById(Integer.parseInt(parameter));

		LOG.debug("cloud server will try contact node #" + parameter);

		// create webservice client
		NodeClient nodeClient = ClientFactory.createNodeClient(node);

		// 1) Call node service
		try {
			node = nodeClient.checkNodeStatus(node);
			AbstractNodeSelector.getInstance().registerNode(node);
		} catch (NodeComunicationException e) {
			markAsCompleted();
			AbstractNodeSelector.getInstance().unregisterNode(node);
			LOG.error("Error on try contact node #"+ parameter);
			return;
		}

		// 2) Update last ping if node was successful return
		node.setLastPing(Calendar.getInstance().getTime());
		NodeDao.getInstance().updateLastPing(node);

		// 3) reschedule task
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, 50);
		LOG.debug("will reschedule task to " + calendar.getTime());
		reschedule(calendar.getTime());
	}

}
