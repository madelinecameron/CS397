package com.Lumension.android.permission_scanner;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The activity that displays the details of an application, only used on small and normal screens
 * @author Ean Lombardo
 *
 */
public class ApplicationDetailActivity extends ActionBarActivity{
	
	Menu menu;
	ApplicationDetailFragment appDetailsFrag;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.application_detail_activity);
		appDetailsFrag = (ApplicationDetailFragment)getSupportFragmentManager().findFragmentById(R.id.detailActivityFragment);
		
		Intent intent = getIntent();
	    Integer applicationPosition = (Integer)intent.getSerializableExtra("applicationId");
	    appDetailsFrag.loadApplication(applicationPosition);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.details, menu);
		menu.findItem(R.id.menuFilter).setVisible(false);
		
     this.menu = menu;
     setMenuFromApplication();
     
     return super.onCreateOptionsMenu(menu);
    }
    
	
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case R.id.blacklistDetailsAddMenuItem:
            ApplicationExceptionList.getInstance().addEntry(appDetailsFrag.getLoadedApplication().getName(),ListType.BLACKLIST);
            setMenuFromApplication();
            appDetailsFrag.getLoadedApplication().setThreatLevel(appDetailsFrag.getLoadedApplication().calculateThreat());
            ApplicationExceptionList.getInstance().saveToMemory(this);
            return true;
        case R.id.blacklistDetailsRemoveMenuItem:
        	ApplicationExceptionList.getInstance().removeEntry(appDetailsFrag.getLoadedApplication().getName());
        	setMenuFromApplication();
        	appDetailsFrag.getLoadedApplication().setThreatLevel(appDetailsFrag.getLoadedApplication().calculateThreat());
        	ApplicationExceptionList.getInstance().saveToMemory(this);
        	return true;
        case R.id.whitelistDetailsAddMenuItem:
        	ApplicationExceptionList.getInstance().addEntry(appDetailsFrag.getLoadedApplication().getName(),ListType.WHITELIST);
        	setMenuFromApplication();
        	appDetailsFrag.getLoadedApplication().setThreatLevel(appDetailsFrag.getLoadedApplication().calculateThreat());
        	ApplicationExceptionList.getInstance().saveToMemory(this);
        	return true;
        case R.id.whitelistDetailsRemoveMenuItem:
        	ApplicationExceptionList.getInstance().removeEntry(appDetailsFrag.getLoadedApplication().getName());
        	setMenuFromApplication();
        	appDetailsFrag.getLoadedApplication().setThreatLevel(appDetailsFrag.getLoadedApplication().calculateThreat());
        	ApplicationExceptionList.getInstance().saveToMemory(this);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void setMenuFromApplication()
    {
    	if(appDetailsFrag != null && appDetailsFrag.getLoadedApplication() != null)
    	{
    	@SuppressWarnings("unchecked")
		ListEntry<String> entry = (ListEntry<String>)(ApplicationExceptionList.getInstance().findEntry(appDetailsFrag.getLoadedApplication().getName()));
    	
    	((MenuItem)menu.findItem(R.id.whitelistDetailsRemoveMenuItem)).setVisible(false);
    	((MenuItem)menu.findItem(R.id.blacklistDetailsRemoveMenuItem)).setVisible(false);
    	((MenuItem)menu.findItem(R.id.blacklistDetailsAddMenuItem)).setVisible(false);
		((MenuItem)menu.findItem(R.id.whitelistDetailsAddMenuItem)).setVisible(false);
		 if(entry != null && (entry.getEntryValue().equals(ListType.WHITELIST) || entry.getEntryValue().equals(ListType.BLACKLIST)))
		 {
			 if(entry.getEntryValue().equals(ListType.WHITELIST))
			 {
				 ((MenuItem)menu.findItem(R.id.whitelistDetailsRemoveMenuItem)).setVisible(true);
			 }
			 else
			 {
				 ((MenuItem)menu.findItem(R.id.blacklistDetailsRemoveMenuItem)).setVisible(true);
			 }
		 }
		 else
		 {
			 ((MenuItem)menu.findItem(R.id.blacklistDetailsAddMenuItem)).setVisible(true);
			 ((MenuItem)menu.findItem(R.id.whitelistDetailsAddMenuItem)).setVisible(true);
		 }
    }
    }

}
