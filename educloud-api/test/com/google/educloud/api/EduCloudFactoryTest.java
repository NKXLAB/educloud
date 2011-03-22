package com.google.educloud.api;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.educloud.api.clients.AbstractClient;
import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class EduCloudFactoryTest {

	private EduCloudConfig config;

	@Before
	public void setUp() throws Exception {
		// setup client configuration
		config = new EduCloudConfig();
		config.setHost("localhost");
		config.setPort(8000);
		config.setLogin("admin");
		config.setPass("admin");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateVMClient() {
		// create vm client
		EduCloudAuthorization auth = null;
		try {
			auth = EduCloudFactory.createAuthorization(config);
		} catch (EduCloudServerException e) {
			EduCloudErrorMessage errorMessage = e.getErrorMessage();
			System.out.println(errorMessage.getText());
			System.out.println(errorMessage.getHint());
			return;
		}

		AbstractClient vmClient = EduCloudFactory.createVMClient(auth);
		assertNotNull(vmClient);
	}

}
