package com.example.ecogas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecogas.Model.Station;

public class DetailStation extends AppCompatActivity {

    Station selectedStation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_detail);
        getSelectedStation();
        setValues();

    }

    private void getSelectedStation()
    {
        Intent previousIntent = getIntent();
        String parsedStringID = previousIntent.getStringExtra("id");
        selectedStation = ViewAllStation.stationsList.get(Integer.valueOf(parsedStringID));
    }

    private void setValues()
    {
        TextView StId = (TextView) findViewById(R.id.StCodeText);
        TextView StName = (TextView) findViewById(R.id.StNameText);
        TextView StLocation = (TextView) findViewById(R.id.StLocationText);

        StId.setText(selectedStation.getStationID());
        StName.setText(selectedStation.getStationName());
        StLocation.setText(selectedStation.getStationLocation());
    }

}
