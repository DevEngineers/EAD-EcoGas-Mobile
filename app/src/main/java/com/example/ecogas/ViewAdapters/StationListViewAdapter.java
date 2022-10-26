package com.example.ecogas.ViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.ecogas.Model.Station;
import com.example.ecogas.R;
import java.util.List;

public class StationListViewAdapter extends ArrayAdapter<Station> {

    public StationListViewAdapter(Context context, int resource, List<Station> stationsList) {
        super(context, resource, stationsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Station station = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.station_layout, parent, false);
        }
        TextView StName = convertView.findViewById(R.id.StNameText);
        TextView StLocation = convertView.findViewById(R.id.StLocationText);
        TextView StID = convertView.findViewById(R.id.stID);

        StName.setText(station.getStationName());
        StLocation.setText(station.getLocation());
        StID.setText(station.getStationID());
        Log.d("TAG","Atepter............................................ . " +station.getOwnerName() );

        return convertView;
    }

}
