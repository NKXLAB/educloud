package com.google.educloud.cloudserver.rs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.educloud.api.entities.VirtualMachine.VMState;
import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.cloudserver.managers.VMManager;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/vm")
public class VMRest {

	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(VMRest.class);

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
		Template externalTemplate = externalMachine.getTemplate();

		/* validations */
		if (id != 0) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-001");
			error.setHint("Set virtual machine id to zero and try again");
			error.setText("Apparently you are trying to start an existing virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		if (null == externalTemplate) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-002");
			error.setHint("Inform a template machine and try again");
			error.setText("Apparently you are trying to start an without inform a template");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		/* create internal entity (Virtual Machine) based on received */
		com.google.educloud.internal.entities.Template template = new com.google.educloud.internal.entities.Template();
		template.setId(externalTemplate.getId());
		template.setName(externalTemplate.getName());
		template.setOsType(externalTemplate.getOsType());

		com.google.educloud.internal.entities.VirtualMachine vm = new com.google.educloud.internal.entities.VirtualMachine();
		vm.setTemplate(template);
		vm.setName(externalMachine.getName());

		/* vm start logic */
		VMManager vmManager = new VMManager();
		vmManager.scheduleNewVM(vm);

		/* update external machine to return for client */
		externalMachine.setId(vm.getId());
		externalMachine.setState(VMState.PENDING);

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
		Template externalTemplate = externalMachine.getTemplate();

		/* validations */
		if (id != 0) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-001");
			error.setHint("Set virtual machine id to zero and try again");
			error.setText("Apparently you are trying to start an existing virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		if (null == externalTemplate) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-002");
			error.setHint("Inform a template machine and try again");
			error.setText("Apparently you are trying to stop a VM without inform a template");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		/* create internal entity (Virtual Machine) based on received */
		com.google.educloud.internal.entities.Template template = new com.google.educloud.internal.entities.Template();
		template.setId(externalTemplate.getId());
		template.setName(externalTemplate.getName());
		template.setOsType(externalTemplate.getOsType());

		com.google.educloud.internal.entities.VirtualMachine vm = new com.google.educloud.internal.entities.VirtualMachine();
		vm.setTemplate(template);
		vm.setName(externalMachine.getName());

		/* vm start logic */
		VMManager vmManager = new VMManager();
		vmManager.scheduleStopVM(vm);

		/* update external machine to return for client */
		externalMachine.setId(vm.getId());
		externalMachine.setState(VMState.PENDING);

		// return a new created virtual machine
		return Response.ok(gson.toJson(externalMachine), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will retrive all started vms
	 *
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAll")
	public Response getAllVms() {

		LOG.debug("Application will list all machines");

		/* Recupera a lista de m�quinas virtuais */
		List<com.google.educloud.internal.entities.VirtualMachine> virtualMachines =
			VirtualMachineDao.getInstance().getAll();

		/* Lista para retorno */
		List<VirtualMachine> listaMaquinasRetorno = new ArrayList<VirtualMachine>();

		/* Transforma a lista interna em uma lista de entidades externas */
		for( com.google.educloud.internal.entities.VirtualMachine vmInterna : virtualMachines )
		{
			VirtualMachine vmRetorno = new VirtualMachine();
			vmRetorno.setId(vmInterna.getId());
			vmRetorno.setName(vmInterna.getName());
			// Ajustar as convers�es
			// vmRetorno.setState(vmInterna.getState());
			// vmRetorno.setTemplate(vmInterna.getTemplate());
		}

		// Ajustar para retornar a lista inteira.
		// Retorna a lista de m�quinas virtuais.
		return Response.ok(gson.toJson(listaMaquinasRetorno.get(0)), MediaType.APPLICATION_JSON).build();
	}
}
