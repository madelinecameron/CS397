package com.Lumension.android.permission_scanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * The fragment that displays a list of applications The activity hosting this
 * fragment must implement an OnItemClickListener so it can take selections from
 * this fragment.
 * 
 * @author Ean Lombardo
 * 
 */
public class ApplicationListFragment extends Fragment {

	ListView applicationListView;
	ApplicationViewAdapter adapter;

	/**
	 * Creates the fragment. Loading the ApplicationViewAdapter with it's data and giving it to the listview.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.application_list_fragment,
				container, false);

		applicationListView = (ListView) view
				.findViewById(R.id.applicationListView);
		adapter = new ApplicationViewAdapter(getActivity(),
				ApplicationList.appList);

		if (getActivity() instanceof OnItemClickListener) {
			applicationListView
					.setOnItemClickListener((OnItemClickListener) getActivity());
		}
		applicationListView.setAdapter(adapter);
		return view;
	}

	/**
	 * Resorts the list, recreating all of the lines in the list
	 */
	public void resetList() {
		((ApplicationViewAdapter) applicationListView.getAdapter())
				.rebuildList();
	}

	/**
	 * Sets the filter to filter the list by
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		adapter.getFilter().filter(filter);
	}
}