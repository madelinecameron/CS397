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
	private ArrayList<String> reasons = new ArrayList<String>();
	private int totalApplicatioNRating;
	private double averageApplicationRating;
	private int systemRating;
	
	public int getTotalApplicatioNRating() {
		return totalApplicatioNRating;
	}

	public void setTotalApplicatioNRating(int totalApplicatioNRating) {
		this.totalApplicatioNRating = totalApplicatioNRating;
	}

	public double getAverageApplicationRating() {
		return averageApplicationRating;
	}

	public void setAverageApplicationRating(double averageApplicationRating) {
		this.averageApplicationRating = averageApplicationRating;
	}

	public int getSystemRating() {
		return systemRating;
	}

	public void setSystemRating(int systemRating) {
		this.systemRating = systemRating;
	}

	public void addReason(String reason)
	{
		reasons.add(reason);
	}
	
	public List<String> getReasons()
	{
		return reasons;
	}
	
	public int getColor()
	{
		return Color.RED;
	}
}