var lastMachineId = null;
function selectMachine(id) {
	var jqxhr = $.get("vmDetail?vmId=" + id, function(vm) {
		$('#vmDetails').load('panes/vmDetailPane.html', function () {
			$('#vm_id').html(vm.id);
			$('#vm_name').html(vm.name);
			$('#vm_state').html(vm.state);
			$('#vm_ostype').html(vm.template.osType);
			
			$('#vmItem_'+vm.id).toggleClass("unselected", false);
			$('#vmItem_'+vm.id).toggleClass("selected", true);
			
			if (lastMachineId) {
				$('#vmItem_'+lastMachineId).toggleClass("unselected", true);
				$('#vmItem_'+lastMachineId).toggleClass("selected", false);
			}
			
			lastMachineId = vm.id;
			
			
		});
	}, "json")
	.error(function() {
		alert("error");
	});
}