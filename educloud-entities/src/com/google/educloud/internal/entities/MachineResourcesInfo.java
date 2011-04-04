package com.google.educloud.internal.entities;

//Classe para armanzear as informacoes relativas aos recursos de uma maquina fisica
public class MachineResourcesInfo {
	
	private long totalMemory;
	private long availableMemory;	
		
	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}
	
	public long getTotalMemory() {
		return totalMemory;
	}
	
	public long getUsedMemory() {
		return totalMemory - availableMemory;
	}
	
	public long setAvailableMemory( long availableMemory ) {
		return this.availableMemory = availableMemory;
	}
	
	public long getAvaliableMemory() {
		return availableMemory;
	}	
}
