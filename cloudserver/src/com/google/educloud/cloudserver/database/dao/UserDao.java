package com.google.educloud.cloudserver.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

}
