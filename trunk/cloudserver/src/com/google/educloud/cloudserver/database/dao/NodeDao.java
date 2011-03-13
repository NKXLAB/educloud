package com.google.educloud.cloudserver.database.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.educloud.internal.entities.Node;

public class NodeDao {

	private static int currentId = 0;

	private static List<Node> nodes = new ArrayList<Node>();

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
		node.setId(++currentId);
		nodes.add(node);
	}

	public Node findRandomicNode() {
		if (nodes.isEmpty()) { return null; }

		Collections.shuffle(nodes);

		return nodes.get(0);
	}

}
