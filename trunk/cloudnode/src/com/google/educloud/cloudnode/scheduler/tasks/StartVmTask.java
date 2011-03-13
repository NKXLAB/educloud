package com.google.educloud.cloudnode.scheduler.tasks;

import com.google.educloud.internal.entities.VirtualMachine;

public class StartVmTask extends AbstractTask {

	private VirtualMachine vm;

	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		// 1) clone template disk
		// 2) create a virtual machine
		// 3) attach new cloned disk to new virtual machine
		// 4) start virtual machine
		// 5) notify server that machine was started
	}

}
