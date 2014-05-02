package com.Lumension.android.permission_scanner;

import android.app.Activity;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.content.*;
import android.util.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages and calculates system security rating.
 * 
 * @author Madeline Cameron
 */
//Credit to Peter
// Stribrany at
// http://stackoverflow.com/questions/507602/how-to-initialise-a-static-map-in-java
public class SystemSecurity extends Activity {
	private static Map<String, Integer> sysSettingsMap = createSysSetMap();
	private static Map<String, Integer> secureSettingsMap = createSecSetMap();
	private Context context;

	/**
	 * Initializes the static map for the system settings
	 * @return The static map for the system settings
	 */
	private static Map<String, Integer> createSysSetMap() { 
		Map<String, Integer> builder = new HashMap<String, Integer>();

		builder.put("AIRPLANE_MODE_ON", Integer.MIN_VALUE);
		builder.put("BLUETOOTH_ON", 25);
		builder.put("INSTALL_NON_MARKET_APPS", 50);

		return Collections.unmodifiableMap(builder);
	}

	/**
	 * Initializes the static map for the secure settings
	 * @return The static map for the secure settings
	 */
	private static Map<String, Integer> createSecSetMap() { 
		Map<String, Integer> builder = new HashMap<String, Integer>();

		builder.put("LOCATION_MODE_HIGH_ACCURACY", 15);
		builder.put("WIFI_ON", 30);
		builder.put("LOCK_PATTERN_VISIBLE", 10);
		builder.put("LOCK_PATTERN_ENABLED", -25);

		return Collections.unmodifiableMap(builder);
	}

	
	public SystemSecurity(Context context) {
		this.context = context;
	}
	
	/**
	 * Calculates the overall system security rating
	 */
	public SecurityRating Calculate() {

		SecurityRating rating = new SecurityRating();

		int sysSecRating = 0;
		for (Map.Entry<String, Integer> secSet : sysSettingsMap.entrySet()) {
			String value = System.getString(context.getContentResolver(),
					secSet.getKey().toLowerCase());
			Log.d("TEST",
					secSet.getKey() + " " + value + " "
							+ String.valueOf(secSet.getValue()));
			if (value != null) {
				if (Integer.parseInt(value) == 1) { // Can't be boolean
					sysSecRating += secSet.getValue();

					if (secSet.getValue() > 0) {
						rating.addReason("Insecure Setting: "
								+ secSet.getKey().toLowerCase());
					} else {
						rating.addReason("Extra Secure Setting: "
								+ secSet.getKey().toLowerCase());
					}
				}
			}
		}

		for (Map.Entry<String, Integer> secSet : secureSettingsMap.entrySet()) {
			String value = Secure
					.getString(context.getContentResolver(), secSet.getKey()
							.toLowerCase());
			Log.d("TEST",
					secSet.getKey() + " " + value + " "
							+ String.valueOf(secSet.getValue()));
			if (value != null) {
				if (Integer.parseInt(value) == 1) { // Can't be boolean
					sysSecRating += secSet.getValue();
					if (secSet.getValue() > 0) {
						rating.addReason("Insecure Setting: "
								+ secSet.getKey().toLowerCase());
					} else {
						rating.addReason("Extra Secure Setting: "
								+ secSet.getKey().toLowerCase());
					}
				}
			}
		}

		WifiManager wifiMan = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		for (WifiConfiguration wifi : wifiMan.getConfiguredNetworks()) {
			if (wifi.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.NONE)) {
				sysSecRating += 35;
				rating.addReason("Unsecured Wifi Network Configured: "
						+ wifi.SSID);

			}
		}

		int appTotal = 0;

		for (Application app : ApplicationList.appList) {
			if (ApplicationExceptionList.getInstance().findEntry(app.getName()) == null) {
				appTotal += app.getThreatLevel();
			}
		}

		rating.setTotalApplicationRating(appTotal);
		rating.setSystemRating(sysSecRating);
		rating.setAverageApplicationRating(((double) appTotal)
				/ ApplicationList.appList.size());

		Log.d("TEST", String.valueOf(sysSecRating));

		if (sysSecRating < 0)
			sysSecRating = 0;

		Log.d("TEST", String.valueOf(sysSecRating));

		return rating;
	}
}
