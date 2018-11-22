package com.easytrip.easytrip.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.Venue;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;

public class VenuesRecyclerAdapter extends RecyclerView.Adapter<VenuesRecyclerAdapter.ViewHolder> {

  private Context        context;
  private List<Venue>    pointsList;
  private HashSet<Venue> selectedPoints;


  public VenuesRecyclerAdapter(List<Venue> points, HashSet<Venue> selectedPoints, Context context) {
    this.pointsList = points;
    this.selectedPoints = selectedPoints;
    this.context = context;
  }


  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_recycler_row, parent, false);
    return new ViewHolder(rowView);
  }

  @Override
  public int getItemCount() {
    return pointsList.size();
  }

  @Override
  public void onBindViewHolder(final ViewHolder viewHolder, int position) {


    final Venue selectedPoint = pointsList.get(position);

    viewHolder.placeName.setText( selectedPoint.getName() );
    viewHolder.placeDescription.setText( selectedPoint.getDescription() );

    viewHolder.checkBox.setOnCheckedChangeListener(null);
    viewHolder.checkBox.setChecked( selectedPoints.contains(selectedPoint) );
    viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if ( isChecked )
          selectedPoints.add(selectedPoint);
        else
          selectedPoints.remove(selectedPoint);
      }
    });

    Picasso.with(context).load(selectedPoint.getIcon())
            .networkPolicy(NetworkPolicy.OFFLINE).fit().into(viewHolder.icon, new Callback() {
      @Override
      public void onSuccess() {
      }
      @Override
      public void onError() {
        //Try again online if cache failed
        Picasso.with(context)
                .load(selectedPoint.getIcon())
                .into(viewHolder.icon, new Callback() {
                  @Override
                  public void onSuccess() {}
                  @Override
                  public void onError() {}
                });
      }
    });
  }


  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView  placeName;
    TextView  placeDescription;
    CheckBox  checkBox;
    ImageView icon;

    public ViewHolder(final View itemView) {
      super(itemView);

      placeName = (TextView) itemView.findViewById(R.id.place_name);
      placeDescription = (TextView) itemView.findViewById(R.id.place_description);
      icon = (ImageView) itemView.findViewById(R.id.place_icon);
      checkBox = (CheckBox) itemView.findViewById(R.id.place_checkBox);
    }
  }
}