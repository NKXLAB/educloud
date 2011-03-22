package com.google.educloud.api.clients;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.google.educloud.api.entities.Template;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class EduCloudTemplateClient extends AbstractClient {

	//Recupera todos os templates registrados.
	public List<Template> describeTemplates() throws EduCloudServerException{
		WebResource service = getWebResouce();

		ClientResponse response = service.path("template").path("describeTemplates").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		//Recupera o array de retorno.
		Template[] templates =
			gson.fromJson(entity, Template[].class);

		//Gera a lista de retorno.
		List<Template> listaTemplates =
			new ArrayList<Template>();

		for( Template vm : templates )
		{
			listaTemplates.add(vm);
		}

		return listaTemplates;
	}

}
