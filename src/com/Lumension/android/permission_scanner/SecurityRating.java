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

/**
 * The security ratings for the system and the reasons for them.
 * 
 * @author Madelin
 * @author Ean Lombardo
 *
 */
public class SecurityRating {
	
	/**
	 * The reasons why the system wide security rating is what it is
	 */
	private ArrayList<String> reasons = new ArrayList<String>();
	
	/**
	 * The sum of all of the application ratings
	 */
	private int totalApplicationRating;
	
	/**
	 * The average of all of the application ratings
	 */
	private double averageApplicationRating;
	
	/** 
	 * The system rating
	 */
	private int systemRating;

	/**
	 * Gets the total Application rating
	 * @return The total Application rating
	 */
	public int getTotalApplicationRating() {
		return totalApplicationRating;
	}

	/**
	 * Sets the total Application rating
	 */
	public void setTotalApplicationRating(int totalApplicationRating) {
		this.totalApplicationRating = totalApplicationRating;
	}

	/**
	 * Gets the average Application rating
	 * @return The average Application rating
	 */
	public double getAverageApplicationRating() {
		return averageApplicationRating;
	}

	/**
	 * Sets the average Application rating
	 */
	public void setAverageApplicationRating(double averageApplicationRating) {
		this.averageApplicationRating = averageApplicationRating;
	}

	/**
	 * Gets the system rating
	 * @return The system rating
	 */
	public int getSystemRating() {
		return systemRating;
	}

	/**
	 * Sets the system rating
	 */
	public void setSystemRating(int systemRating) {
		this.systemRating = systemRating;
	}

	/**
	 * Adds a reason to the reason list
	 * @param reason
	 */
	public void addReason(String reason) {
		reasons.add(reason);
	}

	/**
	 * Gets the reason list
	 * @return The reason list
	 */
	public List<String> getReasons() {
		return reasons;
	}

	/**
	 * Gets the color that represents the overall rating
	 * @return The color that represents the overall rating
	 */
	public int getColor() {
		return Color.RED;
	}
}