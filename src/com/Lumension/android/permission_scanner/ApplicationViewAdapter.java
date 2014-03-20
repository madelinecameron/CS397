package com.Lumension.android.permission_scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** View adapter for application view display
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

public class ApplicationViewAdapter extends ArrayAdapter<Application>{
    /** Instance of Context object required for accessing application-specific resources */
    Context context;

    /** Local variable for the Application List */
    private List<Application> AppList;

    /** Constructor for the ApplicationViewAdapter object
     *
     * @param c The Context object to be used
     * @param appList The list of applications to display
     */
    ApplicationViewAdapter(Context c, List<Application> appList) {
        super(c, R.layout.application_list_item, R.id.applicationlistview, appList);
        this.context = c;
        this.AppList = appList;

        Comparator<Application> comparator = new Comparator<Application>() {
            @Override
            public int compare(Application lhs, Application rhs) {
                return rhs.getThreatLevel() - lhs.getThreatLevel();
            }
        };

        Collections.sort(AppList, comparator);
    }

    /** Get the Application object at the requested position
     *
     * @param position The index of the requested Application
     * @return The Application object at the specified index
     */
    public Application getItem(int position) {
        return AppList.get(position);
    }

    /** Dynamically creates the view
     *
     * @param position The index of the application
     * @param convertView The view to inflate
     * @param parent The parent view of the view to inflate
     * @return The dynamically created view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.application_list_item, parent, false);
        }

        ImageView myImage = (ImageView) row.findViewById(R.id.applicationlistviewimage);
        TextView myTitle = (TextView) row.findViewById(R.id.applicationlistviewname);
        TextView myThreatLevel = (TextView) row.findViewById(R.id.applicationlistviewthreatlevel);
        ImageView myCameraIcon = (ImageView) row.findViewById(R.id.applicationlistviewcameraicon);
       
        myCameraIcon.setVisibility(AppList.get(position).usesCamera()?0:8);
        myImage.setImageDrawable(AppList.get(position).getIcon());
        myTitle.setText(AppList.get(position).getLabel());
        myThreatLevel.setText(AppList.get(position).getThreatDescription());
        row.setBackgroundColor(AppList.get(position).getThreatColor());
        myTitle.setTextColor(context.getResources().getColor(R.color.no_threat_text));
        myThreatLevel.setTextColor(context.getResources().getColor(R.color.no_threat_text));

        return row;
    }
}
