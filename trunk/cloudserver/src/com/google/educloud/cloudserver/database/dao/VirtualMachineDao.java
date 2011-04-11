package com.google.educloud.cloudserver.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.educloud.internal.entities.Template;
import com.google.educloud.internal.entities.VirtualMachine;
import com.google.educloud.internal.entities.VirtualMachine.VMState;

public class VirtualMachineDao extends AbstractDao {

	private static Logger LOG = Logger.getLogger(VirtualMachineDao.class);

	private static VirtualMachineDao dao;

	private VirtualMachineDao() {
	}

	public static VirtualMachineDao getInstance() {
		if (null == dao) {
			dao = new VirtualMachineDao();
		}

		return dao;
	}

	public void insert(VirtualMachine machine, Template template) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("values next value for seq_machine_id");
			rs = ps.executeQuery();
			int key;
			if (rs.next()) {
				key = rs.getInt(1);

				ps = getConnection().prepareStatement("INSERT INTO MACHINE (MACHINE_ID, USER_ID, NAME, DESCRIPTION, OS_TYPE, STATE) VALUES (?, ?, ?, ?, ?, ?)");
				ps.setInt(1, key);
				ps.setInt(2, machine.getUserId());
				ps.setString(3, machine.getName());
				ps.setString(4, machine.getDescription());
				ps.setString(5, template.getOsType());
				ps.setString(6, machine.getState().name());
				ps.execute();

				machine.setId(key);

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

	public VirtualMachine findById(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("SELECT * FROM MACHINE WHERE MACHINE_ID = ?");
			ps.setInt(1, id);

			rs = ps.executeQuery();
			if (rs.next()) {
				VirtualMachine vm = new VirtualMachine();
				vm.setId(rs.getInt("machine_id"));
				vm.setBootableMedium(rs.getString("bootable_medium"));
				vm.setName(rs.getString("name"));
				vm.setDescription(rs.getString("description"));
				vm.setOsType(rs.getString("os_type"));
				vm.setNodeId(rs.getInt("node_id"));
				vm.setState(VMState.valueOf(rs.getString("state")));
				vm.setUserId(rs.getInt("user_id"));
				vm.setUUID(rs.getString("uuid"));
				vm.setVboxSession(rs.getString("vbox_uuid"));
				return vm;
			}
		} catch (SQLException e) {
			LOG.error(e);
		} finally {
			cleanUp(ps, rs);
		}

		return null;
	}

	public List<VirtualMachine> getAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VirtualMachine> machines = new ArrayList<VirtualMachine>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM MACHINE");
			rs = ps.executeQuery();
			while (rs.next()) {
				VirtualMachine vm = new VirtualMachine();
				vm.setId(rs.getInt("machine_id"));
				vm.setBootableMedium(rs.getString("bootable_medium"));
				vm.setName(rs.getString("name"));
				vm.setDescription(rs.getString("description"));
				vm.setOsType(rs.getString("os_type"));
				vm.setNodeId(rs.getInt("node_id"));
				vm.setState(VMState.valueOf(rs.getString("state")));
				vm.setUUID(rs.getString("uuid"));
				vm.setVboxSession(rs.getString("vbox_uuid"));
				machines.add(vm);
			}
		} catch (SQLException e) {
			LOG.error(e);
		} finally {
			cleanUp(ps, rs);
		}

		return machines;
	}

	public List<VirtualMachine> getAllByUser(int userId) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VirtualMachine> machines = new ArrayList<VirtualMachine>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM MACHINE WHERE USER_ID = ?");
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				VirtualMachine vm = new VirtualMachine();
				vm.setId(rs.getInt("machine_id"));
				vm.setBootableMedium(rs.getString("bootable_medium"));
				vm.setName(rs.getString("name"));
				vm.setDescription(rs.getString("description"));
				vm.setOsType(rs.getString("os_type"));
				vm.setNodeId(rs.getInt("node_id"));
				vm.setState(VMState.valueOf(rs.getString("state")));
				vm.setUUID(rs.getString("uuid"));
				vm.setVboxSession(rs.getString("vbox_uuid"));
				machines.add(vm);
			}
		} catch (SQLException e) {
			LOG.error(e);
		} finally {
			cleanUp(ps, rs);
		}

		return machines;

	}
	
	public List<VirtualMachine> getAllByNode(int nodeId) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VirtualMachine> machines = new ArrayList<VirtualMachine>();
		try {
			ps = getConnection().prepareStatement("SELECT * FROM MACHINE WHERE NODE_ID = ?");
			ps.setInt(1, nodeId);
			rs = ps.executeQuery();
			if (rs.next()) {
				VirtualMachine vm = new VirtualMachine();
				vm.setId(rs.getInt("machine_id"));
				vm.setBootableMedium(rs.getString("bootable_medium"));
				vm.setName(rs.getString("name"));
				vm.setDescription(rs.getString("description"));
				vm.setOsType(rs.getString("os_type"));
				vm.setNodeId(rs.getInt("node_id"));
				vm.setState(VMState.valueOf(rs.getString("state")));
				vm.setUUID(rs.getString("uuid"));
				vm.setVboxSession(rs.getString("vbox_uuid"));
				machines.add(vm);
			}
		} catch (SQLException e) {
			LOG.error(e);
		} finally {
			cleanUp(ps, rs);
		}

		return machines;

	}

	public void remove(VirtualMachine vm) {
		PreparedStatement ps = null;

		try {
				ps = getConnection().prepareStatement("DELETE FROM MACHINE WHERE MACHINE_ID=?");
				ps.setInt(1, vm.getId());
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

	public void changeState(VirtualMachine machine) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("UPDATE MACHINE SET STATE=?, UUID=?, VBOX_UUID=?, BOOTABLE_MEDIUM=? WHERE MACHINE_ID=?");
			ps.setString(1, machine.getState().name());
			ps.setString(2, machine.getUUID());
			ps.setString(3, machine.getVboxSession());
			ps.setString(4, machine.getBootableMedium());
			ps.setInt(5, machine.getId());
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
			cleanUp(ps, rs);
		}
	}
	
	public void clearNode(int nodeId) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("UPDATE MACHINE SET NODE_ID=NULL WHERE NODE_ID=?");
			ps.setInt(1, nodeId);
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
			cleanUp(ps, rs);
		}
	}

	public void updateNode(int vmId, int nodeId) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement("UPDATE MACHINE SET NODE_ID=? WHERE MACHINE_ID=?");
			ps.setInt(1, nodeId);
			ps.setInt(2, vmId);
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
			cleanUp(ps, rs);
		}
	}

}