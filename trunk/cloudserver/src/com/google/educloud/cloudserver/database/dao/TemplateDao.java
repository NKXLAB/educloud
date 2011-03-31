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

	public void insert(Template template) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("values next value for seq_template_id");
			rs = ps.executeQuery();
			int key;
			if (rs.next()) {
				key = rs.getInt(1);

				ps = getConnection().prepareStatement("INSERT INTO TEMPLATE (TEMPLATE_ID, OS_TYPE, NAME, FILENAME, DESCRIPTION) VALUES (?, ?, ?, ?, ?)");
				ps.setInt(1, key);
				ps.setString(2, template.getOsType());
				ps.setString(3, template.getName());
				ps.setString(4, template.getFilename());
				ps.setString(5, template.getDescription());
				ps.execute();

				template.setId(key);

				getConnection().commit();
			}
		} catch (SQLException e) {
			LOG.error(e);
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				LOG.error(e1);
			}
		} finally {
			cleanUp(ps, rs);
		}
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
				return tpl;
			}
		} catch (SQLException e) {
			LOG.error(e);
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
			while (rs.next()) {
				Template tpl = new Template();
				tpl.setId(rs.getInt("template_id"));
				tpl.setFilename(rs.getString("filename"));
				tpl.setName(rs.getString("name"));
				tpl.setDescription(rs.getString("description"));
				tpl.setOsType(rs.getString("os_type"));
				templates.add(tpl);
			}
		} catch (SQLException e) {
			LOG.error(e);
		} finally {
			cleanUp(ps, rs);
		}

		return templates;
	}

	public void remove(int id) {
		PreparedStatement ps = null;

		try {
				ps = getConnection().prepareStatement("DELETE FROM TEMPLATE WHERE TEMPLATE_ID=?");
				ps.setInt(1, id);
				ps.execute();

				getConnection().commit();
		} catch (SQLException e) {
			LOG.error(e);
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				LOG.error(e1);
			}
		} finally {
			cleanUp(ps, null);
		}
	}
}
