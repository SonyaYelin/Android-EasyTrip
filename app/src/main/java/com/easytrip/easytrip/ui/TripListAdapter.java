package com.easytrip.easytrip.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.Venue;

import java.util.ArrayList;

public class TripListAdapter extends BaseAdapter {

    private Context          context;
    private ArrayList<Venue> tripList;
    private LayoutInflater  inflater;
    private ViewHolder      viewHolder;


    public TripListAdapter(Context context, ArrayList<Venue> tripList) {
        this.context = context;
        this.tripList = tripList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate((R.layout.trip_list_item), parent, false);
            viewHolder = new ViewHolder();
            viewHolder.placeTextView = convertView.findViewById(R.id.col_place);
            viewHolder.dayTextView = convertView.findViewById(R.id.col_time);
            viewHolder.detailsTextView = convertView.findViewById(R.id.col_details);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        Venue venue = tripList.get(position);
        viewHolder.placeTextView.setText( venue.getName() );
        viewHolder.dayTextView.setText( venue.getDay() + "" );
        viewHolder.detailsTextView.setText( venue.getDescription() );
        return convertView;
    }

    static class ViewHolder {
        private TextView placeTextView;
        private TextView dayTextView;
        private TextView detailsTextView;
    }
}