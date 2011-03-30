package com.google.educloud.cloudserver.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class VirtualMachineLogDao extends AbstractDao {

	private static Logger LOG = Logger.getLogger(VirtualMachineLogDao.class);

	private static VirtualMachineLogDao dao;

	private VirtualMachineLogDao() {
	}

	public static VirtualMachineLogDao getInstance() {
		if (null == dao) {
			dao = new VirtualMachineLogDao();
		}

		return dao;
	}

	public void insert(int vmId, String message) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement(
					"values next value for seq_machine_log_id");
			rs = ps.executeQuery();
			int key;
			if (rs.next()) {
				key = rs.getInt(1);

				ps = getConnection()
						.prepareStatement(
								"INSERT INTO MACHINE_LOG (MACHINE_LOG_ID, MACHINE_ID, MESSAGE, LOG_TIME) VALUES (?, ?, ?, CURRENT_TIMESTAMP)");
				ps.setInt(1, key);
				ps.setInt(2, vmId);
				ps.setString(3, message);
				ps.execute();

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
}
