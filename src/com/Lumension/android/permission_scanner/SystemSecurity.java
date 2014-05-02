package com.Lumension.android.permission_scanner;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.*;
import android.provider.Settings;
import android.provider.Settings.System;
import android.content.*;
import android.content.pm.PackageManager;
import android.util.*;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

/** 
 * Manages and calculates system security rating.
 * @author Madeline Cameron
 */

public class SystemSecurity extends Activity {
	private static Map<String, Integer> sysSettingsMap = createSysSetMap(); 
	private static Map<String, Integer> secureSettingsMap = createSecSetMap();
	private Context context;
	
	//All values are completely arbitrary, except for "AIRPLANE_MODE_ON"
	private static Map<String, Integer> createSysSetMap() { //Credit to Peter Stribrany at http://stackoverflow.com/questions/507602/how-to-initialise-a-static-map-in-java
		Map<String, Integer> builder = new HashMap<String, Integer>();
		
		builder.put("AIRPLANE_MODE_ON", Integer.MIN_VALUE);
		builder.put("BLUETOOTH_ON", 25);
		builder.put("INSTALL_NON_MARKET_APPS", 50);
		
		return Collections.unmodifiableMap(builder);
	}
	
	private static Map<String, Integer> createSecSetMap() { //Credit to Peter Stribrany at http://stackoverflow.com/questions/507602/how-to-initialise-a-static-map-in-java
		Map<String, Integer> builder = new HashMap<String, Integer>();
		
		builder.put("LOCATION_MODE_HIGH_ACCURACY", 15);
		builder.put("WIFI_ON", 30);
		builder.put("LOCK_PATTERN_VISIBLE", 10);
	    builder.put("LOCK_PATTERN_ENABLED", -25);
	    
	    return Collections.unmodifiableMap(builder);
	}
	
	/**
	 *  Calculates the overall system security rating
	 */
	public SystemSecurity(Context context) {
		this.context=context;
	}
	
	public SecurityRating Calculate() { //Just a preliminary calculation function. Will probably be tweaked later.
		Settings.Secure secureSettings = new Settings.Secure();
		Settings.System sysSettings = new Settings.System();
		
		SecurityRating rating = new SecurityRating();
		
		int sysSecRating = 0;
		for(Map.Entry<String, Integer> secSet : sysSettingsMap.entrySet()) {
			String value = sysSettings.getString(context.getContentResolver(), secSet.getKey().toLowerCase());
			Log.d("TEST", secSet.getKey() + " " + value + " " + String.valueOf(secSet.getValue()));
			if(value != null) {
 				if(Integer.parseInt(value) == 1) { //Can't be boolean
					sysSecRating += secSet.getValue();
					
					if(secSet.getValue()>0)
					{
						rating.addReason("Insecure Setting: "+ secSet.getKey().toLowerCase());
					}
					else
					{
						rating.addReason("Extra Secure Setting: "+ secSet.getKey().toLowerCase());
					}
				}
			}
		}
		
		for(Map.Entry<String, Integer> secSet : secureSettingsMap.entrySet()) {
			String value = secureSettings.getString(context.getContentResolver(), secSet.getKey().toLowerCase());
			Log.d("TEST", secSet.getKey() + " " + value + " " + String.valueOf(secSet.getValue()));
			if(value != null) {
				if(Integer.parseInt(value) == 1) { //Can't be boolean
					sysSecRating += secSet.getValue();
					if(secSet.getValue()>0)
					{
						rating.addReason("Insecure Setting: "+ secSet.getKey().toLowerCase());
					}
					else
					{
						rating.addReason("Extra Secure Setting: "+ secSet.getKey().toLowerCase());
					}
				}
			}
		}
		
		WifiManager wifiMan = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		
		for(WifiConfiguration wifi : wifiMan.getConfiguredNetworks())
		{
			if(wifi.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE))
			{
				sysSecRating += 35;
				rating.addReason("Unsecured Wifi Network Configured: "+wifi.SSID);
				
			}
		}
		
		int appTotal=0;
		
		for(Application app : ApplicationList.appList)
		{
			if(ApplicationExceptionList.getInstance().findEntry(app.getName()) == null)
			{
				appTotal+= app.getThreatLevel();
			}
		}
		
		rating.setTotalApplicatioNRating(appTotal);
		rating.setSystemRating(sysSecRating);
		rating.setAverageApplicationRating(((double)appTotal)/ApplicationList.appList.size());
	
		Log.d("TEST", String.valueOf(sysSecRating));
		
		
		if(sysSecRating < 0)
			sysSecRating = 0; 
		
		Log.d("TEST", String.valueOf(sysSecRating));
		
		return rating;
	}
	/**
	 * Removes a permission from the list of permissions used to grade overall security level.
	 * @param permissionName
	 * @return
	 */
	public boolean RemovePermissionFromList(String permissionName)
	{
		throw new UnsupportedOperationException("This is not implemented.");
	}
	
	public boolean AddPermissionToList(String permissionName)
	{
		throw new UnsupportedOperationException("This is not implemented.");
	}
	
	public boolean ModifyPermissionSeverity(String permissionName, int modifier)
	{
		throw new UnsupportedOperationException("This is not implemented.");
	}
}
