package com.google.educloud.cloudserver.internalrs;

import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.internal.entities.Template;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/template")
public class TemplateRest {

	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(TemplateRest.class);

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/publish")
	public Response getStatus(String jsonTemplates) {

		Type collectionType = new TypeToken<Collection<Template>>(){}.getType();
		Collection<Template> templates = gson.fromJson(jsonTemplates, collectionType);

		LOG.debug("Receive new templates");
		LOG.debug(templates);

		String json = gson.toJson(templates);

		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}

}
