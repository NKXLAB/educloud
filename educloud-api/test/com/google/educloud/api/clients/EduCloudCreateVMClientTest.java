package com.google.educloud.api.clients;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;


public class EduCloudCreateVMClientTest {

	private EduCloudVMClient vmClient;

	@Before
	public void setUp() throws Exception {
		// setup client configuration
		EduCloudConfig config = new EduCloudConfig();
		config.setHost("localhost");
		config.setPort(8000);
		config.setLogin("admin");
		config.setPass("admin");

		EduCloudAuthorization auth = EduCloudFactory.createAuthorization(config);
		vmClient = EduCloudFactory.createVMClient(auth);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateVM() throws EduCloudServerException {
		// setup new virtual machine
		Template template = new Template();
		template.setName("lamp-server");
		template.setOsType("Ubuntu");
		template.setId(1);

		VirtualMachine machine = new VirtualMachine();
		machine.setTemplate(template);
		machine.setName("ubuntu-machine");

		// create machine
		VirtualMachine vmCriada = vmClient.createVM(machine);

		//Verifica apenas se criou um id e se o nome é o mesmo.
		Assert.assertTrue(vmCriada.getId() > 0 &&
				vmCriada.getName().equals(machine.getName()));
	}

}
