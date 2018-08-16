package com.easytrip.easytrip.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.SelectPOIActivity;
import com.easytrip.easytrip.bl.Venue;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VenueRecyclerAdapter extends RecyclerView.Adapter<VenueRecyclerAdapter.ViewHolder> {

  private Context           context;
  private List<Venue>       venues;


  public VenueRecyclerAdapter(List<Venue> VenueList, Context context) {
    this.venues = VenueList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_recycler_row, parent, false);
    return new ViewHolder(rowView);
  }

  @Override
  public int getItemCount() {
    return venues.size();
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

    final Venue selectedVenue = venues.get(position);
    final ViewHolder finalHolder = holder;

    String name = selectedVenue.getName();
    holder.name.setText( name );
    holder.details.setText(selectedVenue.getDescription());
    holder.checkBox.setOnCheckedChangeListener(null);
    holder.checkBox.setChecked(  selectedVenue.isSelected() );
    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ((SelectPOIActivity) VenueRecyclerAdapter.this.context).updateSelectedVenues(isChecked, selectedVenue);
        selectedVenue.setSelected(isChecked);
      }
    });

    Picasso.with(context).load(selectedVenue.getIcon())
            .networkPolicy(NetworkPolicy.OFFLINE).fit().into(finalHolder.icon, new Callback() {
      @Override
      public void onSuccess() {
      }
      @Override
      public void onError() {
        //Try again online if cache failed
        Picasso.with(context)
                .load(selectedVenue.getIcon())
                .into(finalHolder.icon, new Callback() {
                  @Override
                  public void onSuccess() {}
                  @Override
                  public void onError() {}
                });
      }
    });
  }


  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView details;
    ImageView icon;
    CheckBox checkBox;

    public ViewHolder(final View itemView) {
      super(itemView);

      details = (TextView) itemView.findViewById(R.id.venue_details);
      name = (TextView) itemView.findViewById(R.id.venue_name);
      icon = (ImageView) itemView.findViewById(R.id.venue_icon);
      checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
    }
  }
}