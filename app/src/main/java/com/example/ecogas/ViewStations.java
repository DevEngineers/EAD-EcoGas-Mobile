package com.example.ecogas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ecogas.Model.Fuel;
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
 * @Author: IT19175126 Zumry A.M
 */

public class ViewStations extends AppCompatActivity {

    ArrayList<Station> stationsList = new ArrayList<Station>();
    List<String> locationList = new ArrayList<String>();
    String selectedLocation, queueID, stationID, fuelName;
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
        checkUserJoinedQueue();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
        StationService stationService = retrofit.create(StationService.class);
        Call<List<Station>> call = stationService.getAllStationDetails();
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(response.isSuccessful()) {
                    List<Station> stations = response.body();
                    locationList.add("Select Location");
                    for (Station station: stations){
                        locationList.add(String.valueOf(station.getLocation()));
                    }

                    /** Setting data to view fuel types in spinner for select fuel type **/
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewStations.this, android.R.layout.simple_spinner_item, locationList);
                    arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                    fuelLocationSpinner.setAdapter(arrayAdapter);

                }
            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) { }
        });

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
            }
        }));


        /** Listener to capture the selected data on spinner **/
        fuelLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSearch.setOnClickListener(view -> {

            if(selectedLocation.equals("Select Location")){
                Toast.makeText(ViewStations.this, "Please Select a Location", Toast.LENGTH_SHORT).show();
            }else {
                /** Api call to retrieve the details of the station by Location **/
                Retrofit retrofitS = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl() + "Station/").addConverterFactory(GsonConverterFactory.create()).build();
                StationService stationServiceS = retrofitS.create(StationService.class);
                Call<List<Station>> callS = stationServiceS.getStationByLocation(selectedLocation);
                callS.enqueue(new Callback<List<Station>>() {
                    @Override
                    public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                        if(response.isSuccessful()) {
                            stationsList.removeAll(stationsList);
                            List<Station> stations = response.body();
                            for (Station st: stations){
                                Station St01 = new Station(st.getId(), st.getOwnerID(), st.getOwnerName(),
                                        st.getStationName(), st.getLocation(), st.getFuel(), st.getPetrolQueue(),
                                        st.getSuperPetrolQueue(), st.getDieselQueue(), st.getSuperDieselQueue());
                                stationsList.add(St01);
                            }
                            stationListViewAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Station>> call, Throwable t) {

                    }
                });
            }
        });


        /** Api call to Remove Queue **/
        btnRemoveQueue.setOnClickListener(v -> {
            Retrofit retrofitR = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
            QueueService queueService = retrofitR.create(QueueService.class);
            Call<Queues> callR = queueService.removeUserInQueue(queueID);
            callR.enqueue(new Callback<Queues>() {
                @Override
                public void onResponse(Call<Queues> call, Response<Queues> response) {
                    if(response.isSuccessful()){
                        joinedQueueLayout.setVisibility(View.GONE);

                        Fuel fuel = new Fuel();
                        fuel.setFuelName(fuelName);

                        /** call Increase queue count methods in Station */
                        Retrofit retrofit1 = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()+"Station/").addConverterFactory(GsonConverterFactory.create()).build();
                        StationService stationService1 = retrofit1.create(StationService.class);
                        Call<Station> call1 = stationService1.decreaseFuelQueueCount(stationID,fuel);
                        call1.enqueue(new Callback<Station>() {
                            @Override
                            public void onResponse(Call<Station> call, Response<Station> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(ViewStations.this,"Removed from the Queue Successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ViewStations.this,"Unable to Remove from the queue ", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Station> call, Throwable t) {
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<Queues> call, Throwable t) {
                }
            });

        });
    }

    private void checkUserJoinedQueue() {

        /** Api call to retrieve the details of the Queue by User ID**/
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()+"Queue/").addConverterFactory(GsonConverterFactory.create()).build();
        QueueService queueService = retrofit.create(QueueService.class);
        Call<Queues> call = queueService.getQueueByUserID(SessionApplication.getUserID());
        call.enqueue(new Callback<Queues>() {
            @Override
            public void onResponse(Call<Queues> queuesCall, Response<Queues> queuesResponse) {
                if(queuesResponse.isSuccessful()){
                    joinedQueueLayout.setVisibility(View.VISIBLE);
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
                                TimeQ.setText(String.valueOf(queues.getArrivalTime()));
                                queueID = queues.getId();
                                stationID = st.getId();
                                fuelName = queues.getFuelName();

                                if(queues.getFuelName().equals("Petrol")){
                                    noQueue.setText(String.valueOf(st.getPetrolQueue()));
                                }else if(queues.getFuelName().equals("SuperPetrol")){
                                    noQueue.setText(String.valueOf(st.getSuperPetrolQueue()));
                                } else if(queues.getFuelName().equals("Diesel")){
                                    noQueue.setText(String.valueOf(st.getDieselQueue()));
                                }else if(queues.getFuelName().equals("SuperDiesel")){
                                    noQueue.setText(String.valueOf(st.getSuperDieselQueue()));
                                }

                            }else{
                                Toast.makeText(ViewStations.this, "Unable to get Queue details", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Station> stationCall, Throwable t) { }
                    });
                }else{
                    joinedQueueLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<Queues> queuesCall, Throwable throwable) { }
        });
    }

    /** Api call to retrieve the all details of the station **/
    private void initImageBitmaps() {

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /** Menu bar actions**/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                editProfile();
                return true;
            case R.id.action_logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        SessionApplication.setUserID("");
        SessionApplication.setUserName("");
        SessionApplication.setUserType("");
        SessionApplication.setStationID("");

        /** Redirecting to login screen after logout via Intent **/
        Intent intent = new Intent(ViewStations.this, MainActivity.class);
        startActivity(intent);
    }

    private void editProfile() {
        /** Redirecting to edit profile via Intent **/
        Intent intent = new Intent(ViewStations.this, EditProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /** check user is log in**/
        if(SessionApplication.getUserName().equals("")){
            Intent intent = new Intent(ViewStations.this,MainActivity.class);
            startActivity(intent);
        }

    }


}