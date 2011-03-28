package com.google.educloud.api.clients;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.educloud.api.EduCloudAuthorization;
import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class EduCloudDescribeInstancesVMClientTest{ 

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
	public void testDescribeInstances() throws EduCloudServerException {

        List<VirtualMachine> listaMaquinasVirtuais = vmClient.describeInstances();

        System.out.println("Listagem das máquinas virtuais");

        for( VirtualMachine vm : listaMaquinasVirtuais ){

                System.out.println(vm.getId());
                System.out.println(vm.getName());
        }
	}
}