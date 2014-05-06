package com.Lumension.android.permission_scanner;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class PermissionAdapter extends android.widget.ArrayAdapter<ListEntry<Integer>> {
	
	List<ListEntry<Integer>> data;
	PermissionAdapter(Context c, List<ListEntry<Integer>> list) {
		super(c, R.layout.permission_list_item, R.id.applicationListView,list);
		this.data = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ExtendedEditText permissionEditText;
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.permission_list_item, parent,
					false);
			
		    permissionEditText = (ExtendedEditText)row.findViewById(R.id.permissionEditText);
		    row.setTag(permissionEditText);
		}
		else
		{
			permissionEditText = (ExtendedEditText)row.getTag();
		}
		
		TextView permissionTextView = (TextView)row.findViewById(R.id.permissionTextView);
		
		permissionTextView.setText(getItem(position).getEntryName());
		permissionEditText.setText(getItem(position).getEntryValue().toString());
		permissionEditText.clearTextChangedListeners();
	    permissionEditText.addTextChangedListener(new SpecialTextWatcher(permissionEditText,row,getContext()));

		return row;
	}
	
	@Override
	public ListEntry<Integer> getItem(int pos)
	{
		return data.get(pos);
	}
	
	@Override 
	public int getCount()
	{
		return data.size();
	}
}