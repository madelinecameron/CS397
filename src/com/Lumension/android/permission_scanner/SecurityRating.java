package com.Lumension.android.permission_scanner;

import java.awt.Color;
import java.util.List;

public class SecurityRating {
	public Color ratingColor;
	public int ratingNum;
	public List<String> relatedPermissionNames;
	public int storeRating;
	public int downloads;
	
	public SecurityRating Create(List<String> relatedPerms)
	{
		return new SecurityRating();
	}
	
	public SecurityRating Create(Color ratingColor, int ratingNum, List<String> relatedPerms)
	{
		return new SecurityRating();
	}
}