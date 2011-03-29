package com.google.educloud.cloudserver.rs;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.gson.Gson;

@Provider
public class UnexpectedCloudError implements ExceptionMapper<Throwable> {

	private static Logger LOG = Logger.getLogger(UnexpectedCloudError.class);

	public Response toResponse(Throwable ex) {

		LOG.error("A unexpected error occurs", ex);

		EduCloudErrorMessage error = new EduCloudErrorMessage();
		error.setCode("CS-600");
		error.setHint("\\o/");
		error.setText("Sorry about this, I know it's a bit silly. :)");

		return Response.status(500).entity(new Gson().toJson(error)).type(MediaType.APPLICATION_JSON)
				.build();
	}

}
