package com.example.ecogas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecogas.Model.Queues;
import com.example.ecogas.Model.Station;
import com.example.ecogas.Service.QueueService;
import com.example.ecogas.Service.StationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailStation extends AppCompatActivity {

    String stID;
    Button btnAddQueue;
    List<String> fuelType = Arrays.asList("Select Fuel Type","Petrol","SuperPetrol","Diesel","SuperDiesel");
    String fuelName;
    String stationID;
    TextView StName,StLocation,StPetrol92A,StPetrol95A,StDieselA,StSDieselA,StPetrol92Q,StPetrol95Q,StDieselQ,StSDieselQ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_detail_layout);

        btnAddQueue = findViewById(R.id.addQueueBtn);
        StName = (TextView) findViewById(R.id.stNameText);
        StLocation = (TextView) findViewById(R.id.stLocationText);
        StPetrol92A = (TextView) findViewById(R.id.stPetrol92TextA);
        StPetrol95A = (TextView) findViewById(R.id.stPetrol95TextA);
        StDieselA = (TextView) findViewById(R.id.stDieselTextA);
        StSDieselA = (TextView) findViewById(R.id.stSDieselTextA);
        StPetrol92Q = (TextView) findViewById(R.id.stPetrol92TextQ);
        StPetrol95Q = (TextView) findViewById(R.id.stPetrol95TextQ);
        StDieselQ = (TextView) findViewById(R.id.stDieselTextQ);
        StSDieselQ = (TextView) findViewById(R.id.stSDieselTextQ);

        Bundle bundle = getIntent().getExtras();
        stID = bundle.getString("id");

        Spinner fuelNameSpinner = (Spinner) findViewById(R.id.spinnerFuelType);

        /** Setting data to view fuel types in spinner for select fuel type **/
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DetailStation.this, android.R.layout.simple_spinner_item, fuelType);
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        fuelNameSpinner.setAdapter(arrayAdapter);

        /** Listener to capture the selected data on spinner **/
        fuelNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fuelName = adapterView.getSelectedItem().toString();
                Log.d("TAG","=========------------------==============.........." + fuelName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.8.100:29193/Station/").addConverterFactory(GsonConverterFactory.create()).build();
        StationService stationService = retrofit.create(StationService.class);
        Call<Station> call = stationService.getStationDetails(stID);
        call.enqueue(new Callback<Station>() {
            @Override
            public void onResponse(@NonNull Call<Station> call, @NonNull Response<Station> response) {
                if (response.isSuccessful()) {
                    Station station = response.body();

                    station.setId(stationID);
                    StName.setText(station.getStationName());
                    StLocation.setText(station.getLocation());
                    StPetrol92A.setText(station.getFuel().get(0).getCapacity());
                    StPetrol95A.setText(station.getFuel().get(1).getCapacity());
                    StDieselA.setText(station.getFuel().get(2).getCapacity());
                    StSDieselA.setText(station.getFuel().get(3).getCapacity());
                    StPetrol92Q.setText(String.valueOf(station.getPetrolQueue()));
                    StPetrol95Q.setText(String.valueOf(station.getSuperPetrolQueue()));
                    StDieselQ.setText(String.valueOf(station.getDieselQueue()));
                    StSDieselQ.setText(String.valueOf(station.getSuperDieselQueue()));

                }
            }

            @Override
            public void onFailure(Call<Station> call, Throwable t) {

            }

        });


        btnAddQueue.setOnClickListener(view -> {
            Queues queues = new Queues();
            queues.setId(stationID);
            queues.setUserID(SessionApplication.getUserID());
            queues.setFuelName(fuelName);
            queues.setArrivalDate(String.valueOf(Calendar.getInstance()));
            queues.setArrivalTime(String.valueOf(Calendar.getInstance().getTime()));

            Log.d("TAG" , "----------------============Fuel name-------- " +queues.getFuelName() );
            Log.d("TAG" , "----------------============ST ID-------- " +queues.getId());

            if(fuelName==null){
                Toast.makeText(DetailStation.this,"Please select Fuel Type!!!", Toast.LENGTH_SHORT).show();
            }else{
                Retrofit retrofitQ = new Retrofit.Builder().baseUrl("http://192.168.8.100:29193/Queue/").addConverterFactory(GsonConverterFactory.create()).build();
                QueueService queueService = retrofitQ.create(QueueService.class);
                Call<Queues> callQ = queueService.addUserToQueue(queues);
                callQ.enqueue(new Callback<Queues>() {
                    @Override
                    public void onResponse(Call<Queues> callQ, Response<Queues> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DetailStation.this,"Joined Queue Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Queues> callQ, Throwable t) {

                    }
                });
            }
        });


    }



}
