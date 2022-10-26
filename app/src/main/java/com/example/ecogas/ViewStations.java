package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ecogas.Model.Queues;
import com.example.ecogas.Model.Station;
import com.example.ecogas.Service.QueueService;
import com.example.ecogas.Service.StationService;
import com.example.ecogas.ViewAdapters.StationListViewAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: IT19175126 Zumry A.M
 */

public class ViewStations extends AppCompatActivity {

    public static ArrayList<Station> stationsList = new ArrayList<Station>();
    List<String> locationList = new ArrayList<String>();
    String selectedLocation;
    private ListView listView;
    TextView StNameQ, FuelTypeQ, noQueue, TimeQ;
    LinearLayout joinedQueueLayout;
    Button btnSearch, btnRemoveQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stations);

        joinedQueueLayout = findViewById(R.id.joinedQueueLayout);
        btnSearch = findViewById(R.id.searchBtn);
        StNameQ = findViewById(R.id.stNameQ);
        FuelTypeQ = findViewById(R.id.fuelTypeQ);
        noQueue = findViewById(R.id.noQueueQ);
        TimeQ = findViewById(R.id.jointTimeQ);
        btnRemoveQueue = findViewById(R.id.removeQueueBtn);
        Spinner fuelLocationSpinner = findViewById(R.id.spinnerLocation);

        initImageBitmaps();

        if(checkUserJoinedQueue()){
            joinedQueueLayout.setVisibility(View.GONE);
        }

         /** Setting data to view fuel types in spinner for select fuel type **/
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewStations.this, android.R.layout.simple_spinner_item, locationList);
        fuelLocationSpinner.setPrompt("Location");
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        fuelLocationSpinner.setAdapter(arrayAdapter);

        /** Listener to capture the selected data on spinner **/
        fuelLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLocation = adapterView.getSelectedItem().toString();
                Toast.makeText(ViewStations.this, "loca :" + selectedLocation, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSearch.setOnClickListener(view -> {
            Toast.makeText(this, "on Btnsearch : " + selectedLocation , Toast.LENGTH_SHORT).show();
            ArrayList<Station> filteredStation = new ArrayList<Station>();
            for(Station station:stationsList){
                if (station.getLocation().equals(selectedLocation)){
                    filteredStation.add(station);
                    Toast.makeText(this, "Select Location : " + selectedLocation , Toast.LENGTH_SHORT).show();
                    Log.d("TAG" , "000000000-------------------Selected location ...................... " + filteredStation.indexOf(station.getLocation()));
                }
            }
        });

        btnRemoveQueue.setOnClickListener(v -> {
            Toast.makeText(this, "on Btn Remove" , Toast.LENGTH_SHORT).show();

        });


    }

    private boolean checkUserJoinedQueue() {
        final boolean[] isExist = {false};
        /** Api call to retrieve the details of the Queue by User ID**/
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()+"Queue/").addConverterFactory(GsonConverterFactory.create()).build();
        QueueService queueService = retrofit.create(QueueService.class);
        Call<Queues> call = queueService.getQueueByUserID(SessionApplication.getUserID());
        call.enqueue(new Callback<Queues>() {
            @Override
            public void onResponse(Call<Queues> queuesCall, Response<Queues> queuesResponse) {
                if(queuesResponse.isSuccessful()){
                    isExist[0] = true;
                    Queues queues = queuesResponse.body();

                    /** Api call to retrieve the details of the station by station ID**/
                    Retrofit retrofit2 = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()+"Station/").addConverterFactory(GsonConverterFactory.create()).build();
                    StationService stationService = retrofit2.create(StationService.class);
                    Call<Station> call2 = stationService.getStationDetails(queues.getStationID());
                    call2.enqueue(new Callback<Station>() {
                        @Override
                        public void onResponse(Call<Station> stationCall, Response<Station> stationResponse) {
                            if(stationResponse.isSuccessful()){
                                Station st = stationResponse.body();

                                /** Setting all the fetched data in to the textViews in the screen **/
                                StNameQ.setText(st.getStationName());
                                FuelTypeQ.setText(String.valueOf(queues.getFuelName()));
                                noQueue.setText(String.valueOf(st.getSuperDieselQueue()));
                                TimeQ.setText(String.valueOf(queues.getArrivalTime()));
                            }else{
                                Toast.makeText(ViewStations.this, "Unable to get station details", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Station> stationCall, Throwable t) {
                        }
                    });
                }else{
                    isExist[0] = false;
                }
            }
            @Override
            public void onFailure(Call<Queues> queuesCall, Throwable throwable) {

            }
        });
        return isExist[0];
    }

    private void initImageBitmaps() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
        StationService stationService = retrofit.create(StationService.class);
        Call<List<Station>> call = stationService.getAllStationDetails();
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(response.isSuccessful()) {
                    List<Station> stations = response.body();
                    Toast.makeText(ViewStations.this, String.valueOf(stations.size()), Toast.LENGTH_SHORT).show();
                    for (Station st: stations){
                        Station St01 = new Station(st.getId(), st.getOwnerID(), st.getOwnerName(),
                                st.getStationName(), st.getLocation(), st.getFuel(), st.getPetrolQueue(),
                                st.getSuperPetrolQueue(), st.getDieselQueue(), st.getSuperDieselQueue());
                        stationsList.add(St01);
                        locationList.add(st.getLocation());
                    }
                    listView = (ListView) findViewById(R.id.stationsListView);
                    StationListViewAdapter stationListViewAdapter = new StationListViewAdapter(getApplicationContext(), 0, stationsList);
                    listView.setAdapter(stationListViewAdapter);

                    listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Station selectStation = (Station) (listView.getItemAtPosition(position));
                            Intent showDetail = new Intent(getApplicationContext(), DetailStation.class);
                            showDetail.putExtra("id", selectStation.getId());
                            startActivity(showDetail);

                            Toast.makeText(ViewStations.this, "You Clicked "+ selectStation.getId(), Toast.LENGTH_SHORT).show();

                        }
                    }));

                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {

            }
        });

    }

    
}