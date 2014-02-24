package com.Lumension.android.permission_scanner;

import android.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** Mock database entry for white/black listing and update tracking
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

public class DBEntry {
    /** Possible values for "listed" status of an application */
    public enum LISTED { BLACK, WHITE, NOT }

    /** Local name of the package */
    private String packageName;

    /** Local LISTED instance */
    private LISTED listed;

    /** This maps old version codes (the first Integer) to a pairing of a threat level (the second int)
     *  and a description of the problem with that (old) version (e.g. Vulnerability)
     */
    private Map<Integer,Pair<Integer,String>> updateHistory;

    /** Constructor for the DBEntry object
     *
     * @param name The package name of the entry
     * @param listed The listing of the object
     */
    public DBEntry(String name, LISTED listed){
        this.packageName=name;
        this.listed=listed;
        updateHistory = new HashMap<Integer, Pair<Integer, String>>();
    }

    /** Adds an update to the Database
     *
     * @param version The version number of the package update
     * @param threat The threat level of the update
     * @param description The description of the update
     */
    public void addUpdate(int version, int threat, String description){
        if(!this.updateHistory.containsKey(version)){
            this.updateHistory.put(version, Pair.create(threat, description));
        }
    }

    /** Updates the listing of the package
     *
     * @param listed The listing of the object
     */
    public void updateList(LISTED listed) {
        /* If for some reason the DB has an app listed as BLACK AND something else, stick with BLACK */
        if(this.listed != LISTED.BLACK)
            this.listed = listed;
    }

    /** Get the packageName of the DBEntry
     *
     * @return The packageName value
     */
    public String getPackageName(){
        return this.packageName;
    }

    /** Get the Listed value
     *
     * @return The listed value
     */
    public LISTED getList(){
        return this.listed;
    }

    /** Get the update status of the entry
     *
     * @param version The version of the currently installed app
     * @return List of missing updates based on version code, consist of threat value and description of updates
     */
    public List<Pair<Integer,String>> getUpdateStatus(int version){
        List<Pair<Integer,String>> missingUpdates = new LinkedList<Pair<Integer,String>>();
        for(Integer updateVersion : this.updateHistory.keySet()){
            if(version <= updateVersion)
                missingUpdates.add(this.updateHistory.get(updateVersion));
        }
        return missingUpdates;
    }
}
