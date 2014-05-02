package com.Lumension.android.permission_scanner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The activity that displays the details of an application, only used on small
 * and normal screens
 * 
 * @author Ean Lombardo
 * 
 */
public class ApplicationDetailActivity extends ActionBarActivity implements
		OnItemClickListener {

	Menu menu;
	ApplicationDetailFragment appDetailsFrag;
	ActionBarDrawerToggle drawerToggle;

	/**
	 * Creates the ApplicationDetailActivity. Loads an Application Detail Fragment with the apropriate application
	 * and modifies the Menu according to the exception list
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.application_detail_activity);
		appDetailsFrag = (ApplicationDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.detailActivityFragment);

		Intent intent = getIntent();
		Integer applicationPosition = (Integer) intent
				.getSerializableExtra("applicationId");
		appDetailsFrag.loadApplication(applicationPosition);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.androidDrawer);
		ListView drawerList = (ListView) findViewById(R.id.leftDrawer);
		drawerList.setOnItemClickListener(this);
		drawerList.setSelection(0);

		drawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.drawerStrings)));

		drawerToggle = new ActionBarDrawerToggle(this, drawer, R.drawable.icon,
				R.string.manageButton, R.string.removeBlacklist);
		drawer.setDrawerListener(drawerToggle);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	/**
	 * Creates the options menu for the Application being displayed. Modifies itself depending on
	 * whether or not the Application is in the exceptions list.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.details, menu);
		menu.findItem(R.id.menuFilter).setVisible(false);

		this.menu = menu;
		setMenuFromApplication();

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Handles an option being selected by adding/removing Applications to the exceptions list
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.blacklistDetailsAddMenuItem:
			ApplicationExceptionList.getInstance().addEntry(
					appDetailsFrag.getLoadedApplication().getName(),
					ListType.BLACKLIST);
			setMenuFromApplication();
			appDetailsFrag.getLoadedApplication().setThreatLevel(
					appDetailsFrag.getLoadedApplication().calculateThreat());
			ApplicationExceptionList.getInstance().saveToMemory(this);
			return true;
		case R.id.blacklistDetailsRemoveMenuItem:
			ApplicationExceptionList.getInstance().removeEntry(
					appDetailsFrag.getLoadedApplication().getName());
			setMenuFromApplication();
			appDetailsFrag.getLoadedApplication().setThreatLevel(
					appDetailsFrag.getLoadedApplication().calculateThreat());
			ApplicationExceptionList.getInstance().saveToMemory(this);
			return true;
		case R.id.whitelistDetailsAddMenuItem:
			ApplicationExceptionList.getInstance().addEntry(
					appDetailsFrag.getLoadedApplication().getName(),
					ListType.WHITELIST);
			setMenuFromApplication();
			appDetailsFrag.getLoadedApplication().setThreatLevel(
					appDetailsFrag.getLoadedApplication().calculateThreat());
			ApplicationExceptionList.getInstance().saveToMemory(this);
			return true;
		case R.id.whitelistDetailsRemoveMenuItem:
			ApplicationExceptionList.getInstance().removeEntry(
					appDetailsFrag.getLoadedApplication().getName());
			setMenuFromApplication();
			appDetailsFrag.getLoadedApplication().setThreatLevel(
					appDetailsFrag.getLoadedApplication().calculateThreat());
			ApplicationExceptionList.getInstance().saveToMemory(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Finds out if and how the application is in the exceptions list and modifies the menu to display the appropriate options.
	 */
	private void setMenuFromApplication() {
		if (appDetailsFrag != null
				&& appDetailsFrag.getLoadedApplication() != null) {
			@SuppressWarnings("unchecked")
			ListEntry<String> entry = (ListEntry<String>) (ApplicationExceptionList
					.getInstance().findEntry(appDetailsFrag
					.getLoadedApplication().getName()));

			((MenuItem) menu.findItem(R.id.whitelistDetailsRemoveMenuItem))
					.setVisible(false);
			((MenuItem) menu.findItem(R.id.blacklistDetailsRemoveMenuItem))
					.setVisible(false);
			((MenuItem) menu.findItem(R.id.blacklistDetailsAddMenuItem))
					.setVisible(false);
			((MenuItem) menu.findItem(R.id.whitelistDetailsAddMenuItem))
					.setVisible(false);
			if (entry != null
					&& (entry.getEntryValue().equals(ListType.WHITELIST) || entry
							.getEntryValue().equals(ListType.BLACKLIST))) {
				if (entry.getEntryValue().equals(ListType.WHITELIST)) {
					((MenuItem) menu
							.findItem(R.id.whitelistDetailsRemoveMenuItem))
							.setVisible(true);
				} else {
					((MenuItem) menu
							.findItem(R.id.blacklistDetailsRemoveMenuItem))
							.setVisible(true);
				}
			} else {
				((MenuItem) menu.findItem(R.id.blacklistDetailsAddMenuItem))
						.setVisible(true);
				((MenuItem) menu.findItem(R.id.whitelistDetailsAddMenuItem))
						.setVisible(true);
			}
		}
	}

	/**
	 * Handles options being clicked in the NavigationDrawer, opens the proper activity.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			Intent intent = new Intent(getBaseContext(), MainActivity.class);
			intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		if (position == 1) {
			Intent intent = new Intent(getBaseContext(),
					ApplicationRatingsActivity.class);
			intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
	}

}
