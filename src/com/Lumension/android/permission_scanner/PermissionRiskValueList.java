package com.Lumension.android.permission_scanner;

/**
 * Class representing the Permission Risk Value List
 * 
 * @author Derek Allen
 *
 */
public class PermissionRiskValueList extends PersistantList<Integer> {

	/**
	 * Single Instance of the {@link PermissionRiskValueList}
	 */
	public static PermissionRiskValueList instance = null;
	
	public static final String EXLISTFILENAME = "PermRVList.xml";
	
	/**
	 * Get the singleton {@link PermissionRiskValueList} instance and create it if it doesn't exist.
	 * 
	 * @return {@link PersistantList} singleton
	 */
	public static PermissionRiskValueList getInstance(){
		if(instance == null)
            instance = new PermissionRiskValueList();
        return instance;
	}
}

