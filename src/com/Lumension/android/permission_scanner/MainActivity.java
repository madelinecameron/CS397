package com.Lumension.android.permission_scanner;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/** Main function called at program launch
 *
 * @author Madeline Cameron
 * @author Ean Lombardo
 * @author Derek Allen
 * @author Michael Neumiller
 * ------
 * @author Aaron Pope
 * @author Chuck Housley
 * @author Kyle Jamison
 * @author Connor McKinney
 *
 */

public class MainActivity extends ActionBarActivity implements OnItemClickListener,OnQueryTextListener {
    /** Local ApplicationList object instance*/
    

    /** Local UpdateDB object instance */
    UpdateDB updateDB;


    /** Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this
     *                           Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Defaults to null
     */
    ApplicationDetailFragment appDetailsFrag;
    ApplicationListFragment appListFrag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        UpdateDB.getInstance().addEntry("com.android.gesture.builder", DBEntry.LISTED.BLACK);
        UpdateDB.getInstance().addEntry("com.example.android.apis", DBEntry.LISTED.WHITE);
        UpdateDB.getInstance().addEntry("com.nordicusability.jiffy", DBEntry.LISTED.WHITE);
        UpdateDB.getInstance().addEntry("com.android.widgetpreview", 17, 0, "Cosmetic update");
        UpdateDB.getInstance().addEntry("com.android.widgetpreview", 18, 30, "Security update");
        UpdateDB.getInstance().addEntry("com.android.smoketest", 18, 100, "Critical update");

        updateDB = UpdateDB.getInstance();
        
        appListFrag = (ApplicationListFragment)getSupportFragmentManager().findFragmentById(R.id.singleFragment);
        if(appListFrag == null)
        {
        	appListFrag = (ApplicationListFragment)getSupportFragmentManager().findFragmentById(R.id.mainListFragment);
        }
        
        appDetailsFrag = (ApplicationDetailFragment)getSupportFragmentManager().findFragmentById(R.id.mainDetailFragment);
        if(appDetailsFrag == null)
        {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else
        {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * Called by the ApplicationListFragment telling the activity what Application was selected
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	 
    	 if(appDetailsFrag != null)
    	 {
    		 appDetailsFrag.loadApplication(((Application)parent.getAdapter().getItem(position)).getIndex());
    	 }
    	 else
    	 {
    		 Intent intent = new Intent(getBaseContext(),ApplicationDetailActivity.class);
    		 intent.putExtra("applicationId", ((Application)parent.getAdapter().getItem(position)).getIndex());
    		 startActivity(intent);
    	 }
    }
    
    /**
     * Creates the menu in the ActionBar. Right now just creates the search field for the Application list
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     getMenuInflater().inflate(R.menu.main, menu);
     SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
           SearchView searchView = (SearchView) menu.findItem(R.id.menuFilter).getActionView();
    
           searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
           searchView.setSubmitButtonEnabled(true);
           searchView.setOnQueryTextListener(this);
    
           return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * Called when the search field is used. Filters the Application list
     */
    @Override
    public boolean onQueryTextChange(String newText)
    {
    	appListFrag.setFilter(newText);
        return true;
    }
    
    /**
     * Not used, overriden as a requirement for the OnQueryListener class, but submission is not used
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
     return false;
    }
}
