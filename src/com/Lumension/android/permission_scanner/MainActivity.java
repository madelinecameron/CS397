package com.Lumension.android.permission_scanner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnItemClickListener{
	
	SecurityRating rating;
	ProgressDialog dialog;
	Activity context;
	ActionBarDrawerToggle drawerToggle;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = this;
        
        setContentView(R.layout.system_security_activity);
        
        DrawerLayout drawer= (DrawerLayout)findViewById(R.id.androidDrawer);
        ListView drawerList = (ListView)findViewById(R.id.leftDrawer);
        drawerList.setOnItemClickListener(this);
        drawerList.setSelection(0);
        
        drawerList.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.drawerStrings)));
        
        drawerToggle = new ActionBarDrawerToggle(this,drawer,R.drawable.icon,R.string.manageButton,R.string.removeBlacklist);
        drawer.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
       
        try {
        	 FileInputStream fis;
        	 fis = openFileInput(ApplicationExceptionList.EXLISTFILENAME);
			 fis.close();
		} catch (Exception e) {
			copyAsset(ApplicationExceptionList.EXLISTFILENAME);
		} 
        

        try {
        	 FileInputStream fis;
        	 fis = openFileInput(PermissionRiskValueList.EXLISTFILENAME);
			 fis.close();
		} catch (Exception e) {
			copyAsset(PermissionRiskValueList.EXLISTFILENAME);
		} 
        
        ApplicationExceptionList.getInstance().loadFromMemory(this);
        PermissionRiskValueList.getInstance().loadFromMemory(this);
        
        dialog = ProgressDialog.show(this, "", "Scanning System", true);
		dialog.show();
        
		new RetreiveListTask().execute(new View[4]);
    }
	
	private void copyAsset(String assetName) {
		try {
			AssetManager assetManager = getAssets();
			InputStream asset;
			asset = assetManager.open(assetName);

			FileOutputStream saver;
			saver = this.openFileOutput(assetName, Context.MODE_PRIVATE);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = asset.read(buffer)) != -1) {
				saver.write(buffer, 0, read);
			}
			
			Log.d("lumension","Copying asset: "+assetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class RetreiveListTask extends AsyncTask<View, Void, View> {

	    protected View doInBackground(View... params) {

	    	if(ApplicationList.appList == null || ApplicationList.appList.size() != 0)
	    	{
	    		PackageManager pm = getPackageManager();
	    		ApplicationList.getInstance(pm);
	    	}
		    
	    	return params[0];

	    }

	    protected void onPostExecute(View view) 
	    {
	    	SystemSecurity sysSec = new SystemSecurity(context);
		    rating = sysSec.Calculate();
	    	dialog.dismiss();
	    	
	    	TextView overallRatingTextBox = (TextView)context.findViewById(R.id.overallRatingTextBox);
	    	overallRatingTextBox.setText("Overall Rating: "+String.valueOf(rating.getSystemRating()+ rating.getTotalApplicatioNRating()));
	    	
	    	TextView totalAppRatingtextBox = (TextView)context.findViewById(R.id.totalAppRatingtextBox);
	    	totalAppRatingtextBox.setText("Application Rating: "+String.valueOf(rating.getTotalApplicatioNRating()));
	    	
	    	TextView averageAppRatingTextBox = (TextView)context.findViewById(R.id.averageAppRatingTextBox);
	    	averageAppRatingTextBox.setText("Average App Rating: "+String.valueOf(rating.getAverageApplicationRating()));
	    	
	    	TextView systemRatingTextBox = (TextView)context.findViewById(R.id.systemRatingTextBox);
	    	systemRatingTextBox.setText("System Rating: "+String.valueOf(rating.getSystemRating()));
	    	
	    	ListView reasonsListView = (ListView)context.findViewById(R.id.reasonsListView);
	    	reasonsListView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,rating.getReasons()));
	    }

	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
 
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position == 0)
		{
			Intent intent = new Intent(getBaseContext(),MainActivity.class);
			intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		if(position == 1)
		{
			Intent intent = new Intent(getBaseContext(),ApplicationRatingsActivity.class);
			intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
	}

}
