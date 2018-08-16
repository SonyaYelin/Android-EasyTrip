package com.easytrip.easytrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.easytrip.easytrip.ui.TripListAdapter;
import com.easytrip.easytrip.bl.Venue;

import java.util.ArrayList;
import java.util.LinkedList;

public class TripTableFragment extends Fragment {

    private ListView            tripList;
    private TripListAdapter     tripAdapter;
    private ArrayList<Venue>    venues = new ArrayList<>();


    public TripTableFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.getFocusables(position);
                view.setSelected(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_table, container, false);
        tripList = view.findViewById(R.id.trip_list);
        tripAdapter = new TripListAdapter(getContext(), venues);
        tripList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tripList.setAdapter(tripAdapter);
        return view;
    }

    public void showTable(LinkedList<Venue> venues){
        this.venues.clear();
        if(venues.size()>0)
            this.venues.addAll(venues);
        if (tripAdapter != null )
            tripAdapter.notifyDataSetChanged();
    }
}