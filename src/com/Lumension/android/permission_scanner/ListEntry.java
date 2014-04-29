package com.Lumension.android.permission_scanner;

/**
 * Class for entries in the Application Exception list. Each instance will represent
 * a application and what list it belongs to.
 * 
 * @author Derek
 *
 */
public class ListEntry {
	private String entryName;
	private String entryList;
	
	/**
	 * Creates an AppListEntry object with the specified parameters
	 * 
	 * @param entryName A string containing the package name of the app this object represents (ex."com.android.gesture.builder").
	 * @param entryList The list that the app belongs to.
	 */
	public ListEntry(String entryName, String entryList)
	{
		this.entryName = entryName;
		this.entryList = entryList;
	}
	
	/**
	 * Getter for entryName
	 * 
	 * @return entryName
	 */
	public String getEntryName()
	{
		return this.entryName;
	}
	
	/**
	 * Setter for entryName
	 * 
	 * @param entryName The new entryName
	 */
	public void setEntryName(String entryName)
	{
		this.entryName = entryName;
	}
	
	/**
	 * Getter for entryList
	 * 
	 * @return entryList
	 */
	public String getEntryList()
	{
		return this.entryList;
	}
	
	/**
	 * Setter for entryList
	 * 
	 * @param entryList The new entryList
	 */
	public void setEntryList(String entryList)
	{
		this.entryList = entryList;
	}
}
