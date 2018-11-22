package com.easytrip.easytrip.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Venue;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripListAdapter extends BaseAdapter {

    private ArrayList<Venue>    tripList;
    private LayoutInflater      inflater;
    private ViewHolder          viewHolder;


    public TripListAdapter(Context context, ArrayList<Venue> tripList) {

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
            viewHolder.circleImageView = convertView.findViewById(R.id.place_icon);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        final Venue venue = tripList.get(position);
        viewHolder.placeTextView.setText( venue.getName() );

        Picasso.with(convertView.getContext()).load(venue.getIcon())
                .networkPolicy(NetworkPolicy.OFFLINE).fit().into(viewHolder.circleImageView, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private TextView        placeTextView;
        private CircleImageView circleImageView;
    }
}