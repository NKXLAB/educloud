package com.google.educloud.api.clients;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class EduCloudStopVMClientTest {

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
	public void testStopVM() {
		VirtualMachine machine = new VirtualMachine();
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
