package com.google.educloud.cloudserver.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.educloud.internal.entities.Template;

//Classe para persistir o objeto template
public class TemplateDao {
	
	private static int currentId = 0;

	private static List<Template> templates = new ArrayList<Template>();

	private static TemplateDao dao;

	public static TemplateDao getInstance() {
		// TODO Auto-generated method stub
		if (null == dao) {
			dao = new TemplateDao();
			
			//Adiciona um template hardcoded
			Template template = new Template();
			template.setName("lamp-server");
			template.setOsType("Ubuntu");
			template.setId(1);
			template.setSize(8589934592L);
			
			dao.insert(template);
		}

		return dao;
	}
	
	public void insert(Template template) {
		template.setId(++currentId);		
		templates.add(template);
	}

	public Template findTemplateById(int templateId){		
		Template toReturn = null;		
		for( Template t : templates ){
			if( t.getId() == templateId )
			{
				toReturn = t;
				break;
			}
		}
		return toReturn;
	}

	public List<Template> getAll() {
		// TODO Auto-generated method stub
		return templates;
	}
}
