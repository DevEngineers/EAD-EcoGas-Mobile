package com.example.ecogas.ViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.ecogas.Model.Station;
import com.example.ecogas.R;
import java.util.List;

public class StationAdapter extends ArrayAdapter<Station> {

    public StationAdapter(Context context, int resource, List<Station> stationsList) {
        super(context, resource, stationsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Station station = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.station_cell, parent, false);
        }
        TextView StId = convertView.findViewById(R.id.StCodeText);
        TextView StName = convertView.findViewById(R.id.StNameText);
        TextView StLocation = convertView.findViewById(R.id.StLocationText);

        StId.setText(station.getStationID());
        StName.setText(station.getStationName());
        StLocation.setText(station.getStationLocation());

        return convertView;
    }
}
