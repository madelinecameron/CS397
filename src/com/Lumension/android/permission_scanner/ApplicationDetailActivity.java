package com.Lumension.android.permission_scanner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * The activity that displays the details of an application, only used on small and normal screens
 * @author Ean Lombardo
 *
 */
public class ApplicationDetailActivity extends ActionBarActivity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.application_detail_activity);
		ApplicationDetailFragment frag = (ApplicationDetailFragment)getSupportFragmentManager().findFragmentById(R.id.detailActivityFragment);
		
		Intent intent = getIntent();
	    Integer applicationPosition = (Integer)intent.getSerializableExtra("applicationId");
		frag.loadApplication(applicationPosition);
	}

}
