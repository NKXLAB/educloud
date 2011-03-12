package com.google.educloud.cloudserver.rs;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.VirtualMachine;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/vm")
public class VMRest {

	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(VMRest.class);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/start")
	public Response startVM(String machine) {

		LOG.debug("Application will start a new VM");
		LOG.debug(machine);

		VirtualMachine externalMachine = gson.fromJson(machine, VirtualMachine.class);

		int id = externalMachine.getId();

		/* validations */
		if (id != 0) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-001");
			error.setHint("Set virtual machine id to zero and try again");
			error.setText("Apparently you are trying to start an existing virtual machine");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		/* vm start logic */
		com.google.educloud.internal.entities.VirtualMachine vm = new com.google.educloud.internal.entities.VirtualMachine();
		vm.setTemplateId(externalMachine.getTemplateId());
		vm.setName(externalMachine.getName());
		vm.setState(com.google.educloud.internal.entities.VirtualMachine.VMState.STARTED);
		vm.setNodeId(987);
		vm.setUserId(567);
		vm.setId(98);

		externalMachine.setId(vm.getId());

		// return a new created virtual machine
		return Response.ok(gson.toJson(externalMachine), MediaType.APPLICATION_JSON).build();
	}

}
