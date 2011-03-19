package com.google.educloud.cloudserver.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.educloud.internal.entities.Template;

public class TemplateDao extends AbstractDao {

	private static Logger LOG = Logger.getLogger(TemplateDao.class);

	private static TemplateDao dao;

	private TemplateDao() {
	}

	public static TemplateDao getInstance() {
		if (null == dao) {
			dao = new TemplateDao();
		}

		return dao;
	}

	public Template findById(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("SELECT * FROM TEMPLATE WHERE TEMPLATE_ID = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Template tpl = new Template();
				tpl.setId(rs.getInt("template_id"));
				tpl.setFilename(rs.getString("filename"));
				tpl.setName(rs.getString("name"));
				tpl.setOsType(rs.getString("os_type"));
				tpl.setSize(rs.getLong("size"));
				return tpl;
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}

		return null;
	}

	public List<Template> getAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Template> templates = new ArrayList<Template>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM TEMPLATE");
			rs = ps.executeQuery();
			if (rs.next()) {
				Template tpl = new Template();
				tpl.setId(rs.getInt("template_id"));
				tpl.setFilename(rs.getString("filename"));
				tpl.setName(rs.getString("name"));
				tpl.setOsType(rs.getString("os_type"));
				tpl.setSize(rs.getLong("size"));
				templates.add(tpl);
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}

		return templates;
	}

}
