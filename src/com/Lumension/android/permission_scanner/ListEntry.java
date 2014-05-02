package com.Lumension.android.permission_scanner;

/**
 * Class for entries in the persistent lists.
 * 
 * 
 * @author Derek
 * 
 */
public class ListEntry<T> {
	private String entryName;
	private T entryValue;

	/**
	 * Creates an AppListEntry object with the specified parameters
	 * 
	 * @param entryName
	 *            A string containing the package name of the app this object
	 *            represents (ex."com.android.gesture.builder").
	 * @param entryList
	 *            The list that the app belongs to.
	 */
	public ListEntry(String entryName, T entryList) {
		this.entryName = entryName;
		this.entryValue = entryList;
	}

	/**
	 * Getter for entryName
	 * 
	 * @return entryName
	 */
	public String getEntryName() {
		return this.entryName;
	}

	/**
	 * Setter for entryName
	 * 
	 * @param entryName
	 *            The new entryName
	 */
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	/**
	 * Getter for entryValue
	 * 
	 * @return entryValue
	 */
	public T getEntryValue() {
		return this.entryValue;
	}

	/**
	 * Setter for entryValue
	 * 
	 * @param entryList
	 *            The new entryValue
	 */
	public void setEntryValue(T entryList) {
		this.entryValue = entryList;
	}
}
