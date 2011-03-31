package com.google.educloud.api.to;

import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.VirtualMachine;

public class NewVirtualMachineTO {

	private VirtualMachine virtualMachine;

	private Template template;

	public VirtualMachine getVirtualMachine() {
		return virtualMachine;
	}

	public void setVirtualMachine(VirtualMachine virtualMachine) {
		this.virtualMachine = virtualMachine;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

}
