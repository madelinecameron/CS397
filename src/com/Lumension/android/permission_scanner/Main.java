package com.Lumension.android.permission_scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.pm.PackageManager;

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

public class Main extends Activity {
    /** Local ApplicationList object instance*/
    ApplicationList applicationList;

    /** Local UpdateDB object instance */
    UpdateDB updateDB;


    /** Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this
     *                           Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Defaults to null
     */
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

        PackageManager pm = getPackageManager();
        applicationList = ApplicationList.getInstance(pm);

        ListView ApplicationListView;

        ApplicationListView = (ListView) findViewById(R.id.applicationlistview);
        ApplicationViewAdapter adapter = new ApplicationViewAdapter(this, applicationList.AppList);
        ApplicationListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ApplicationDetail.class);
                intent.putExtra("application_position", position);
                startActivity(intent);
            }
        });
        ApplicationListView.setAdapter(adapter);

        updateDB = UpdateDB.getInstance();
    }

    /**
     * Called when the Activity is being closed
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        // Do any clean-up
    }
}
