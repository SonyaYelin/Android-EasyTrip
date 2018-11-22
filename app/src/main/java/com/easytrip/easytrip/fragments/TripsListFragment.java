package com.easytrip.easytrip.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.ui.TripProgressDialog;
import com.easytrip.easytrip.ui.TripsListAdapter;

import java.util.ArrayList;

import static com.easytrip.easytrip.utils.IConstants.NO_INTERNET;

public class TripsListFragment extends Fragment {

    private ArrayList<Trip>     tripsList = new ArrayList<>();
    private ListView            tripsListView;
    private TripsListAdapter    tripsAdapter;

    public TripsListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        tripsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.getFocusables(position);
                view.setSelected(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips_table, container, false);

        tripsListView = view.findViewById(R.id.trips_list);
        tripsAdapter = new TripsListAdapter(view.getContext(), tripsList);
        tripsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tripsListView.setAdapter(tripsAdapter);

        return view;
    }

    public void showTable(ArrayList<Trip > tripsList, Context context){
        TripProgressDialog.getInstance().dismiss(context);

        this.tripsList.clear();
        if(tripsList.size()>0)
            this.tripsList.addAll(tripsList);
        if (tripsAdapter != null )
            tripsAdapter.notifyDataSetChanged();

    }
}