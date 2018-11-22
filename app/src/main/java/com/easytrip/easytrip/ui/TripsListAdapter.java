package com.easytrip.easytrip.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.activities.MainPageActivity;
import com.easytrip.easytrip.bl.Trip;

import java.util.ArrayList;

import static com.easytrip.easytrip.utils.IConstants.DAYS;
import static com.easytrip.easytrip.utils.IConstants.TRIP_TO;

public class TripsListAdapter extends BaseAdapter {

    private Context         context;
    private ArrayList<Trip> tripsList;
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
            viewHolder.place = convertView.findViewById(R.id.col_place);
            viewHolder.detailsBtn = convertView.findViewById(R.id.col_details);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        final Trip trip = tripsList.get(position);
        viewHolder.place.setText(TRIP_TO + trip.getDestination() + " - " + trip.getDays() + DAYS);
        viewHolder.detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ( (MainPageActivity) context ).showTrip(trip);
            }
        });
       //set image
        return convertView;
    }

    static class ViewHolder {
        private TextView place;
        private Button detailsBtn;

    }
}