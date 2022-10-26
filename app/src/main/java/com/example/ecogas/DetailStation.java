package com.example.ecogas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Model.Queues;
import com.example.ecogas.Model.Station;
import com.example.ecogas.Service.QueueService;
import com.example.ecogas.Service.StationService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is View Full Station details and add to queue screen java file to provide logic station_detail_layout layout xml
 * This Screen is add the queue in a station by the user
 * @Author: IT19175126 Zumry A.M
 */

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /** Api call to retrieve the details of the station fuel details **/
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()+"Station/").addConverterFactory(GsonConverterFactory.create()).build();
        StationService stationService = retrofit.create(StationService.class);
        Call<Station> call = stationService.getStationDetails(stID);
        call.enqueue(new Callback<Station>() {
            @Override
            public void onResponse(@NonNull Call<Station> call, @NonNull Response<Station> response) {
                if (response.isSuccessful()) {
                    Station station = response.body();

                    /** Setting all the fetched data in to the textViews in the screen **/
                    stationID = station.getId();
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
            /** Date Format */
            Date todayDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String date = format.format(todayDate);

            /** Time Format */
            SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
            String nowTime = formatTime.format(Calendar.getInstance().getTime());

            Queues queues = new Queues();
            queues.setStationID(stationID);
            queues.setUserID(SessionApplication.getUserID());
            queues.setFuelName(fuelName);
            queues.setArrivalDate(date);
            queues.setArrivalTime(nowTime);

            if(fuelName.equals("Select Fuel Type")){
                Toast.makeText(DetailStation.this,"Please select Fuel Type!!!", Toast.LENGTH_SHORT).show();
            }else{
                /** adding queue methods */
                Retrofit retrofitQ = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
                QueueService queueService = retrofitQ.create(QueueService.class);
                Call<Queues> callQ = queueService.addUserToQueue(queues);
                callQ.enqueue(new Callback<Queues>() {
                    @Override
                    public void onResponse(Call<Queues> callQ, Response<Queues> response) {
                        if (response.isSuccessful()) {

                            Fuel fuel = new Fuel();
                            fuel.setFuelName(queues.getFuelName());

                            /** call Increase queue count methods in Station */
                            Retrofit retrofit1 = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()+"Station/").addConverterFactory(GsonConverterFactory.create()).build();
                            StationService stationService1 = retrofit1.create(StationService.class);
                            Call<Station> call1 = stationService1.increaseFuelQueueCount(stationID, fuel);
                            call1.enqueue(new Callback<Station>() {
                                @Override
                                public void onResponse(Call<Station> call, Response<Station> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(DetailStation.this,"Joined Queue Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DetailStation.this,ViewStations.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(DetailStation.this,"Unable to joint the queue ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Station> call, Throwable t) {
                                }
                            });
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
