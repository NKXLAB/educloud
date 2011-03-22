package com.google.educloud.cloudserver.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.entity.CloudSession;

public class SessionDao extends AbstractDao {

	private static Logger LOG = Logger.getLogger(SessionDao.class);

	private static SessionDao dao;

	private SessionDao() {
	}

	public static SessionDao getInstance() {
		if (null == dao) {
			dao = new SessionDao();
		}

		return dao;
	}

	public void insert(CloudSession cloudSession) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement(
					"values next value for seq_session_id");
			rs = ps.executeQuery();
			int key;
			if (rs.next()) {
				key = rs.getInt(1);

				ps = getConnection()
						.prepareStatement(
								"INSERT INTO SESSION (SESSION_ID, USER_ID, CREATION_DATE, LAST_UPDATE) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
				ps.setInt(1, key);
				ps.setInt(2, cloudSession.getUser().getId());
				ps.execute();

				Date time = Calendar.getInstance().getTime();
				cloudSession.setCreationDate(time);
				cloudSession.setLastUpdate(time);
				cloudSession.setId(key);
				getConnection().commit();
			}
		} catch (SQLException e) {
			LOG.debug(e);
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				LOG.debug(e1);
			}
		} finally {
			cleanUp(ps, rs);
		}
	}

	public void remove(CloudSession cloudSession) {
		PreparedStatement ps = null;

		try {
			ps = getConnection().prepareStatement("DELETE FROM SESSION WHERE SESSION_ID=?");
			ps.setInt(1, cloudSession.getId());
			ps.execute();

			getConnection().commit();
		} catch (SQLException e) {
			LOG.debug(e);
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				LOG.debug(e1);
			}
		} finally {
			cleanUp(ps, null);
		}
	}

	public void updateLastUpdate(CloudSession cloudSession) {

		Date currentTime = Calendar.getInstance().getTime();
		cloudSession.setLastUpdate(currentTime);

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("UPDATE SESSION SET LAST_UPDATE=CURRENT_TIMESTAMP WHERE SESSION_ID=?");
			ps.setInt(1, cloudSession.getId());
			ps.execute();

			getConnection().commit();
		} catch (SQLException e) {
			LOG.debug(e);
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				LOG.debug(e1);
			}
		} finally {
			cleanUp(ps, rs);
		}
	}

}
