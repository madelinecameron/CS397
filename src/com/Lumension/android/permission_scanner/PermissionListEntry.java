package com.Lumension.android.permission_scanner;

public class PermissionListEntry {
	private String permissionName;
	private String permissionList;
	
	public PermissionListEntry(String permissionName, String permissionList)
	{
		this.permissionName = permissionName;
		this.permissionList = permissionList;
	}
	
	public String getPermissionName()
	{
		return this.permissionName;
	}
	
	public void setPermissionName(String permissionName)
	{
		this.permissionName = permissionName;
	}
	
	public String getPermissionList()
	{
		return this.permissionList;
	}
	
	public void setPermissionList(String permissionList)
	{
		this.permissionList = permissionList;
	}
}
