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
 * Class containing and modifying the application exception lists. 
 * 
 * @author Derek Allen
 *
 */
public class ApplicationExceptionList extends Activity {

	//Single Instance of the ApplicationException List
	public static ApplicationExceptionList instance = null;
	
	//The hashtable containing the lists
	private static Map<String,AppListEntry> list;
	
	public static final String APPEXLISTFILENAME = "AppExList.xml";
	
	/**
	 * Get the singleton ApplicationExceptionList instance
	 * 
	 * @return {@link ApplicationExceptionList} singleton
	 */
	public static ApplicationExceptionList getInstance(){
		if(instance == null)
            instance = new ApplicationExceptionList();
        return instance;
	}
	
	public boolean addEntry(String packageName, String packageList)
	{
		AppListEntry previous = list.put(packageName, new AppListEntry(packageName,packageList));
		if(previous == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean removeEntry(String packageName)
	{
		AppListEntry previous = list.remove(packageName);
		if(previous == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public AppListEntry findEntry(String packageName)
	{
		if(list.containsKey(packageName))
		{
			return list.get(packageName);
		}
		return null;
	}
	
	public boolean loadFromMemory()
	{
		try {
			FileInputStream fis = openFileInput(APPEXLISTFILENAME);
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);
            parser.nextTag();
            while (parser.next() != XmlPullParser.END_TAG) {
            	parser.require(XmlPullParser.START_TAG, null, "AppListEntry");
            	parser.nextTag();
            	parser.require(XmlPullParser.START_TAG, null, "PackageName");
            	String packageName = parser.getText();
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "PackageName");
            	parser.nextTag();
            	parser.require(XmlPullParser.START_TAG, null, "PackageList");
            	String packageList = parser.getText();
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "PackageList");
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "AppListEntry");
            	parser.nextTag();
            	list.put(packageName, new AppListEntry(packageName, packageList));
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
			FileOutputStream fos = openFileOutput(APPEXLISTFILENAME, Context.MODE_PRIVATE);
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("UTF-8",true);
			for(AppListEntry value : list.values()){
				xmlSerializer.startTag(null, "AppListEntry");
				xmlSerializer.startTag(null, "PackageName");
				xmlSerializer.text(value.getPackageName());
				xmlSerializer.endTag(null, "PackageName");
				xmlSerializer.startTag(null, "PackageList");
				xmlSerializer.text(value.getPackageList());
				xmlSerializer.endTag(null, "PackageList");
				xmlSerializer.endTag(null, "AppListEntry");
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
	
	public List<AppListEntry> getList(String packageList)
	{
		List<AppListEntry> entriesInList = new ArrayList<AppListEntry>();
		for (AppListEntry value : list.values()) {
		    if(value.getPackageList() == packageList)
		    {
		    	entriesInList.add(value);
		    }
		}
		return entriesInList;
	}
	
	public void clearList(String packageList)
	{
		for (Iterator<Map.Entry<String,AppListEntry>> it = list.entrySet().iterator(); it.hasNext();)
		{
			 Map.Entry<String,AppListEntry> e = it.next();
			 if (e.getValue().getPackageList() == packageList) {
				 it.remove();
			 }
		}
	}
}
