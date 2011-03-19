package com.google.educloud.api.clients;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.api.EduCloudFactory;
import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.Node;
import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;

public class EduCloudVMClientTest {

	private EduCloudVMClient vmClient;
	private EduCloudTemplateClient templateClient;
	private EduCloudNodeClient nodeClient;

	@Before
	public void setUp() throws Exception {
		// setup client configuration
		EduCloudConfig config = new EduCloudConfig();
		config.setHost("localhost");
		config.setPort(8000);

		vmClient = EduCloudFactory.createVMClient(config);
		templateClient = EduCloudFactory.createTemplateClient(config);
		nodeClient = EduCloudFactory.createNodeClient(config);
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
		machine.setName("ubuntu-machine-2");

		// start machine
		VirtualMachine vm = vmClient.startVM(machine);

		System.out.println(vm.getId());
		System.out.println(vm.getName());
		System.out.println(vm.getState());
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
	
	@Test
	public void testDescribeTemplates() throws EduCloudServerException {

		List<Template> listaTemplates = templateClient.describeTemplates();

		System.out.println("Listagem dos templates");

		for( Template template : listaTemplates ){

			System.out.println(template.getId());
			System.out.println(template.getName());
		}
	}
	
	@Test	
	public void testDescribeNodes() throws EduCloudServerException {

		List<Node> listaNodos = nodeClient.decribeNodes();

		System.out.println("Listagem dos nodos");

		for( Node nodo : listaNodos ){

			System.out.println(nodo.getId());
			System.out.println(nodo.getIp());
			System.out.println(nodo.getPort());
		}
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
		machine.setName("ubuntu-machine-2");
		machine.setId(1);

		// stop machine
		try {
			vmClient.stopVM(machine);
		} catch (EduCloudServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testStartVMValidationError() {
		// setup new virtual machine
		Template template = new Template();
		template.setName("lamp-server");
		template.setOsType("Ubuntu");
		template.setSize(8589934592L);

		VirtualMachine machine = new VirtualMachine();
		machine.setTemplate(template);
		machine.setName("ubuntu-machine");

		machine.setId(77777); // introduce error

		try {
			// try start machine
			vmClient.startVM(machine);
		} catch (EduCloudServerException e) {
			assertEquals("Apparently you are trying to start an existing virtual machine", e.getMessage());
			EduCloudErrorMessage errorMessage = e.getErrorMessage();

			assertEquals("CS-001", errorMessage.getCode());
			assertEquals("Set virtual machine id to zero and try again", errorMessage.getHint());
			assertEquals("Apparently you are trying to start an existing virtual machine", errorMessage.getText());
		}
	}

}
