package com.Lumension.android.permission_scanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/** The fragment that displays the details of an application and allows opening of the applications information screen
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
public class ApplicationDetailFragment extends Fragment implements OnClickListener{

    /** Called when the activity is starting
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this
     *                           Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Defaults to null
     */
	
	/**
	 * The view for this fragment
	 */
	private View view;
	
	private Application thisApp;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

    	view = inflater.inflate(R.layout.application_details,container,false);
    	
    	return view;
    }
    
    /**
     * Loads an application into this fragment
     * @param index- The index of the application in Application List
     */
    public void loadApplication(int index)
    {
        thisApp = ApplicationList.getApplication(index);

        ((ImageView)view.findViewById(R.id.applicationDetailIcon)).setImageDrawable(thisApp.getIcon());
        ((TextView)view.findViewById(R.id.applicationDetailName)).setText(thisApp.getLabel());
        ((TextView)view.findViewById(R.id.applicationDetailVersion)).setText("Version: " + thisApp.getVersionCode());
        ((TextView)view.findViewById(R.id.applicationDetailThreat)).setText(thisApp.getThreatDescription());
        ((TextView)view.findViewById(R.id.applicationDetailActivityLayout)).setText("Permission Count: " + thisApp.getPermissions().size());
        ((TextView)view.findViewById(R.id.applicationDetailsDownloads)).setText(thisApp.getDownloadDescription()+ " downloads");
        ((RatingBar)view.findViewById(R.id.applicationDetailsStars)).setRating(thisApp.getStars());
        ((Button)view.findViewById(R.id.appicationDetailsManage)).setOnClickListener(this);
        
        ((RatingBar)view.findViewById(R.id.applicationDetailsStars)).setVisibility(View.VISIBLE);
        ((Button)view.findViewById(R.id.appicationDetailsManage)).setVisibility(View.VISIBLE);
        
        ListView applicationDetailListView = (ListView)view.findViewById(R.id.applicationDetailList);

        List<String> testList = new ArrayList<String>();
        for( String row: thisApp.getPermissions()) {
            testList.add(row);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.application_details_list_item, testList);
        applicationDetailListView.setAdapter(arrayAdapter);
    }

    /**
     * Called by the manage button, opens the application information screen
     */
	@Override
	public void onClick(View v) {
		startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+thisApp.getName())));
	}
}
