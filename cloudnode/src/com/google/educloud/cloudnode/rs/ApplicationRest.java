package com.google.educloud.cloudnode.rs;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;
import org.virtualbox.service.IHost;
import org.virtualbox.service.IVirtualBox;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.virtualbox.VirtualBoxConnector;
import com.google.educloud.internal.entities.MachineResourcesInfo;
import com.google.educloud.internal.entities.Node;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/application")
public class ApplicationRest {

	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(ApplicationRest.class);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/check")
	public Response getStatus(String jsonNode) {

		LOG.debug("Returning application status");

		Node node = gson.fromJson(jsonNode, Node.class);

		try {
			IVirtualBox vbox = VirtualBoxConnector.connect(NodeConfig.getVirtualBoxWebservicesUrl());
			String version = vbox.getVersion();
			
			IHost host = vbox.getHost();
			MachineResourcesInfo mri = new MachineResourcesInfo();
			mri.setTotalMemory(host.getMemorySize());
			mri.setAvailableMemory(host.getMemoryAvailable());
						
			node.setMachinesReourcesInfo(mri);			
			node.setVboxVersion(version);
			node.setConnectedToVBox(true);
			vbox.release();
		} catch (WebServiceException e) {
			LOG.error("Error on connect on vbox services", e);
			node.setVboxVersion(null);
			node.setConnectedToVBox(false);
		} catch (Error e) {
			LOG.error("Error on connect on vbox services", e);
			node.setVboxVersion(null);
			node.setConnectedToVBox(false);
		}

		return Response.ok(gson.toJson(node), MediaType.APPLICATION_JSON).build();
	}

}
