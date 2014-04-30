package com.Lumension.android.permission_scanner;

/**
 * Class representing the Application Exception List
 * 
 * @author Derek Allen
 *
 */
public class ApplicationExceptionList extends ExceptionList {

	/**
	 * Single Instance of the {@link ApplicationExceptionList}
	 */
	public static ApplicationExceptionList instance = null;
	
	public static final String EXLISTFILENAME = "AppExList.xml";
	
	/**
	 * Get the singleton {@link ApplicationExceptionList} instance and create it if it doesn't exist.
	 * 
	 * @return {@link ExceptionList} singleton
	 */
	public static ApplicationExceptionList getInstance(){
		if(instance == null)
            instance = new ApplicationExceptionList();
        return instance;
	}
}
