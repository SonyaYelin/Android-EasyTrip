package com.easytrip.easytrip.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.easytrip.easytrip.ProfileActivity;
import com.easytrip.easytrip.R;
import com.easytrip.easytrip.SelectPOIActivity;
import com.easytrip.easytrip.ShowTripActivity;
import com.easytrip.easytrip.bl.Trip;
import com.easytrip.easytrip.bl.Venue;

import java.util.ArrayList;

import static com.easytrip.easytrip.IConstants.TRIP;

public class TripsListAdapter extends BaseAdapter {

    private Context         context;
    private ArrayList<Trip>  tripsList;
    private LayoutInflater  inflater;
    private ViewHolder      viewHolder;

    public TripsListAdapter(Context context, ArrayList<Trip> tripsList) {
        this.context = context;
        this.tripsList = tripsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tripsList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate((R.layout.trips_list_item), parent, false);
            viewHolder = new ViewHolder();
            viewHolder.placeTextView = convertView.findViewById(R.id.col_place);
            viewHolder.daysTextView = convertView.findViewById(R.id.col_days);
            viewHolder.details = convertView.findViewById(R.id.btn_details);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        final Trip trip = tripsList.get(position);
        viewHolder.placeTextView.setText(trip.getDestination());
        viewHolder.daysTextView.setText(trip.getDays()+"");
        viewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ( (ProfileActivity) context ).showTrip(trip);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private TextView placeTextView;
        private TextView daysTextView;
        private Button details;
    }
}