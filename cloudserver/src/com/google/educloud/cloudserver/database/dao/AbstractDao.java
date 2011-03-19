package com.google.educloud.cloudserver.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mortbay.log.Log;

import com.google.educloud.cloudserver.database.ConnectionHelper;

public class AbstractDao {

	private Connection connection;

	protected Connection getConnection() {
		if (null == connection) {
			connection = ConnectionHelper.getConnection();
		}

		return connection;
	}

	protected void cleanUp(PreparedStatement ps, ResultSet rs) {
		try {
			if (null != ps) ps.close();
			if (null != rs) rs.close();
		} catch (SQLException e) {
			Log.debug(e);
		}
	}

}
