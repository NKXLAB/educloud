package com.google.educloud.api.clients;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class EduCloudStopVMClientTest {

	private EduCloudVMClient vmClient;
	private String name;

	@Before
	public void setUp() throws Exception {
		// setup client configuration
		EduCloudConfig config = new EduCloudConfig();
		config.setHost("localhost");
		config.setPort(8000);

		name = "ubuntu-machine";

		vmClient = EduCloudFactory.createVMClient(config);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStopVM() {
		// setup new virtual machine
		Template template = new Template();
		template.setName("lamp-server");
		template.setOsType("Ubuntu");
		template.setSize(8589934592L);

		VirtualMachine machine = new VirtualMachine();
		machine.setTemplate(template);
		machine.setName(name);
		machine.setId(1);

		// stop machine
		try {
			vmClient.stopVM(machine);
		} catch (EduCloudServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
