package com.Lumension.android.permission_scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


/**
 * The activity that displayes a list of the permissions with editable weights
 * @author Ean Lombardo
 *
 */
public class PermissionListActivity extends ActionBarActivity implements OnItemClickListener{
	/**
	 * Handles the navigation drawer
	 */
	ActionBarDrawerToggle drawerToggle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.permission_list_activity);
		
		ListView permListView = (ListView)findViewById(R.id.permissionListView);
		
		PermissionAdapter permAdapter = new PermissionAdapter(this,PermissionRiskValueList.getInstance().getAllEntries());
		permListView.setAdapter(permAdapter);
		
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
	 * Handles a user clicking in the application drawer
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
		DrawerLayout mDrawerLayout;
		mDrawerLayout = (DrawerLayout) findViewById(R.id.androidDrawer);
		mDrawerLayout.closeDrawers();
	}
	
	/**
	 * Handles the drawer opening when the icon is clicked
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
