package com.google.educloud.cloudserver.managers;

import com.google.educloud.cloudserver.database.dao.TaskDao;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.scheduler.tasks.AbstractTask;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask.Status;
import com.google.educloud.cloudserver.scheduler.tasks.StartVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.StopVMTask;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class VMManager {

	public VirtualMachine scheduleStartVM(VirtualMachine vm) {

		vm = VirtualMachineDao.getInstance().findById(vm.getId());
		vm.setState(VMState.PENDING);

		AbstractTask startVmTask = new StartVmTask();
		startVmTask.setStatus(Status.PENDING);
		startVmTask.setParameter(StartVmTask.VM_ID, String.valueOf(vm.getId()));

		VirtualMachineDao.getInstance().changeState(vm);

		TaskDao.getInstance().insert(startVmTask);

		return vm;
	}

	public VirtualMachine scheduleStopVM(VirtualMachine vm) {

		vm = VirtualMachineDao.getInstance().findById(vm.getId());

		vm.setState(VMState.SHUTDOWN);

		AbstractTask stopVmTask = new StopVMTask();
		stopVmTask.setStatus(Status.PENDING);
		stopVmTask.setParameter(StopVMTask.VM_ID, String.valueOf(vm.getId()));

		VirtualMachineDao.getInstance().changeState(vm);

		TaskDao.getInstance().insert(stopVmTask);

		return vm;
	}
}