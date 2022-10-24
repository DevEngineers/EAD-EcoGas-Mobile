package com.example.ecogas;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.ecogas.Model.Station;
import com.example.ecogas.ViewAdapters.StationAdapter;
import java.util.ArrayList;

public class ViewAllStation extends AppCompatActivity {

    public static ArrayList<Station> stationsList = new ArrayList<Station>();

    private ListView listView;
   
    Button petrol92Btn, petrol95Btn, dieselBtn, superDieselBtn;

    private int white, darkGray, red;

    private final ArrayList<String> selectedFilters = new ArrayList<String>();
    private String currentSearchText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stations);

//        initSearchWidgets();
        initWidgets();
        setUpData();
        setUpList();
        setUpOnClickListener();
        initColors();
//        lookSelected(allButton);
        selectedFilters.add("all");
    }

    private void initColors() {
        white = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        red = ContextCompat.getColor(getApplicationContext(), R.color.red);
        darkGray = ContextCompat.getColor(getApplicationContext(), R.color.darkerGray);
    }

    private void unSelectAllFilterButtons() {
        lookUnSelected(petrol92Btn);
        lookUnSelected(petrol95Btn);
        lookUnSelected(dieselBtn);
        lookUnSelected(superDieselBtn);
    }

    private void lookSelected(Button parsedButton) {
        parsedButton.setTextColor(white);
        parsedButton.setBackgroundColor(red);
    }

    private void lookUnSelected(Button parsedButton) {
        parsedButton.setTextColor(red);
        parsedButton.setBackgroundColor(darkGray);
    }

    private void initWidgets() {
//        petrol92Btn = findViewById(R.id.petrol92Btn);
//        petrol95Btn = findViewById(R.id.petrol95Btn);
//        dieselBtn = findViewById(R.id.dieselBtn);
        superDieselBtn = findViewById(R.id.superDieselBtn);
    }

//    private void initSearchWidgets() {
//        SearchView searchView = (SearchView) findViewById(R.id.shapeListSearchView);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                currentSearchText = s;
//                ArrayList<Station> filteredStations = new ArrayList<Station>();
//
//                for (Station station : stationsList) {
//                    if (station.getStationName().toLowerCase().contains(s.toLowerCase())) {
//                        if (selectedFilters.equals("all")) {
//                            filteredStations.add(station);
//                        } else {
//                            for (String filter : selectedFilters) {
//                                if (station.getStationName().toLowerCase().contains(filter)) {
//                                    filteredStations.add(station);
//                                }
//                            }
//                        }
//                    }
//                }
//                setStationAdapter(filteredStations);
//
//                return false;
//            }
//        });
//    }

    private void setUpData() {
        Station st1 = new Station("0", "IOC", "Colombo10");
        stationsList.add(st1);
        Station st2 = new Station("1", "Name 02", "Colombo01");
        stationsList.add(st2);
        Station st3 = new Station("2", "Samantha Shed", "Colombo10");
        stationsList.add(st3);
        Station st4 = new Station("3", "Name IOC", "Kandy");
        stationsList.add(st4);
        Station st5 = new Station("4", "Lanka shed", "Colombo01");
        stationsList.add(st5);
        Station st6 = new Station("5", "Name IOC 05", "Kandy");
        stationsList.add(st6);
        Station st7 = new Station("6", "Lanka 04", "Kandy");
        stationsList.add(st7);
    }

    private void setUpList() {
        listView = (ListView) findViewById(R.id.stationsListView);

        setStationAdapter(stationsList);
    }

    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Station selectStation = (Station) (listView.getItemAtPosition(position));
                Intent showDetail = new Intent(getApplicationContext(), DetailStation.class);
                showDetail.putExtra("id", selectStation.getStationID());
                startActivity(showDetail);
            }
        });
    }

    private void filterList(String status) {
        if (status != null && !selectedFilters.contains(status))
            selectedFilters.add(status);

        ArrayList<Station> filteredStations = new ArrayList<Station>();

        for (Station station : stationsList) {
            if (station.getStationName().toLowerCase().contains(status)) {
                if (currentSearchText == "") {
                    filteredStations.add(station);
                } else {
                    if (station.getStationName().toLowerCase().contains(currentSearchText.toLowerCase())) {
                        filteredStations.add(station);
                    }
                }
            }
        }

        setStationAdapter(filteredStations);
    }


    public void petrol92Tapped(View view) {
        selectedFilters.clear();
        filterList("ioc");

        unSelectAllFilterButtons();
        lookSelected(petrol92Btn);
    }

    public void petrol95Tapped(View view) {
        selectedFilters.clear();
        filterList("petrol 95");

        unSelectAllFilterButtons();
        lookSelected(petrol95Btn);
    }

    public void dieselTapped(View view) {
        selectedFilters.clear();
        filterList("Lanka");

        unSelectAllFilterButtons();
        lookSelected(dieselBtn);
    }

    public void superDieselTapped(View view) {
        selectedFilters.clear();
        filterList("Name");

        unSelectAllFilterButtons();
        lookSelected(superDieselBtn);
    }


    private void setStationAdapter(ArrayList<Station> stationsList) {
        StationAdapter stationAdapter = new StationAdapter(getApplicationContext(), 0, stationsList);
        listView.setAdapter(stationAdapter);
    }

    public void searchTapped(View view) {
    }
}