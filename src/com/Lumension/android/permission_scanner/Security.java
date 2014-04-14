package com.Lumension.android.permission_scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.provider.Settings;
import android.provider.Settings.System;
import android.content.ContentResolver;
import android.content.*;
import android.util.*;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

/** An activity object to be used with the Application detail view
 *
 * @author Madeline Cameron
 * @author Ean Lombardo
 * @author Derek Allen
 * @author Michael Neumiller
 *
 */

public class SystemSecurity {
	private SecurityRating overallSecurity;
	private List<String> sysSettingsList = Arrays.asList( "AIRPLANE_MODE_ON", "BLUETOOTH_DISCOVERABILITY", "BLUETOOTH_ON", "INSTALL_NON_MARKET_APPS");
	private	List<String> secureSettingsList = Arrays.asList("LOCK_PATTERN_VISIBLE", "LOCK_PATTERN_ENABLED");
		
	public void CalculateSystemSecurity() {
		Settings.Secure secureSettings = new Settings.Secure();
		Settings.System sysSettings = new Settings.System();
		ContentResolver resolver = null;
		
		for(Iterator<String> i = sysSettingsList.iterator(); i.hasNext(); ) {
			String value = System.getString(resolver, i.toString());
			Log.println(0, "Test", value);
		}
	}
	
	public void RemovePermissionFromList(String permissionName)
	{
	}
	
	public void AddPermissionToList(String permissionName)
	{
	}
	
	public void ModifyPermissionSeverity(String permissionName, int modifier)
	{
	}
}
