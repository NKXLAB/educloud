package com.google.educloud.api;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.educloud.api.clients.AbstractClient;

public class EduCloudFactoryTest {

	private EduCloudConfig config;

	@Before
	public void setUp() throws Exception {
		// setup client configuration
		config = new EduCloudConfig();
		config.setHost("localhost");
		config.setPort(8000);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateVMClient() {
		// create vm client
		AbstractClient vmClient = EduCloudFactory.createVMClient(config);
		assertNotNull(vmClient);
	}

}