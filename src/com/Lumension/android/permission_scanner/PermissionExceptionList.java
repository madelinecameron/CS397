package com.Lumension.android.permission_scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.util.Xml;

/**
 * Class containing and modifying the permission exception lists. 
 * 
 * @author Derek Allen
 *
 */
public class PermissionExceptionList extends Activity {

	//Single Instance of the ApplicationException List
	public static PermissionExceptionList instance = null;
	
	//The hashtable containing the lists
	private static Map<String,PermissionListEntry> list;
	
	public static final String PERMISSIONEXLISTFILENAME = "PermissionExList.xml";
	
	/**
	 * Get the singleton PermissionExceptionList instance
	 * 
	 * @return {@link PermissionExceptionList} singleton
	 */
	public static PermissionExceptionList getInstance(){
		if(instance == null)
            instance = new PermissionExceptionList();
        return instance;
	}
	
	public boolean addEntry(String permissionName, String permissionList)
	{
		PermissionListEntry previous = list.put(permissionName, new PermissionListEntry(permissionName,permissionList));
		if(previous == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean removeEntry(String permissionName)
	{
		PermissionListEntry previous = list.remove(permissionName);
		if(previous == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public PermissionListEntry findEntry(String permissionName)
	{
		if(list.containsKey(permissionName))
		{
			return list.get(permissionName);
		}
		return null;
	}
	
	public boolean loadFromMemory()
	{
		try {
			FileInputStream fis = openFileInput(PERMISSIONEXLISTFILENAME);
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);
            parser.nextTag();
            while (parser.next() != XmlPullParser.END_TAG) {
            	parser.require(XmlPullParser.START_TAG, null, "PermissionListEntry");
            	parser.nextTag();
            	parser.require(XmlPullParser.START_TAG, null, "permissionName");
            	String permissionName = parser.getText();
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "permissionName");
            	parser.nextTag();
            	parser.require(XmlPullParser.START_TAG, null, "permissionList");
            	String permissionList = parser.getText();
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "permissionList");
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "PermissionListEntry");
            	parser.nextTag();
            	list.put(permissionName, new PermissionListEntry(permissionName, permissionList));
            }
            fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean saveToMemory()
	{
		try {
			FileOutputStream fos = openFileOutput(PERMISSIONEXLISTFILENAME, Context.MODE_PRIVATE);
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("UTF-8",true);
			for(PermissionListEntry value : list.values()){
				xmlSerializer.startTag(null, "PermissionListEntry");
				xmlSerializer.startTag(null, "permissionName");
				xmlSerializer.text(value.getPermissionName());
				xmlSerializer.endTag(null, "permissionName");
				xmlSerializer.startTag(null, "permissionList");
				xmlSerializer.text(value.getPermissionList());
				xmlSerializer.endTag(null, "permissionList");
				xmlSerializer.endTag(null, "PermissionListEntry");
			}
			xmlSerializer.endDocument();
			xmlSerializer.flush();
			String dataWrite = writer.toString();
			fos.write(dataWrite.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<PermissionListEntry> getList(String permissionList)
	{
		List<PermissionListEntry> entriesInList = new ArrayList<PermissionListEntry>();
		for (PermissionListEntry value : list.values()) {
		    if(value.getPermissionList() == permissionList)
		    {
		    	entriesInList.add(value);
		    }
		}
		return entriesInList;
	}
	
	public void clearList(String permissionList)
	{
		for (Iterator<Map.Entry<String,PermissionListEntry>> it = list.entrySet().iterator(); it.hasNext();)
		{
			 Map.Entry<String,PermissionListEntry> e = it.next();
			 if (e.getValue().getPermissionList() == permissionList) {
				 it.remove();
			 }
		}
	}
}
