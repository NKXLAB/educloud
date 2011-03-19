package com.google.educloud.cloudnode.rs;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

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

		return Response.ok(gson.toJson(node), MediaType.APPLICATION_JSON).build();
	}

}
