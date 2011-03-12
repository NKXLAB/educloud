package com.google.educloud.cloudserver.internalrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/application")
public class ApplicationRest {

	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(ApplicationRest.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/status")
	public Response getStatus() {

		LOG.debug("Returning application status");

		String json = gson.toJson(true);

		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}

}
