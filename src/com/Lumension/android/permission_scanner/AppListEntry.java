package com.Lumension.android.permission_scanner;

public class AppListEntry {
	private String packageName;
	private String packageList;
	
	public AppListEntry(String packageName, String packageList)
	{
		this.packageName = packageName;
		this.packageList = packageList;
	}
	
	public String getPackageName()
	{
		return this.packageName;
	}
	
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}
	
	public String getPackageList()
	{
		return this.packageList;
	}
	
	public void setPackageList(String packageList)
	{
		this.packageList = packageList;
	}
}
