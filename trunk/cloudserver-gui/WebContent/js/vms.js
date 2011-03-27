function startVM(id) {
	$.get("controlVirtualMachine?action=start&id=" + id, function() {
		alert("success");
		window.location = 'vms.jsp';
	}).error(function() {
		alert("error");
	});
}

function stopVM(id) {
	$.get("controlVirtualMachine?action=stop&id=" + id, function() {
		alert("success");
		window.location = 'vms.jsp';
	}).error(function() {
		alert("error");
	});
}
