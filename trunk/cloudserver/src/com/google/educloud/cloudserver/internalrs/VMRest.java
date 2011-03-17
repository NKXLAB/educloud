package com.google.educloud.cloudserver.internalrs;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.VirtualMachineDao;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/vm")
public class VMRest {

	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(VMRest.class);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/changeState")
	public Response getChangeState(String jsonMachine) {

		LOG.debug("Returning application status");

		VirtualMachine machine = gson.fromJson(jsonMachine, VirtualMachine.class);

		VirtualMachineDao.getInstance().changeState(machine);

		return Response.ok(gson.toJson(machine), MediaType.APPLICATION_JSON).build();
	}

}
