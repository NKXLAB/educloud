package com.google.educloud.cloudserver.rs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.TemplateDao;
import com.google.educloud.internal.entities.Template;
import com.google.gson.Gson;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/template")
public class TemplateRest {
	
	private static Gson gson = new Gson();

	private static Logger LOG = Logger.getLogger(TemplateRest.class);
	
	/**
	 * this method will retrive all templates
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

		//Coloca a lista interna no array de máquinas externas
		for( com.google.educloud.internal.entities.Template templateInterno : listaTemplates )
		{
			Template templateRetorno = new Template();
			templateRetorno.setId(templateInterno.getId());
			templateRetorno.setName(templateInterno.getName());
			templateRetorno.setFilename(templateInterno.getFilename());
			templateRetorno.setOsType(templateInterno.getOsType());
			templateRetorno.setSize(templateInterno.getSize());
			templateRetorno.setUUID(templateInterno.getUUID());
			templates[indice] = templateRetorno;

			indice++;
		}

		//Retorna o array de templates.
		return Response.ok(gson.toJson(templates), MediaType.APPLICATION_JSON).build();	
	}
}
