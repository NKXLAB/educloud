package com.google.educloud.cloudserver.rs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.VirtualMachine.VMState;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.managers.VMManager;
import com.google.educloud.internal.entities.Template;
import com.sun.jersey.spi.container.servlet.PerSession;

@PerSession
@Path("/vm")
public class VMRest extends CloudResource {

	private static Logger LOG = Logger.getLogger(VMRest.class);
	
	/**
	 * this method will create a virtual machine.
	 *
	 * @param machine
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create")
	public Response createVM(String machine) {

		LOG.debug("Application will create a new VM");
		LOG.debug(machine);

		VirtualMachine externalMachine = gson.fromJson(machine, VirtualMachine.class);

		/* create internal entity (Virtual Machine) based on received */
		com.google.educloud.internal.entities.VirtualMachine vm = 
			new com.google.educloud.internal.entities.VirtualMachine();
		
		/* create internal entity (Template) based on received */
		com.google.educloud.internal.entities.Template tpt =
			new com.google.educloud.internal.entities.Template();		
		tpt.setId(externalMachine.getTemplate().getId());
		
		vm.setTemplate(tpt);
		vm.setName(externalMachine.getName());
		vm.setState(com.google.educloud.internal.entities.VirtualMachine.VMState.DONE);

		/* vm start logic */
		VMManager vmManager = new VMManager();
		vmManager.CreateVM(vm);

		/* update external machine to return for client */
		externalMachine.setId(vm.getId());
		externalMachine.setState(VMState.valueOf(vm.getState().name()));

		// return a new created virtual machine
		return Response.ok(gson.toJson(externalMachine), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will schedule a new allocation of a virtual machine
	 *
	 * @param machine
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/start")
	public Response startVM(String machine) {

		LOG.debug("Application will start a new VM");
		LOG.debug(machine);

		VirtualMachine externalMachine = gson.fromJson(machine, VirtualMachine.class);

		int id = externalMachine.getId();

		/* validations */
		if (id == 0) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-001");
			error.setHint("Set virtual machine id and try again");
			error.setText("Apparently you are trying to start a nonexistent virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		/* create internal entity (Virtual Machine) based on received */
		com.google.educloud.internal.entities.VirtualMachine vm = new com.google.educloud.internal.entities.VirtualMachine();
		vm.setId(externalMachine.getId());

		/* vm start logic */
		VMManager vmManager = new VMManager();
		vm = vmManager.scheduleStartVM(vm);

		/* update external machine to return for client */
		externalMachine.setId(vm.getId());
		externalMachine.setState(VMState.valueOf(vm.getState().name()));

		// return a new created virtual machine
		return Response.ok(gson.toJson(externalMachine), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will schedule a new allocation of a virtual machine
	 *
	 * @param machine
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/stop")
	public Response stopVM(String machine) {

		LOG.debug("Application will stop a VM");
		LOG.debug(machine);

		VirtualMachine externalMachine = gson.fromJson(machine, VirtualMachine.class);

		int id = externalMachine.getId();

		/* validations */
		if (id == 0) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-003");
			error.setHint("Set virtual machine id and try again");
			error.setText("Apparently you are trying to stop a nonexistent virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		com.google.educloud.internal.entities.VirtualMachine vm = new com.google.educloud.internal.entities.VirtualMachine();
		vm.setId(externalMachine.getId());

		/* vm start logic */
		VMManager vmManager = new VMManager();
		vm = vmManager.scheduleStopVM(vm);
		externalMachine.setState(VMState.valueOf(vm.getState().name()));

		// return a new created virtual machine
		return Response.ok(gson.toJson(externalMachine), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will retrive all started vms
	 *
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/describeInstances")
	public Response describeInstances() {

		LOG.debug("Application will list all machines");

		//Recupera a lista de máquinas virtuais da base de dados.
		List<com.google.educloud.internal.entities.VirtualMachine> listaVirtualMachines =
			VirtualMachineDao.getInstance().getAll();

		//Array para retorno
		VirtualMachine[] virtualMachines = new VirtualMachine[listaVirtualMachines.size()];

		//Para controle do indice do array
		int indice = 0;

		//Coloca a lista interna no array de máquinas externas
		for( com.google.educloud.internal.entities.VirtualMachine vmInterna : listaVirtualMachines )
		{
			VirtualMachine vmRetorno = new VirtualMachine();
			vmRetorno.setId(vmInterna.getId());
			vmRetorno.setName(vmInterna.getName());
			vmRetorno.setState(VMState.valueOf(vmInterna.getState().name()));
			Template template = vmInterna.getTemplate();
			com.google.educloud.api.entities.Template templateRetorno = new com.google.educloud.api.entities.Template();
			templateRetorno.setId(template.getId());
			templateRetorno.setName(template.getName());
			templateRetorno.setOsType(template.getOsType());
			vmRetorno.setTemplate(templateRetorno);
			virtualMachines[indice] = vmRetorno;

			indice++;
		}

		//Retorna o array de máquinas virtuais.
		return Response.ok(gson.toJson(virtualMachines), MediaType.APPLICATION_JSON).build();
	}
}
