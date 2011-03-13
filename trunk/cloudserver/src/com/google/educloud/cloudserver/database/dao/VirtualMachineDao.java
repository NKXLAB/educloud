package com.google.educloud.cloudserver.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.educloud.internal.entities.VirtualMachine;

public class VirtualMachineDao {

	private static int currentId = 0;

	private static List<VirtualMachine> virtualMachines = new ArrayList<VirtualMachine>();

	private static VirtualMachineDao dao;

	public static VirtualMachineDao getInstance() {
		if (null == dao) {
			dao = new VirtualMachineDao();
		}

		return dao;
	}

	public void insert(VirtualMachine machine) {
		machine.setId(++currentId);
		virtualMachines.add(machine);
	}
}
