package com.google.educloud.cloudserver.scheduler.tasks;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;


public class StartVmTask extends AbstractTask {

	public static final String VM_ID = "VM_ID";

	private static Logger LOG = Logger.getLogger(StartVmTask.class);

	@Override
	public void run() {

		markAsRunning();

		// 1) select a registered host
		Node node = NodeDao.getInstance().findRandomicNode();
		LOG.debug("Selected node for new virtual machine: #" + node.getId());

		// 2) load virtual machine from database
		String vmId = getParameter(VM_ID);
		VirtualMachine vm = VirtualMachineDao.getInstance().findById(Integer.parseInt(vmId));

		// 3) send requisition for host
		// TODO create connection pool before :)

		markAsCompleted();
	}

}
