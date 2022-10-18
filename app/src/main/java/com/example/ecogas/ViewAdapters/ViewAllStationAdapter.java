package com.example.ecogas.ViewAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecogas.R;

public class ViewAllStationAdapter extends RecyclerView.Adapter<ViewAllStationAdapter.ViewHolder> {

    String[] StationCode , StationName, StationLocation;

    public ViewAllStationAdapter(String []StationCode, String []StationName, String []StationLocation) {
        this.StationCode = StationCode;
        this.StationName = StationName;
        this.StationLocation = StationLocation;
    }

    @NonNull
    @Override
    public ViewAllStationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /** Create view from xml layout(layoutList)**/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station,parent,false);

        /** Create ViewHolder & return View Holder **/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllStationAdapter.ViewHolder holder, int position) {

        holder.stationCode.setText(StationCode.length);
        holder.stationName.setText(StationName.length);
        holder.stationLocation.setText(StationLocation.length);
    }

    @Override
    public int getItemCount() {
        return StationCode.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView stationCode,stationName,stationLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /** Accessing the Views and Button of Recycle View **/
            stationCode = itemView.findViewById(R.id.StationCode);
            stationName = itemView.findViewById(R.id.StationName);
            stationLocation = itemView.findViewById(R.id.StationLocation);
            itemView.findViewById(R.id.Station).setOnClickListener(view -> {

            });

        }
    }
}
