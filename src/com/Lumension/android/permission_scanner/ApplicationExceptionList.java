package com.Lumension.android.permission_scanner;

/**
 * Class representing the Application Exception List
 * 
 * @author Derek Allen
 * 
 */
public class ApplicationExceptionList extends PersistantList<String> {

	/**
	 * Single Instance of the {@link ApplicationExceptionList}
	 */
	public static ApplicationExceptionList instance = null;

	/**
	 * Get the singleton {@link ApplicationExceptionList} instance and create it
	 * if it doesn't exist.
	 * 
	 * @return {@link PersistantList} singleton
	 */
	public static ApplicationExceptionList getInstance() {
		if (instance == null)
			instance = new ApplicationExceptionList();
		return instance;
	}

	@Override
	public String getFilename() {
		return "AppExList.xml";
	}
}
