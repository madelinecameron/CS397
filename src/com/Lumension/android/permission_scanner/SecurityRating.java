/**
 * Object to keep app and system security ratings organized.
 * @author Madeline Cameron
 * @version 1
 * 
 */

package com.Lumension.android.permission_scanner;

import android.graphics.Color;
import java.util.ArrayList;
//import java.awt.Color;
import java.util.List;


public class SecurityRating {
	public Color ratingColor;
	public int ratingNum;
	public ArrayList<String> relatedPermissionNames;
	public int storeRating;
	public int downloads;
	
	/**
	 * Creates a SecurityRating object using algorithm with the given permissions, 
	 * app rating(0­5), and number of downloads as inputs.  
	 * @param relatedPerms permissions of the application
	 * @param appRating app store rating of the application
	 * @param numDownloads number of app store downloads
	 * @return a SecurityRating object
	 */
	public SecurityRating Create(ArrayList<String> relatedPerms, int appRating, int numDownloads)
	{
		//Needs Michael's new algorithm
		return new SecurityRating();
	}
	
	/**
	 * Creates a SecurityRating object with the 
	 * given ratingColor, ratingNum and list of permissions.
	 * </p> 
	 * Used for overall system security rating and 
	 * potentially if a different rating algorithm was used / needed
	 * @param ratingColor color part of the rating
	 * @param ratingNum numerical part of the rating
	 * @param relatedPerms permissions used to come to this rating
	 * @return a SecurityRating object
	 */
	public SecurityRating Create(Color ratingColor, int ratingNum, ArrayList<String> relatedPerms)
	{
		this.ratingColor = ratingColor;
		this.ratingNum = ratingNum;
		this.relatedPermissionNames = relatedPerms;
		
		return this;
	}
	
}