package com.Lumension.android.permission_scanner;

import android.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Lumension.android.permission_scanner.DBEntry.LISTED;

/** A singleton class which contains the fake database of white/black lists as well as update information
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

public class UpdateDB {
    /** The single instance of UpdateDB */
    public static UpdateDB instance = null;
    /** Contains the mock database entries */
    private static Map<String,DBEntry> database;

    /** Gets the UpdateDB instance
     *
     * @return The single instance of the UpdateDB
     */
    public static UpdateDB getInstance() {
        if(instance == null)
            instance = new UpdateDB();
        return instance;
    }

    /** Constructor for UpdateDB
     *
     */
    protected UpdateDB(){
        database = new HashMap<String, DBEntry>();
    }

    /** Adds a database entry with a listing value or adds listed status to existing entry
     *
     * @param packageName package name of the application to be listed
     * @param listed listed value of the application (WHITE/BLACK/NOT)
     */
    public void addEntry(String packageName, LISTED listed) {
        DBEntry entry = database.get(packageName);
        if(entry == null) {
            database.put(packageName, new DBEntry(packageName, listed));
        }
        else {
            entry.updateList(listed);
            database.put(packageName, entry);
        }
    }

    /** Adds a database entry for a specific update or adds update to existing entry
     *
     * @param packageName package name of the application
     * @param version the out-dated version code
     * @param threat numeric threat value associated with this out-dated version
     * @param description a description of the purpose of the update
     */
    public void addEntry(String packageName, int version, int threat, String description) {
        DBEntry entry = database.get(packageName);
        if(entry == null) {
            entry = new DBEntry(packageName, LISTED.NOT);
            entry.addUpdate(version, threat, description);
            database.put(packageName, entry);
        }
        else {
            entry.addUpdate(version, threat, description);
            database.put(packageName, entry);
        }
    }

    /** Gets a list of updates for a specific application
     *
     * @param app the application to check for updates
     * @return List of missing updates from the database (consisting of a threat value and description) or null if list is empty
     */
    public List<Pair<Integer, String>> checkForUpdates(Application app){
        DBEntry entry = database.get(app.getName());
        return (entry == null ? null : entry.getUpdateStatus(app.getVersionCode()));
    }

    /** Gets white/black listed status of an application from the database
     *
     * @param app Application to check for listed status
     * @return LISTED.BLACK if app is black listed, LISTED.WHITE if app is white listed and up-to-date,
     *      LISTED.NOT if app is unlisted or is white listed but missing updates
     */
    public LISTED findList(Application app){
        DBEntry entry = database.get(app.getName());
        if(entry == null)
            return LISTED.NOT;
        if(entry.getList() == LISTED.NOT || entry.getList() == LISTED.BLACK)
            return entry.getList();
        /* White listed, check for updates */
        return entry.getUpdateStatus(app.getVersionCode()).isEmpty() ? LISTED.WHITE : LISTED.NOT;
    }
}
