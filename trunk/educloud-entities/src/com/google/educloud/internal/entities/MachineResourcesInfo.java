package com.google.educloud.internal.entities;

//Classe para armanzear as informacoes relativas aos recursos de uma maquina fisica
public class MachineResourcesInfo {
	
	private long totalMemory;
	private long usedMemory;	
	private long totalDiskSpace;
	private long usedDiskSpace;
	
	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}	
	public long getTotalMemory() {
		return totalMemory;
	}	
	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}	
	public long getUsedMemory() {
		return usedMemory;
	}	
	public long getAvaliableMemory() {
		return totalMemory - usedMemory;
	}
	public void setTotalDiskSpace(int totalDiskSpace) {
		this.totalDiskSpace = totalDiskSpace;
	}
	public long getTotalDiskSpace() {
		return totalDiskSpace;
	}
	public void setUsedDiskSpace(long usedDiskSpace) {
		this.usedDiskSpace = usedDiskSpace;
	}
	public long getUsedDiskSpace() {
		return usedDiskSpace;
	}
	public long getAvailableDiskSpace(){
		return totalDiskSpace - usedDiskSpace;
	}
	
}
