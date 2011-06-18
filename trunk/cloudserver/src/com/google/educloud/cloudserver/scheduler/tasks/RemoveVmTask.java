package com.google.educloud.cloudserver.scheduler.tasks;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.nodecllient.ClientFactory;
import com.google.educloud.cloudserver.nodecllient.NodeComunicationException;
import com.google.educloud.cloudserver.nodecllient.VMNodeClient;
import com.google.educloud.cloudserver.selector.NodeSelectorManager;
import com.google.educloud.internal.entities.Node;
import com.google.educloud.internal.entities.VirtualMachine;

public class RemoveVmTask extends AbstractTask {

	public static final String VM_ID = "VM_ID";

	private static Logger LOG = Logger.getLogger(RemoveVmTask.class);

	@Override
	public void run() {

		markAsRunning();

		// 1) load virtual machine from database
		String vmId = getParameter(VM_ID);
		VirtualMachine vm = VirtualMachineDao.getInstance().findById(
				Integer.parseInt(vmId));


		Node node = NodeSelectorManager.getSelector().getNext(vm);

		if (null != node) {
			VMNodeClient nodeClient = ClientFactory.createVMNodeClient(node);
			try {
				nodeClient.removeVM(vm);
			} catch (NodeComunicationException e) {
				LOG.error("An error when remove virtual machine: #" + vm.getId(), e);
			}
		}

		VirtualMachineDao.getInstance().remove(vm);

		markAsCompleted();

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "REMOVEVM";
	}

}
