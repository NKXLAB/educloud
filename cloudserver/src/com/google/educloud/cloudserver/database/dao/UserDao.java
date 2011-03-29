package com.google.educloud.cloudserver.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.entity.User;
import com.google.educloud.cloudserver.entity.User.UserType;

public class UserDao extends AbstractDao {

	private static Logger LOG = Logger.getLogger(UserDao.class);

	private static UserDao dao;

	private UserDao() {
	}

	public static UserDao getInstance() {
		if (null == dao) {
			dao = new UserDao();
		}

		return dao;
	}

	public User findByLogin(String login) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement(
					"SELECT * FROM CLOUD_USER WHERE LOGIN = ?");
			ps.setString(1, login);
			rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setLogin(rs.getString("login"));
				user.setName(rs.getString("name"));
				user.setType(UserType.valueOf(rs.getString("type")));
				user.setPass(rs.getString("password"));
				return user;
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}

		return null;
	}

	public List<User> getAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<User> users = new ArrayList<User>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM CLOUD_USER");
			rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("user_id"));
				user.setLogin(rs.getString("login"));
				user.setName(rs.getString("name"));
				user.setType(UserType.valueOf(rs.getString("type")));
				users.add(user);
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}

		return users;
	}

	public void insert(User user) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("values next value for seq_user_id");
			rs = ps.executeQuery();
			int key;
			if (rs.next()) {
				key = rs.getInt(1);

				ps = getConnection().prepareStatement("INSERT INTO CLOUD_USER (USER_ID, NAME, LOGIN, PASSWORD, TYPE) VALUES (?, ?, ?, ?, ?)");
				ps.setInt(1, key);
				ps.setString(2, user.getName());
				ps.setString(3, user.getLogin());
				ps.setString(4, user.getPass());
				ps.setString(5, user.getType().name());

				ps.execute();

				user.setId(key);

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

	public void remove(Integer id) {
		PreparedStatement ps = null;

		try {
				ps = getConnection().prepareStatement("DELETE FROM CLOUD_USER WHERE USER_ID=?");
				ps.setInt(1, id);
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

}
