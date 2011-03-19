package com.google.educloud.cloudserver.scheduler.tasks;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.NodeDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.cloudserver.nodecllient.VMNodeClient;
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
		vm.setNodeId(node.getId());

		// 3) send requisition for host
		VMNodeClient nodeClient = ClientFactory.createVMNodeClient(node);
		try {
			nodeClient.startVM(vm);
		} catch (NodeComunicationException e) {
			LOG.error("An error when start virtual machine: #" + vm.getId(), e);
		}

		markAsCompleted();
	}

	@Override
	public String getType() {
		return "STARTVM";
	}

}
