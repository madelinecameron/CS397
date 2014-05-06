package com.Lumension.android.permission_scanner;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SpecialTextWatcher implements TextWatcher{
	EditText edit;
	View entry;
	Context context;
	SpecialTextWatcher(EditText edit, View entry,Context context)
	{
		this.edit = edit;
		this.entry = entry;
		this.context = context;
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		try
		{
			TextView permissionTextView = (TextView)entry.findViewById(R.id.permissionTextView);
			PermissionRiskValueList.getInstance().findEntry(permissionTextView.getText().toString()).setEntryValue(Integer.valueOf(edit.getText().toString()));
			PermissionRiskValueList.getInstance().saveToMemory(context);
		}
		catch(NumberFormatException ex){}
		
	}

}
