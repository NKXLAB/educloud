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

	/**
	 * Return a cloud template
	 *
	 * @return
	 * @throws EduCloudServerException
	 */
	public Template getTemplate(int id) throws EduCloudServerException {
		WebResource service = getWebResouce();

		ClientResponse response = service.path("template").path("get").path(String.valueOf(id)).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		return gson.fromJson(entity, Template.class);
	}

	/**
	 * Create a new template in the cloud
	 */
	public Template createTemplate(Template template) throws EduCloudServerException {
		WebResource service = getWebResouce();

		String json = gson.toJson(template);

		ClientResponse response = service.path("template").path("create").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		return gson.fromJson(entity, Template.class);
	}

	public void delete(ArrayList<Integer> arrayList) throws EduCloudServerException {
		WebResource service = getWebResouce();

		String json = gson.toJson(arrayList);

		ClientResponse response = service.path("template").path("delete").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);
	}


}
