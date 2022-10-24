package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.ecogas.Model.Station;
import java.util.ArrayList;

public class ViewStations extends AppCompatActivity {

    public static ArrayList<Station> stationsList = new ArrayList<Station>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stations);
    }

    public void searchTapped(View view) {

    }
}