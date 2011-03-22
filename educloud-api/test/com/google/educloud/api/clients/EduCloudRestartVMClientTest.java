package com.google.educloud.api.clients;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class EduCloudRestartVMClientTest {

	private EduCloudVMClient vmClient;

	@Before
	public void setUp() throws Exception {
		// setup client configuration
		EduCloudConfig config = new EduCloudConfig();
		config.setHost("localhost");
		config.setPort(8000);
		config.setLogin("admin");
		config.setPass("123");

		EduCloudAuthorization auth = EduCloudFactory.createAuthorization(config);
		vmClient = EduCloudFactory.createVMClient(auth);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartVM() throws EduCloudServerException {
		// setup new virtual machine
		Template template = new Template();
		template.setName("lamp-server");
		template.setOsType("Ubuntu");
		template.setId(1);
		template.setSize(8589934592L);

		VirtualMachine machine = new VirtualMachine();
		machine.setTemplate(template);
		machine.setName("ubuntu-machine");

		// start machine
		VirtualMachine vm = vmClient.startVM(machine);

		System.out.println(vm.getId());
		System.out.println(vm.getName());
		System.out.println(vm.getState());
	}

}
