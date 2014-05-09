package com.Lumension.android.permission_scanner;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A text watcher that allows the editable permission weights to be edited without declaring objects final, which is problematic in listviews
 * @author Ean Lombardo
 *
 */
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
	
	/**
	 * Called before the text is changed
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Called When the text is changed
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Called after the text is changed. Updates the PermssionsRiskValueList with the appropriate value, and saves it
	 */
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
