package com.example.ecogas.ViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecogas.R;

import java.util.ArrayList;

/**
 * This adapter class is to set and initiate the station details in recycle view to view in AdminViewStations screen
 *
 * Author: IT19153414 Akeel M.N.M
 */

public class AdminStationRecycleViewAdepter extends RecyclerView.Adapter<AdminStationRecycleViewAdepter.ViewHolder> {

    private ArrayList<String> stationName = new ArrayList<>();
    private ArrayList<String> ownerName = new ArrayList<>();
    private ArrayList<String> location = new ArrayList<>();


    private Context mContext;

    public AdminStationRecycleViewAdepter(ArrayList<String> stationName, ArrayList<String> ownerName, ArrayList<String> location, Context mContext) {
        this.stationName = stationName;
        this.ownerName = ownerName;
        this.location = location;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_station_view_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.name.setText(stationName.get(position));
        holder.owner.setText(ownerName.get(position));
        holder.location.setText(location.get(position));
    }

    @Override
    public int getItemCount() {
        return stationName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,owner,location;
        RelativeLayout adminStationLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.stationViewName);
            owner=itemView.findViewById(R.id.stationViewOwner);
            location=itemView.findViewById(R.id.stationViewLocation);
            adminStationLayout=itemView.findViewById(R.id.adminStationViewLayout);
        }
    }

}