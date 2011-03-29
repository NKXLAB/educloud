<%@page import="com.google.educloud.api.entities.User"%>
<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="userBean"
	class="com.google.educloud.gui.beans.UserBean" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"></meta>
<title>EduCloud</title>
<jsp:include page="scripts.jsp" />
<style type="text/css">
table {
	display: table;
	border-collapse: separate;
	border-spacing: 2px 2px;
	border-color: gray;
}

.cloudgrid {
	width: 100%;
	text-align: left;
	border-collapse: collapse;
}

.cloudgrid th {
	background: #d6d6d6;
	display: table-cell;
	vertical-align: inherit;
	padding: 8px;
}

.cloudgrid td {
	display: table-cell;
	vertical-align: inherit;
	border-bottom: 1px solid white;
	border-top: 1px solid transparent;
	padding: 8px;
	background: #ebebeb;
}

input {
	width: auto;
	display: block;
	padding: auto;
	margin: 0 0 0 0;
	font-size: 18px;
	color: #3a3a3a;
	
}
select {
	width: auto;
	display: block;
	padding: auto;
	margin: 0 0 0 0;
	font-size: 12px;
	color: #3a3a3a;
	padding: 0px;
}
</style>
<script type="text/javascript">
function checkAll(selection) {
	var b = selection == 'select' ? true : false;
	$('input[name^="usr_"]').each(function (index) {
		$(this).attr('checked', b);
	});
}

function executeAction(o) {
	if ($(o).val() == 'delete') {
		var selected = [], i=0;
		$('input[name^="usr_"]').each(function (index) {
			if ($(this).is(':checked')) {
				selected[i++] = $(this).val();
			}
		});
		
		if (selected.length > 0) {
			$.post("deleteUsers", {'users':selected.join(';')}, function(data) {
				window.location = "users.jsp";
			}, 'json')
			.error(function() {
				alert("error");
			});
		}
	}
}
</script>
</head>
<body>
<div id="container"><jsp:include page="header.jsp" />
<div id="content">
<div style="margin:10px">
<div>
<div style="background:#ebebeb;padding:5px;">
Select: <a href="#" onclick="checkAll('select')">All</a> | <a href="#" onclick="checkAll('unselect')">None</a>
<select style="display:inline;" onchange="executeAction(this)">
	<option value="">Actions...</option>
	<option value="delete">Delete</option>
</select>
</div>
<table id="usersTable" class="cloudgrid">
	<tr>
		<th>-</th>
		<th>ID</th>
		<th>Name</th>
		<th>Login</th>
		<th>Type</th>
	</tr>
	<%
		List<User> users = userBean.getUsers(session);

		for (User user : users) {
	%>
	<tr>
		<td width="10px">
			<input type="checkbox" name="usr_<%=user.getId()%>" id="usr_<%=user.getId()%>" value="<%=user.getId()%>" />
		</td>
		<td><%=user.getId()%></td>
		<td><%=user.getName()%></td>
		<td><%=user.getLogin()%></td>
		<td><%=user.getType()%></td>
	</tr>
	<%
		}
	%>
</table>
</div>
</div>
</div>
<jsp:include page="footer.jsp" />
</div>
</body>
</html>
