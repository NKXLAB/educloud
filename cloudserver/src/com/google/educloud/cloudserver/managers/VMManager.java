package com.google.educloud.cloudserver.managers;

import com.google.educloud.cloudserver.database.dao.TaskDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.scheduler.tasks.AbstractTask;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask.Status;
import com.google.educloud.cloudserver.scheduler.tasks.StartVmTask;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class VMManager {

	public void scheduleNewVM(VirtualMachine vm) {

		vm.setState(VMState.PENDING);

		VirtualMachineDao.getInstance().insert(vm);

		AbstractTask startVmTask = new StartVmTask();
		startVmTask.setStatus(Status.PENDING);
		startVmTask.setParameter(StartVmTask.VM_ID, String.valueOf(vm.getId()));

		TaskDao.getInstance().insert(startVmTask);
	}

}
