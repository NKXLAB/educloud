package com.google.educloud.cloudserver.rs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.cloudserver.database.dao.TemplateDao;
import com.google.educloud.cloudserver.entity.CloudSession;
import com.google.educloud.internal.entities.Template;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.spi.container.servlet.PerSession;

@PerSession
@Path("/template")
public class TemplateRest extends CloudResource {

	private static Logger LOG = Logger.getLogger(TemplateRest.class);

	/**
	 * this method will retrieve all templates
	 *
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/describeTemplates")
	public Response describeTemplates() {

		LOG.debug("Application will list all templates");

		//Recupera a lista de templates da base de dados.
		List<com.google.educloud.internal.entities.Template> listaTemplates =
			TemplateDao.getInstance().getAll();

		//Array para retorno
		Template[] templates = new Template[listaTemplates.size()];

		//Para controle do indice do array
		int indice = 0;

		//Coloca a lista interna no array de maquinas externas
		for( com.google.educloud.internal.entities.Template templateInterno : listaTemplates )
		{
			Template templateRetorno = new Template();
			templateRetorno.setId(templateInterno.getId());
			templateRetorno.setName(templateInterno.getName());
			templateRetorno.setFilename(templateInterno.getFilename());
			templateRetorno.setOsType(templateInterno.getOsType());
			templates[indice] = templateRetorno;

			indice++;
		}

		//Retorna o array de templates.
		return Response.ok(gson.toJson(templates), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will create a new template
	 *
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create")
	public Response createTemplate(String jsonTemplate) {

		com.google.educloud.api.entities.Template extTemplate = gson.fromJson(jsonTemplate, com.google.educloud.api.entities.Template.class);

		Template template = new Template();
		template.setFilename(extTemplate.getFilename());
		template.setName(extTemplate.getName());
		template.setOsType(extTemplate.getOsType());

		TemplateDao.getInstance().insert(template);

		return Response.ok(gson.toJson(template), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will return a cloud template.
	 *
	 * @param tplId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/{id: [0-9]*}")
	public Response getVM(@PathParam("id") int tplId) {

		int id = Integer.valueOf(tplId);

		Template loadedTemplate = TemplateDao.getInstance().findById(id);

		HttpSession session = request.getSession();
		CloudSession cloudSession = (CloudSession)session.getAttribute(CloudSession.HTTP_ATTR_NAME);

		/* validations */
		if (loadedTemplate == null) {
			EduCloudErrorMessage error = new EduCloudErrorMessage();
			error.setCode("CS-303");
			error.setHint("Set template id and try again");
			error.setText("Apparently you are trying to get a nonexistent template");

			// return error message
			return Response.status(400).entity(gson.toJson(error)).build();
		}

		com.google.educloud.api.entities.Template template = new com.google.educloud.api.entities.Template();
		template.setId(loadedTemplate.getId());
		template.setName(loadedTemplate.getName());
		template.setOsType(loadedTemplate.getOsType());

		// return template
		return Response.ok(gson.toJson(template), MediaType.APPLICATION_JSON).build();
	}

	/**
	 * this method will delete a template
	 *
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public Response deleteTemplates(String jsonTemplates) {

		Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
		List<Integer> templates = gson.fromJson(jsonTemplates, type);

		for (Integer tplId : templates) {
			TemplateDao.getInstance().remove(tplId);
		}

		return Response.ok().build();
	}

}
