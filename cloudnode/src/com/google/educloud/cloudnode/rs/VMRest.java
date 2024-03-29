package com.google.educloud.cloudnode.rs;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.cloudnode.scheduler.Scheduler;
import com.google.educloud.cloudnode.scheduler.tasks.CreateVmTask;
import com.google.educloud.cloudnode.scheduler.tasks.RemoveVmTask;
import com.google.educloud.cloudnode.scheduler.tasks.StartVmTask;
import com.google.educloud.cloudnode.scheduler.tasks.StopVmTask;
import com.google.educloud.internal.entities.Template;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.to.NewVirtualMachineTO;
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
	@Path("/create")
	public Response createVM(String machine) {

		LOG.debug("Application will start a new VM");
		LOG.debug(machine);

		NewVirtualMachineTO vmTO = gson.fromJson(machine, NewVirtualMachineTO.class);

		VirtualMachine vm = vmTO.getVirtualMachine();
		Template template = vmTO.getTemplate();

		/* setup task */
		CreateVmTask createVmTask = new CreateVmTask();
		createVmTask.setVirtualMachine(vm);
		createVmTask.setTemplate(template);

		/* add task to scheduler */
		Scheduler.getInstance().addTask(createVmTask);

		// return a new created virtual machine
		return Response.ok(gson.toJson(""), MediaType.APPLICATION_JSON).build();
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

		VirtualMachine vm = gson.fromJson(machine, VirtualMachine.class);

		/* setup task */
		StartVmTask startVmTask = new StartVmTask();
		startVmTask.setVirtualMachine(vm);

		/* add task to scheduler */
		Scheduler.getInstance().addTask(startVmTask);

		// return a new created virtual machine
		return Response.ok(gson.toJson(""), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will schedule a task to stop a virtual machine.
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

		VirtualMachine vm = gson.fromJson(machine, VirtualMachine.class);

		/* setup task */
		StopVmTask stopVmTask = new StopVmTask();
		stopVmTask.setVirtualMachine(vm);

		/* add task to scheduler */
		Scheduler.getInstance().addTask(stopVmTask);

		// return virtual machine
		return Response.ok(gson.toJson(vm), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will schedule a task to remove a virtual machine.
	 *
	 * @param machine
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/remove")
	public Response removeVM(String machine) {

		LOG.debug("Application will remove a VM");
		LOG.debug(machine);

		VirtualMachine vm = gson.fromJson(machine, VirtualMachine.class);

		/* setup task */
		RemoveVmTask removeVmTask = new RemoveVmTask();
		removeVmTask.setVirtualMachine(vm);

		/* add task to scheduler */
		Scheduler.getInstance().addTask(removeVmTask);

		// return virtual machine
		return Response.ok(gson.toJson(vm), MediaType.APPLICATION_JSON).build();
	}

}
