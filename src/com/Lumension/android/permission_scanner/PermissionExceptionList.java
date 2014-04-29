package com.Lumension.android.permission_scanner;

/**
 * Class representing the Application Exception List
 * 
 * @author Derek Allen
 *
 */
public class PermissionExceptionList extends ExceptionList {

	/**
	 * Single Instance of the {@link PermissionExceptionList}
	 */
	public static PermissionExceptionList instance = null;
	
	public static final String EXLISTFILENAME = "PermExList.xml";
	
	/**
	 * Get the singleton {@link PermissionExceptionList} instance and create it if it doesn't exist.
	 * 
	 * @return {@link ExceptionList} singleton
	 */
	public static PermissionExceptionList getInstance(){
		if(instance == null)
            instance = new PermissionExceptionList();
        return instance;
	}
}

