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
 * Abstract class representing the base exception list. 
 * 
 * @author Derek Allen
 *
 */
abstract class ExceptionList extends Activity {
	
	/**
	 * 	The hash table containing all {@link ListEntry} for the lists
	 */
	private static Map<String,ListEntry> list;
	
	public static final String EXLISTFILENAME = "ExList.xml";
	
	/**
	 * Adds the specified application to the list.
	 * 
	 * @param entryName The string representing the entry (ex. Application Package Name "com.android.gesture.builder").
	 * @param entryList The listing value of the entry.
	 * @return True if the entry was previously in the list. False if the entry didn't previously exist in list.
	 */
	public boolean addEntry(String entryName, String entryList)
	{
		ListEntry previous = list.put(entryName, new ListEntry(entryName,entryList));
		if(previous == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Removes the specified entry from the list.
	 * @param entryName The string representing the entry (ex. Application Package Name "com.android.gesture.builder").
	 * @return True if the entry has been removed from a list, False if the entry was not in the list.
	 */
	public boolean removeEntry(String entryName)
	{
		ListEntry previous = list.remove(entryName);
		if(previous == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Finds and returns the {@link ListEntry} representing the specified entry.
	 * @param entryName The string representing the entry (ex. Application Package Name "com.android.gesture.builder").
	 * @return The {@link ListEntry} of the specified entry. Null if the entry is not in a list.
	 */
	public ListEntry findEntry(String entryName)
	{
		if(list.containsKey(entryName))
		{
			return list.get(entryName);
		}
		return null;
	}
	
	/**
	 * Loads the existing exception list from memory into list.
	 * 
	 * First, list is cleared of all entries. Then the XML file specified by EXLISTFILENAME is opened and parsed.
	 * Each XML entry corresponds to an ListEntry object that will be put into list when parsed.
	 * 
	 * If the file does not exist, it is created. If the file is can't be parsed properly, then an exception is thrown.
	 * 
	 * @return True if the load was successful, False if the load failed due to an exception.
	 * If false is returned, the current state of list is invalid.
	 */
	public boolean loadFromMemory()
	{
		try {
			FileInputStream fis = openFileInput(EXLISTFILENAME);
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(fis, null);
            parser.nextTag();
            while (parser.next() != XmlPullParser.END_TAG) {
            	parser.require(XmlPullParser.START_TAG, null, "ListEntry");
            	parser.nextTag();
            	parser.require(XmlPullParser.START_TAG, null, "entryName");
            	String entryName = parser.getText();
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "entryName");
            	parser.nextTag();
            	parser.require(XmlPullParser.START_TAG, null, "entryList");
            	String entryList = parser.getText();
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "entryList");
            	parser.nextTag();
            	parser.require(XmlPullParser.END_TAG, null, "ListEntry");
            	parser.nextTag();
            	list.put(entryName, new ListEntry(entryName, entryList));
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
	
	/**
	 * Saves the list to memory for future use.
	 * 
	 * First, an input stream for the XML specified by EXLISTFILENAME is opened. Then, for each {@link ListEntry} in list,
	 * a new XML entry is written to the file. Once all entries are written, the file is saved and closed.
	 * 
	 *  This should be called any time the exception list is changed so that the list in memory is valid and up to date.
	 *   
	 * @return True if the save is successful, false if the save failed. If false is returned, the file containing the list in memory is invalid
	 */
	public boolean saveToMemory()
	{
		try {
			FileOutputStream fos = openFileOutput(EXLISTFILENAME, Context.MODE_PRIVATE);
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("UTF-8",true);
			for(ListEntry value : list.values()){
				xmlSerializer.startTag(null, "ListEntry");
				xmlSerializer.startTag(null, "entryName");
				xmlSerializer.text(value.getEntryName());
				xmlSerializer.endTag(null, "entryName");
				xmlSerializer.startTag(null, "entryList");
				xmlSerializer.text(value.getEntryList());
				xmlSerializer.endTag(null, "entryList");
				xmlSerializer.endTag(null, "ListEntry");
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
	
	/**
	 * Returns a list of all entries with the specified list value.
	 * @param entryList the value of entries to be returned.
	 * @return The list of entries with the list value specified.
	 */
	public List<ListEntry> getList(String entryList)
	{
		List<ListEntry> entriesInList = new ArrayList<ListEntry>();
		for (ListEntry value : list.values()) {
		    if(value.getEntryList() == entryList)
		    {
		    	entriesInList.add(value);
		    }
		}
		return entriesInList;
	}
	
	/**
	 * Removes all entries from list whose entryList value is the specified entryList, clearing the list.
	 * @param entryList the list to be cleared.
	 */
	public void clearList(String entryList)
	{
		for (Iterator<Map.Entry<String,ListEntry>> it = list.entrySet().iterator(); it.hasNext();)
		{
			 Map.Entry<String,ListEntry> e = it.next();
			 if (e.getValue().getEntryList() == entryList) {
				 it.remove();
			 }
		}
	}
}
