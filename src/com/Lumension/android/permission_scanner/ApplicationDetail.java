package com.Lumension.android.permission_scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/** An activity object to be used with the Application detail view
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

public class ApplicationDetail extends Activity {

    /** Called when the activity is starting
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this
     *                           Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Defaults to null
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.application_details);

        Intent intent = getIntent();
        Integer applicationPosition = (Integer)intent.getSerializableExtra("application_position");

        ApplicationList appList = ApplicationList.getInstance(getPackageManager());
        Application app = appList.getApplication(applicationPosition);

        ((ImageView)findViewById(R.id.application_detail_icon)).setImageDrawable(app.getIcon());
        ((TextView)findViewById(R.id.application_detail_name)).setText(app.getLabel());
        ((TextView)findViewById(R.id.application_detail_version)).setText("Version: " + app.getVersionCode());
        ((TextView)findViewById(R.id.application_detail_threat)).setText(app.getThreatDescription());
        ((TextView)findViewById(R.id.application_detail_permission_count)).setText("Permission Count: " + app.getPermissions().size());
        ListView applicationDetailListView = (ListView)findViewById(R.id.application_detail_list);

        List<String> testList = new ArrayList<String>();
        for( String row: app.getPermissions()) {
            testList.add(row);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.application_details_list_item, testList);
        applicationDetailListView.setAdapter(arrayAdapter);
    }
}
