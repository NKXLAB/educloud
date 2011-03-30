package com.google.educloud.cloudserver.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.educloud.internal.entities.Node;

public class NodeDao extends AbstractDao {

	private static Logger LOG = Logger.getLogger(NodeDao.class);

	private static NodeDao dao;

	private NodeDao() {
	}

	public static NodeDao getInstance() {
		if (null == dao) {
			dao = new NodeDao();
		}

		return dao;
	}

	public void insert(Node node) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("values next value for seq_node_id");
			rs = ps.executeQuery();
			int key;
			if (rs.next()) {
				key = rs.getInt(1);

				ps = getConnection().prepareStatement("INSERT INTO node (NODE_ID, HOSTNAME, PORT, START_TIME, LAST_PING) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
				ps.setInt(1, key);
				ps.setString(2, node.getHostname());
				ps.setInt(3, node.getPort());
				ps.execute();

				node.setId(key);
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

	public Node findRandomicNode() {
		List<Node> all = getAllOnline();

		ArrayList<Node> arrayList = new ArrayList<Node>();
		for (Node node : all) {
			if (node.isConnectedToVBox()) {
				arrayList.add(node);
			}
		}

		if (arrayList.isEmpty()) return null;

		Collections.shuffle(arrayList);

		return arrayList.get(0);
	}

	public Node findNodeById(int nodeId){
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("SELECT * FROM NODE WHERE NODE_ID = ?");
			ps.setInt(1, nodeId);
			rs = ps.executeQuery();
			if (rs.next()) {
				Node node = new Node();
				node.setId(rs.getInt("node_id"));
				node.setHostname(rs.getString("hostname"));
				node.setPort(rs.getInt("port"));
				node.setConnectedToVBox(rs.getBoolean("vbox_connected"));
				node.setVboxVersion(rs.getString("vbox_version"));
				return node;
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}

		return null;
	}

	public void remove(Node node) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("DELETE FROM NODE WHERE NODE_ID = ?");
			ps.setInt(1, node.getId());
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

	public List<Node> getAllOnline() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Node> nodes = new ArrayList<Node>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM NODE WHERE TIMESTAMPDIFF(SQL_TSI_SECOND, LAST_PING, current_timestamp) <= 60");
			rs = ps.executeQuery();
			if (rs.next()) {
				Node node = new Node();
				node.setId(rs.getInt("node_id"));
				node.setHostname(rs.getString("hostname"));
				node.setPort(rs.getInt("port"));
				node.setStartTime(rs.getTimestamp("start_time"));
				node.setLastPing(rs.getTimestamp("last_ping"));
				node.setConnectedToVBox(rs.getBoolean("vbox_connected"));
				node.setVboxVersion(rs.getString("vbox_version"));
				nodes.add(node);
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}

		return nodes;
	}

	public void updateLastPing(Node node) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("UPDATE NODE SET LAST_PING=?, VBOX_VERSION=?, VBOX_CONNECTED=? WHERE NODE_ID = ?");
			ps.setTimestamp(1, new Timestamp(node.getLastPing().getTime()));
			ps.setString(2, node.getVboxVersion());
			ps.setBoolean(3, node.isConnectedToVBox());
			ps.setInt(4, node.getId());
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

	public List<Node> findNodeByHostname(String hostname) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Node> nodes = new ArrayList<Node>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM NODE WHERE HOSTNAME = ?");
			ps.setString(1, hostname);
			rs = ps.executeQuery();
			if (rs.next()) {
				Node node = new Node();
				node.setId(rs.getInt("node_id"));
				node.setHostname(rs.getString("hostname"));
				node.setPort(rs.getInt("port"));
				node.setConnectedToVBox(rs.getBoolean("vbox_connected"));
				node.setVboxVersion(rs.getString("vbox_version"));
				nodes.add(node);
			}
		} catch (SQLException e) {
			LOG.debug(e);
		} finally {
			cleanUp(ps, rs);
		}

		return nodes;
	}
}
