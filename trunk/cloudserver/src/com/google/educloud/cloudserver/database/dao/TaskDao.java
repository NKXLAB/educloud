package com.google.educloud.cloudserver.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.scheduler.tasks.AbstractTask;
import com.google.educloud.cloudserver.scheduler.tasks.CheckNodeTask;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask.Status;
import com.google.educloud.cloudserver.scheduler.tasks.CreateVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.RemoveVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.StartVmTask;
import com.google.educloud.cloudserver.scheduler.tasks.StopVMTask;

public class TaskDao extends AbstractDao {

	private static Logger LOG = Logger.getLogger(TaskDao.class);

	private static TaskDao dao;

	private TaskDao() {
	}

	public static TaskDao getInstance() {
		if (null == dao) {
			dao = new TaskDao();
		}

		return dao;
	}

	public List<CloudTask> findPendingTasks() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CloudTask> list = new ArrayList<CloudTask>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM TASK WHERE STATUS = 'PENDING' AND TIMESTAMPDIFF(SQL_TSI_FRAC_SECOND, SCHEDULE_TIME, CURRENT_TIMESTAMP) >= 0");
			rs = ps.executeQuery();
			if (rs.next()) {
				String type = rs.getString("type");

				CloudTask task;

				if (type.equals("STARTVM")) {
					task = new StartVmTask();
				} else if (type.equals("STOPVM")) {
					task = new StopVMTask();
				} else if (type.equals("CHECKNODE")) {
					task = new CheckNodeTask();
				} else if(type.equals("REMOVEVM")){
					task = new RemoveVmTask();
				} else if(type.equals("CREATEVM")){
					task = new CreateVmTask();
				}
				else {
					LOG.error("Unsupported task type");
					throw new RuntimeException("Unsupported task type");
				}

				task.setId(rs.getInt("task_id"));
				task.setStatus(Status.valueOf(rs.getString("status")));
				task.setScheduleTime(rs.getTimestamp("schedule_time"));

				loadTaksParams(task);

				list.add(task);
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}

		return list;
	}

	private void loadTaksParams(CloudTask task) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("SELECT * FROM TASK_PARAM WHERE TASK_ID = ?");
			ps.setInt(1, task.getId());
			rs = ps.executeQuery();
			while (rs.next()) {
				task.setParameter(rs.getString("name"), rs.getString("value"));
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}
	}

	public void insert(CloudTask task) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("values next value for seq_task_id");
			rs = ps.executeQuery();
			int key;
			if (rs.next()) {
				key = rs.getInt(1);

				ps = getConnection().prepareStatement("INSERT INTO TASK (TASK_ID, STATUS, TYPE, SCHEDULE_TIME) VALUES (?, ?, ?, ?)");
				ps.setInt(1, key);
				ps.setString(2, task.getStatus().name());
				ps.setString(3, task.getType());
				ps.setTimestamp(4, new Timestamp(task.getScheduleTime().getTime()));
				ps.execute();

				task.setId(key);

				insertParams(task);

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

	private void insertParams(CloudTask task) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		HashMap<String, String> parameters = task.getParameters();
		try {
			ps = getConnection().prepareStatement("INSERT INTO TASK_PARAM (TASK_ID, NAME, VALUE) VALUES (?, ?, ?)");

			Set<String> keySet = parameters.keySet();

			for (String key : keySet) {
				String value = parameters.get(key);
				ps.setInt(1, task.getId());
				ps.setString(2, key);
				ps.setString(3, value);
				ps.execute();
			}
		} catch (SQLException e) {
			LOG.debug(e);
			throw e;
		} finally {
			cleanUp(ps, rs);
		}
	}

	public void updateStatus(CloudTask task) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("UPDATE TASK SET STATUS=? WHERE TASK_ID=?");
			ps.setString(1, task.getStatus().name());
			ps.setInt(2, task.getId());
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

	public void updateStatus(AbstractTask task , Date time) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("UPDATE TASK SET STATUS=?, SCHEDULE_TIME=? WHERE TASK_ID=?");
			ps.setString(1, task.getStatus().name());
			ps.setTimestamp(2, new Timestamp(time.getTime()));
			ps.setInt(3, task.getId());
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
