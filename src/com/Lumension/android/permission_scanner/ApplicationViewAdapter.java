package com.Lumension.android.permission_scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * View adapter for application view display. Builds lines for the ListView to display
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

public class ApplicationViewAdapter extends ArrayAdapter<Application> implements
		Filterable {
	/**
	 * Instance of Context object required for accessing application-specific
	 * resources
	 */
	Context context;

	/** Local variable for the Application List */
	private List<Application> appList;
	
	/**
	 * Contains the list of applications to display, after being filtered and sorted.
	 */
	private static List<Application> filteredApps = new ArrayList<Application>();;

	/**
	 * Constructor for the ApplicationViewAdapter object
	 * 
	 * @param c
	 *            The Context object to be used
	 * @param appList
	 *            The list of applications to display
	 */
	ApplicationViewAdapter(Context c, List<Application> appList) {
		super(c, R.layout.application_list_item, R.id.applicationListView,
				filteredApps);
		this.context = c;
		this.appList = appList;

		filteredApps = new ArrayList<Application>();
		rebuildList();
	}
	
	/**
	 * Invalidates the list being displayed, causing the ListView to rebuild entirely, necessary because it is both filtered and sorted
	 * otherwise Android could do this for us.
	 */
	public void rebuildList() {
		clear();
		filteredApps = new ArrayList<Application>();
		filteredApps.clear();
		for (Application app : appList) {
			filteredApps.add(app);
		}

		Comparator<Application> comparator = new Comparator<Application>() {
			@Override
			public int compare(Application lhs, Application rhs) {
				return rhs.getThreatLevel() - lhs.getThreatLevel();
			}
		};

		Collections.sort(filteredApps, comparator);

		for (Application app : filteredApps) {
			add(app);
		}

	}

	/**
	 * Get the Application object at the requested position
	 * 
	 * @param position
	 *            The index of the requested Application
	 * @return The Application object at the specified index
	 */
	public Application getItem(int position) {
		return filteredApps.get(position);
	}

	/**
	 * Dynamically creates the view
	 * 
	 * @param position
	 *            The index of the application
	 * @param convertView
	 *            The view to inflate
	 * @param parent
	 *            The parent view of the view to inflate
	 * @return The dynamically created view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.application_list_item, parent,
					false);
		}

		ImageView myImage = (ImageView) row
				.findViewById(R.id.applicationListViewImage);
		TextView myTitle = (TextView) row
				.findViewById(R.id.applicationListViewName);
		TextView myThreatLevel = (TextView) row
				.findViewById(R.id.applicationListViewThreatLevel);
		ImageView myCameraIcon = (ImageView) row
				.findViewById(R.id.applicationListViewCameraIcon);
		View colorView = (View) row.findViewById(R.id.colorView);

		myCameraIcon.setVisibility(filteredApps.get(position).usesCamera() ? 0
				: 8);
		myImage.setImageDrawable(filteredApps.get(position).getIcon());
		myTitle.setText(filteredApps.get(position).getLabel());
		myThreatLevel
				.setText(filteredApps.get(position).getThreatDescription());
		colorView.setBackgroundColor(filteredApps.get(position)
				.getThreatColor());

		// myTitle.setTextColor(context.getResources().getColor(R.color.noThreatText));
		// myThreatLevel.setTextColor(context.getResources().getColor(R.color.noThreatText));
		return row;
	}

	/**
	 * The filter for the list of applications. Removes applications that do not
	 * contain the constraint, case insensitive
	 */
	@Override
	public Filter getFilter() {

		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				// filteredApps = (List<Application>)results.values;
				Comparator<Application> comparator = new Comparator<Application>() {
					@Override
					public int compare(Application lhs, Application rhs) {
						return rhs.getThreatLevel() - lhs.getThreatLevel();
					}
				};

				Collections
						.sort((List<Application>) results.values, comparator);
				clear();
				filteredApps.clear();
				for (Application app : (List<Application>) results.values) {
					filteredApps.add(app);
					add(app);
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				List<Application> filterList = new ArrayList<Application>();

				for (Application app : appList) {
					if (app.getLabel().toLowerCase()
							.contains(constraint.toString().toLowerCase())) {
						filterList.add(app);
					}
				}
				results.count = filterList.size();
				results.values = filterList;

				return results;
			}
		};

		return filter;
	}
}
