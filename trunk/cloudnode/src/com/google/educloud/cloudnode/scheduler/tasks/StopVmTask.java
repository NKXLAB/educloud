package com.google.educloud.cloudnode.scheduler.tasks;

import org.apache.log4j.Logger;

import com.google.educloud.cloudnode.rs.VMRest;
import com.google.educloud.internal.entities.VirtualMachine;

public class StopVmTask extends AbstractTask {
	
	private static Logger LOG = Logger.getLogger(VMRest.class);

	private VirtualMachine vm;

	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		LOG.debug("Running start virtual machine task");
		LOG.debug(vm);

		// 1) Remove template disk
		// 2) Remove a virtual machine
		// 3) notify server that machine was dropped
	}

}
