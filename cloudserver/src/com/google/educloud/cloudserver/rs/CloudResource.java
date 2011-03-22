package com.google.educloud.cloudserver.rs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.google.gson.Gson;

public class CloudResource {

	protected Gson gson = new Gson();

	@Context HttpServletRequest request;

}
