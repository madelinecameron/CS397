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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Runs the system scan and displays overall results and the reasons for those results.
 * @author Ean Lombardo
 *
 */
public class MainActivity extends ActionBarActivity implements
		OnItemClickListener {

	boolean refresh = false;
	/**
	 * The current rating of the system
	 */
	SecurityRating rating;
	
	/**
	 * A simple "Scanning..." dialog so the user knows the process is working
	 */
	ProgressDialog dialog;
	
	/**
	 * This activity, used for parental referencing within the Asynchronous task.
	 */
	Activity context;
	
	/**
	 * Handles the navigation drawer
	 */
	ActionBarDrawerToggle drawerToggle;

	/**
	 * Creates the Activity. Deploying the default xml files if they arent present, displaying the progress
	 * dialog and starting the thread that runs the scan.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;

		setContentView(R.layout.system_security_activity);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.androidDrawer);
		ListView drawerList = (ListView) findViewById(R.id.leftDrawer);
		drawerList.setOnItemClickListener(this);
		drawerList.setSelection(0);

		drawerList.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.drawerStrings)));

		drawerToggle = new ActionBarDrawerToggle(this, drawer, R.drawable.icon,
				R.string.manageButton, R.string.removeBlacklist);
		drawer.setDrawerListener(drawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		try {
			FileInputStream fis;
			fis = openFileInput(ApplicationExceptionList.getInstance().getFilename());
			fis.close();
		} catch (Exception e) {
			copyAsset(ApplicationExceptionList.getInstance().getFilename());
		}

		try {
			FileInputStream fis;
			fis = openFileInput(PermissionRiskValueList.getInstance().getFilename());
			fis.close();
		} catch (Exception e) {
			copyAsset(PermissionRiskValueList.getInstance().getFilename());
		}

		ApplicationExceptionList.getInstance().loadFromMemory(this);
		PermissionRiskValueList.getInstance().loadFromMemory(this);

		dialog = ProgressDialog.show(this, "", "Scanning System", true);
		dialog.show();

		new RetreiveListTask().execute(new View[4]);
	}

	/**
	 * Copies an asset file from the application to the applications data directory
	 * @param assetName
	 */
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

			Log.d("lumension", "Copying asset: " + assetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs the actual system scan in a separate thread (necessary as android does not allow network communication on the primary thread
	 * and updates the application with the values, then dismisses the progress dialog.
	 * @author Ean Lombardo
	 *
	 */
	class RetreiveListTask extends AsyncTask<View, Void, View> {
		
		/**
		 * Runs in a separate thread. Runs the actuall system scan
		 */
		protected View doInBackground(View... params) {
			PackageManager pm = getPackageManager();
			if(refresh)
			{
				ApplicationList.regenerate(pm);
			}

				ApplicationList.getInstance(pm);
				

			return params[0];

		}

		/**
		 * Updates the interface with the values from the scan and dismisses the progress dialog
		 */
		protected void onPostExecute(View view) {
			SystemSecurity sysSec = new SystemSecurity(context);
			rating = sysSec.Calculate();
			dialog.dismiss();

			TextView overallRatingTextBox = (TextView) context
					.findViewById(R.id.overallRatingTextBox);
			
			overallRatingTextBox.setText("Overall Rating: "
					+ String.valueOf(rating.getSystemRating()
							+ rating.getTotalApplicationRating()));

			TextView totalAppRatingtextBox = (TextView) context
					.findViewById(R.id.totalAppRatingtextBox);
			totalAppRatingtextBox.setText("Application Rating: "
					+ String.valueOf(rating.getTotalApplicationRating()));

			TextView averageAppRatingTextBox = (TextView) context
					.findViewById(R.id.averageAppRatingTextBox);
			averageAppRatingTextBox.setText("Average App Rating: "
					+ String.valueOf(String.format("%.2f",rating.getAverageApplicationRating())));

			TextView systemRatingTextBox = (TextView) context
					.findViewById(R.id.systemRatingTextBox);
			systemRatingTextBox.setText("System Rating: "
					+ String.valueOf(rating.getSystemRating()));

			ListView reasonsListView = (ListView) context
					.findViewById(R.id.reasonsListView);
			reasonsListView.setAdapter(new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, rating.getReasons()));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Handles the navigation drawer being opened
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		drawerToggle.onOptionsItemSelected(item);
		
		if(item.getItemId() == R.id.rescanOption)
		{
			dialog = ProgressDialog.show(this, "", "Scanning System", true);
			dialog.show();
			
			refresh = true;
			new RetreiveListTask().execute(new View[4]);
		}

		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Handles user selection in the navigation drawer.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			Intent intent = new Intent(getBaseContext(), MainActivity.class);
			startActivity(intent);
		}
		if (position == 1) {
			Intent intent = new Intent(getBaseContext(),
					ApplicationRatingsActivity.class);
			startActivity(intent);
		}
		if (position == 2) {
			Intent intent = new Intent(getBaseContext(),
					PermissionListActivity.class);
			startActivity(intent);
		}
		DrawerLayout mDrawerLayout;
		mDrawerLayout = (DrawerLayout) findViewById(R.id.androidDrawer);
		mDrawerLayout.closeDrawers();
	}

}
