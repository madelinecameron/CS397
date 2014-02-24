package com.Lumension.android.permission_scanner;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.*;

/** A singleton class that contains list of application objects with information about each app
 *
 * @author Madeline Cameron
 * @author Ean Lombardo
 * @author Derek Allen
 * @author Michael Neumiller
 * ------
 * @author Aaron Pope
 * @author Chuck Housley
 * @author Kyle Jamison
 * @author Connor McKinney
 *
 */

public class ApplicationList {
    /** The single instance of the ApplicationList */
    private static ApplicationList instance = null;

    /** The list of each application object */
    public static List<Application> AppList;

    /** Gets the ApplicationList instance
     *
     * @param pm The package manager object required to create the permissions list
     * @return The single instance of the ApplicationList
     */
    public static ApplicationList getInstance(PackageManager pm) {
        if(instance == null) {
            instance = new ApplicationList(pm);
        }
        return instance;
    }

    /** Constructor for the ApplicationList
     *
     * @param packageManager The PackageManager object from witch the application list will be constructed from
     */
    protected ApplicationList(PackageManager packageManager){
        List<String> alreadyAdded = new ArrayList<String>();
        AppList = new ArrayList<Application>();
        Application applicationToAdd;
        String packageName;
        String applicationLabel;
        int versionCode;
        boolean system;
        String permissionName;
        PackageInfo packageInfo;
        Drawable icon;
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo applicationInfo : packages){
            system = ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            if(!system){
                packageName = applicationInfo.packageName;
                try {
                    applicationLabel = packageManager.getApplicationLabel(applicationInfo).toString();
                } catch (Exception except) {
                    applicationLabel = packageName;
                }
                try {
                    packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
                    icon = packageManager.getApplicationIcon(packageName);
                    versionCode = packageInfo.versionCode;
                } catch (Exception except) {
                    icon = null;
                    versionCode = 0;
                }
                if(!alreadyAdded.contains(packageName)) {
                    applicationToAdd = new Application(applicationLabel, packageName, versionCode, system, icon);
                    try {
                        packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
                        if(packageInfo.requestedPermissions != null && packageInfo.requestedPermissions.length > 0) {
                            for(int i = 0; i < packageInfo.requestedPermissions.length; i++) {
                                if(packageInfo.requestedPermissions[i].startsWith("android.permission.")) {
                                    permissionName = packageInfo.requestedPermissions[i].substring("android.permission.".length());
                                    applicationToAdd.addPermission(permissionName);
                                }
                            }
                        }
                    } catch (Exception except) {
                        // Fail
                    }
                    applicationToAdd.setThreatLevel(applicationToAdd.calculateThreat());
                    AppList.add(applicationToAdd);
                    alreadyAdded.add(packageName);
                }
            }
        }
    }

    /** Gets the requested application
     *
     * @param index The index value of the requested application
     * @return The Application object at the specified index
     */
    public Application getApplication(int index) {
        return AppList.get(index);
    }
}
