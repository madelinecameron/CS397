package com.Lumension.android.permission_scanner;

import android.graphics.drawable.Drawable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/** A basic container object that contains information on a specific app
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

public class Application {
    /** The label of the application */
    private String label;

    /** The name of the application */
    private String name;

    /** The version code of the application */
    private int versionCode;

    /** The boolean value of if the application is a system app */
    private boolean system;

    /** The numerical threat level of the app */
    private int threatLevel;

    /** The description of the threat type */
    private String threatDescription;

    /** The official application icon */
    private Drawable icon;

    /** A list of each requested permission */
    private List<String> permissions;

    /** Constructor for the Application object
     *
     * @param label The label of the application
     * @param name The name of the application
     * @param versionCode The version code of the application
     * @param system boolean value of if the application is a system app
     * @param icon the application icon
     */
    public Application(String label, String name, int versionCode, boolean system, Drawable icon) {
        this.label = label;
        this.name = name;
        this.versionCode = versionCode;
        this.system = system;
        this.icon = icon;
        this.threatDescription = "";
        permissions = new ArrayList<String>();
    }

    /** Get the label of the object
     *
     * @return The application label
     */
    public String getLabel() {
        return label;
    }

    /** Set the value for label
     *
     * @param label The new label value
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /** Get the value for name
     *
     * @return The value for name
     */
    public String getName() {
        return name;
    }

    /** Set the value for name
     *
     * @param name The new name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Get the value for versionCode
     *
     * @return The value for versionCode
     */
    public Integer getVersionCode() {
        return versionCode;
    }

    /** Set the value for the versionCode
     *
     * @param versionCode The new versionCode value
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /** Returns a boolean value of if the app is system or not
     *
     * @return True if the app is a system app, false otherwise
     */
    public boolean isSystem() {
        return system;
    }

    /** Set the value for the system
     *
     * @param system The new system value
     */
    public void setSystem(boolean system) {
        this.system = system;
    }

    /** Get the value for threatLevel
     *
     * @return The value for threatLevel
     */
    public int getThreatLevel() {
        return threatLevel;
    }

    /** Set the value for threatLevel
     *
     * @param threatLevel The new value for threatLevel
     */
    public void setThreatLevel(int threatLevel) {
        this.threatLevel = threatLevel;
    }

    /** Set the value for description
     *
     * @param description The new value for description
     */
    public void setThreatDescription(String description) {
        this.threatDescription = description;
    }

    /** Get the value for threatDescription
     *
     * @return The value for threatDescription
     */
    public String getThreatDescription() {
        return threatDescription;
    }

    /** Get the value for icon
     *
     * @return The value for icon
     */
    public Drawable getIcon() {
        return icon;
    }

    /** Set the value for icon
     *
     * @param icon The new value for icon
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /** Get the value for permissions
     *
     * @return The list of permissions
     */
    public List<String> getPermissions() {
        return permissions;
    }

    /** Adds the permission to the permissions list
     *
     * @param permissionName The new string value to be added to the permission list
     */
    public void addPermission(String permissionName) {
        // Make the output more human readable
        String cleanName = permissionName.replace('_', ' ');
        cleanName = cleanName.toLowerCase();
        cleanName = Character.toUpperCase(cleanName.charAt(0)) + cleanName.substring(1);
        if (!permissions.contains(cleanName))
            permissions.add(cleanName);
    }
    /** Whether of not this application uses the camera permissions
     * 
     * @return True if the application declares the android.permissions.camera permission
     */
    public boolean usesCamera()
    {
    	return permissions.contains("Camera");
    }

    /** Gets the hexadecimal color value associated with the threat level
     *
     * @return The hexadecimal value of the color
     */
    public int getThreatColor() {
        int color = 0xFF000000;
        if(threatLevel>50)
            color += (0xFF0000 - 0x100*(int)(0xFF*((float)(threatLevel-50)/50)));
        else
            color += (0xFF00 + 0x10000*(int)(0xFF*((float)threatLevel/50)));
        return color;
    }

    /** Generates and sets the threat level for the application
     *
     * @return The threat level associated with this app
     */
    public int calculateThreat() {
        int threat = 0;

        if(UpdateDB.getInstance().findList(this) == DBEntry.LISTED.BLACK) {
            this.setThreatDescription("Known malicious application!");
            return 100;
        }
        else if(UpdateDB.getInstance().findList(this) == DBEntry.LISTED.WHITE) {
            this.setThreatDescription("Trusted application");
            return 0;
        }
        List<Pair<Integer, String>> updateList = UpdateDB.getInstance().checkForUpdates(this);
        if(updateList != null && !(updateList.isEmpty())) {
            for(Pair<Integer, String> update : updateList) {
                threat += update.first;
            }
            if(threat > 50)
                this.setThreatDescription("Missing critical updates");
            else
                this.setThreatDescription("Missing updates");
        }
        for (String p : permissions) {
            if (p.equals("Internet")) {
                threat += 33;
            }
            else if (p.equals("Call phone")) {
                threat += 30;
            }
            else if (p.equals("Send sms")) {
                threat += 30;
            }
            else if (p.equals("Receive sms")) {
                threat += 30;
            }
            else if (p.equals("Read sms")) {
                threat += 30;
            }
            else if (p.equals("Write sms")) {
                threat += 25;
            }
            else if (p.equals("Vibrate")) {
                threat += 10;
            }
            else if (p.equals("Access course location")) {
                threat += 18;
            }
            else if (p.equals("Access fine location")) {
                threat += 18;
            }
            else if (p.equals("Write external storage")) {
                threat += 20;
            }
            else if (p.equals("Read contacts")) {
                threat += 22;
            }
            else if (p.equals("Write contacts")) {
                threat += 22;
            }
            else {threat += 2;}
        }
        if (threat > 100) {
            threat = 100;
        }
        if (threat == 0) {
            threat = 1; // Distinguishes from white listed apps
        }
        if(this.getThreatDescription().equals("")) {
            if(threat > 70)
                this.setThreatDescription("Risk Level: High");
            else if(threat > 50)
                this.setThreatDescription("Risk Level: Moderate");
            else if(threat > 20)
                this.setThreatDescription("Risk Level: Low");
            else
                this.setThreatDescription("Risk Level: Minimal");
        }
        return threat;
    }
}

