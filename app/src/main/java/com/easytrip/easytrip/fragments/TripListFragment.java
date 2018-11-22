package com.easytrip.easytrip.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Venue;
import com.easytrip.easytrip.ui.TripListAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

public class TripListFragment extends Fragment {

    private ListView            tripList;
    private TripListAdapter     tripAdapter;
    private ArrayList<Venue>      points = new ArrayList<>();


    public TripListFragment() {

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
        tripAdapter = new TripListAdapter( view.getContext(), points);
        tripList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tripList.setAdapter(tripAdapter);
        return view;
    }

    public void showTable(ArrayList<Venue> points){
        this.points.clear();
        if(points.size() > 0)
            this.points.addAll(points);
        if (tripAdapter != null )
            tripAdapter.notifyDataSetChanged();
    }
}