package com.Lumension.android.permission_scanner;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Lists the applications by their ratings with appropriate coloring. On Small and Normal screen selecting
 * an Application opens a ApplicationDetails activity. On Larger screens, it displayes the details next to the list.
 * 
 * @author Madeline Cameron
 * @author Ean Lombardo
 * @author Derek Allen
 * @author Michael Neumiller ------
 * @author Aaron Pope
 * @author Chuck Housley
 * @author Kyle Jamison
 * @author Connor McKinney
 * 
 */

public class ApplicationRatingsActivity extends ActionBarActivity implements
		OnItemClickListener, OnQueryTextListener {
	
	/**
	 * The Menu for this Activity
	 */
	Menu menu;
	
	/**
	 * The Detail Fragment, only used on Larger screens
	 */
	ApplicationDetailFragment appDetailsFrag;
	
	/**
	 * The List Fragments
	 */
	ApplicationListFragment appListFrag;
	
	/**
	 * Handles the action bar
	 */
	ActionBarDrawerToggle drawerToggle;
	
	/**
	 * The ListView for the items in the drawer, tracked because multiple menu types are used here.
	 */
	ListView drawerList;
	
	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). Defaults to null
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		appListFrag = (ApplicationListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.singleFragment);
		if (appListFrag == null) {
			appListFrag = (ApplicationListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.mainListFragment);
		}

		appDetailsFrag = (ApplicationDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mainDetailFragment);

		if (appDetailsFrag == null) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.androidDrawer);
		drawerList = (ListView) findViewById(R.id.leftDrawer);
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
	 * Called by the ApplicationListFragment telling the activity what
	 * Application was selected
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == drawerList) {
			if (position == 0) {
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				startActivity(intent);
			}
			DrawerLayout mDrawerLayout;
			mDrawerLayout = (DrawerLayout) findViewById(R.id.androidDrawer);
			mDrawerLayout.closeDrawers();
		} else {
			if (appDetailsFrag != null) {
				appDetailsFrag.loadApplication(((Application) parent
						.getAdapter().getItem(position)).getIndex());
				setMenuFromApplication();
			} else {
				Intent intent = new Intent(getBaseContext(),
						ApplicationDetailActivity.class);
				intent.putExtra("applicationId", ((Application) parent
						.getAdapter().getItem(position)).getIndex());
				intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
			}
		}
	}

	/**
	 * Creates the menu in the ActionBar. Right now just creates the search
	 * field for the Application list
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menuFilter)
				.getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(true);
		searchView.setOnQueryTextListener(this);
		this.menu = menu;

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Called when the search field is used. Filters the Application list
	 */
	@Override
	public boolean onQueryTextChange(String newText) {
		appListFrag.setFilter(newText);
		return true;
	}

	/**
	 * Not used, overridden as a requirement for the OnQueryListener class, but
	 * submission is not used
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	/**
	 * Called every time the Activity resumes (ie you return from another screen)
	 */
	@Override
	protected void onResume() {
		super.onResume();
		try {
			appListFrag.resetList();
		} catch (Exception e) {
		}
	}
	
	/**
	 * Handles a menu item being selected, or the application drawer opening
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.blacklistAddMenuItem:
			ApplicationExceptionList.getInstance().addEntry(
					appDetailsFrag.getLoadedApplication().getName(),
					ListType.BLACKLIST);
			setMenuFromApplication();
			appDetailsFrag.getLoadedApplication().setThreatLevel(
					appDetailsFrag.getLoadedApplication().calculateThreat());
			appListFrag.resetList();
			ApplicationExceptionList.getInstance().saveToMemory(this);
			return true;
		case R.id.blacklistRemoveMenuItem:
			ApplicationExceptionList.getInstance().removeEntry(
					appDetailsFrag.getLoadedApplication().getName());
			setMenuFromApplication();
			appDetailsFrag.getLoadedApplication().setThreatLevel(
					appDetailsFrag.getLoadedApplication().calculateThreat());
			appListFrag.resetList();
			ApplicationExceptionList.getInstance().saveToMemory(this);
			return true;
		case R.id.whitelistAddMenuItem:
			ApplicationExceptionList.getInstance().addEntry(
					appDetailsFrag.getLoadedApplication().getName(),
					ListType.WHITELIST);
			setMenuFromApplication();
			appDetailsFrag.getLoadedApplication().setThreatLevel(
					appDetailsFrag.getLoadedApplication().calculateThreat());
			appListFrag.resetList();
			ApplicationExceptionList.getInstance().saveToMemory(this);
			return true;
		case R.id.whitelistRemoveMenuItem:
			ApplicationExceptionList.getInstance().removeEntry(
					appDetailsFrag.getLoadedApplication().getName());
			setMenuFromApplication();
			appDetailsFrag.getLoadedApplication().setThreatLevel(
					appDetailsFrag.getLoadedApplication().calculateThreat());
			appListFrag.resetList();
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

			((MenuItem) menu.findItem(R.id.whitelistRemoveMenuItem))
					.setVisible(false);
			((MenuItem) menu.findItem(R.id.blacklistRemoveMenuItem))
					.setVisible(false);
			((MenuItem) menu.findItem(R.id.blacklistAddMenuItem))
					.setVisible(false);
			((MenuItem) menu.findItem(R.id.whitelistAddMenuItem))
					.setVisible(false);
			if (entry != null
					&& (entry.getEntryValue().equals(ListType.WHITELIST) || entry
							.getEntryValue().equals(ListType.BLACKLIST))) {
				if (entry.getEntryValue().equals(ListType.WHITELIST)) {
					((MenuItem) menu.findItem(R.id.whitelistRemoveMenuItem))
							.setVisible(true);
				} else {
					((MenuItem) menu.findItem(R.id.blacklistRemoveMenuItem))
							.setVisible(true);
				}
			} else {
				((MenuItem) menu.findItem(R.id.blacklistAddMenuItem))
						.setVisible(true);
				((MenuItem) menu.findItem(R.id.whitelistAddMenuItem))
						.setVisible(true);
			}
		}
	}
}
