package com.example.ecogas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecogas.Model.Station;
import com.example.ecogas.Service.StationService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationOwnerHome extends AppCompatActivity {
    Button btnUpdateP;
    TextView name,stName,petrol,superPetrol,diesel,superDiesel,location,pQ,psQ,dQ,sdQ,pArrival,psArrival,dArrival,sdArrival;
    String pID,p95ID,dID,sdID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnUpdateP=findViewById(R.id.updateP);

        name = findViewById(R.id.ownerName);
        stName = findViewById(R.id.stName);
        petrol = findViewById(R.id.petrol92);
        superPetrol = findViewById(R.id.petrol95);
        diesel = findViewById(R.id.diesel);
        superDiesel = findViewById(R.id.superDiesel);
        pQ = findViewById(R.id.petrol92Q);
        psQ = findViewById(R.id.petrol95Q);
        dQ = findViewById(R.id.dieselQ);
        sdQ = findViewById(R.id.superDieselQ);
        pArrival = findViewById(R.id.petrol92Arrival);
        psArrival = findViewById(R.id.petrol95Arrival);
        dArrival = findViewById(R.id.dieselArrival);
        sdArrival = findViewById(R.id.superDieselArrival);
        location = findViewById(R.id.location);

        btnUpdateP.setOnClickListener(v -> {
            /** Redirecting to update fuel status via Intent **/
            Intent intent = new Intent(StationOwnerHome.this, UpdateFuelStatus.class);
            intent.putExtra("FUEL_ID", pID);
            intent.putExtra("FUEL_NAME","Petrol");
            startActivity(intent);
        });

        /** Api call to retrieve all the details of the station **/
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl() + "Station/").addConverterFactory(GsonConverterFactory.create()).build();
        StationService stationService = retrofit.create(StationService.class);
        Call<Station> call = stationService.getStationByOwnerID(SessionApplication.getUserID());
        call.enqueue(new Callback<Station>() {
            @Override
            public void onResponse(@NonNull Call<Station> call, @NonNull Response<Station> response) {
                if(response.isSuccessful()){
                    Station station = response.body();

                    /** Setting all the fetched data in to the textViews in the screen **/
                    SessionApplication.setStationID(String.valueOf(station.getId()));
                    name.setText(String.valueOf(station.getOwnerName()));
                    stName.setText(String.valueOf(station.getStationName()));
                    petrol.setText(String.valueOf(station.getFuel().get(0).getCapacity()));
                    superPetrol .setText(String.valueOf(station.getFuel().get(1).getCapacity()));
                    diesel.setText(String.valueOf(station.getFuel().get(2).getCapacity()));
                    superDiesel.setText(String.valueOf(station.getFuel().get(3).getCapacity()));
                    location.setText(String.valueOf(station.getLocation()));
                    pQ.setText(String.valueOf(station.getPetrolQueue()));
                    psQ.setText(String.valueOf(station.getSuperPetrolQueue()));
                    dQ.setText(String.valueOf(station.getDieselQueue()));
                    sdQ.setText(String.valueOf(station.getSuperDieselQueue()));
                    sdArrival.setText(String.valueOf(response.body().getSuperDieselQueue()));
                    pArrival.setText(new StringBuilder().append(station.getFuel().get(0).getArrivalDate()).append(" ").append(station.getFuel().get(0).getArrivalTime()));
                    psArrival.setText(new StringBuilder().append(station.getFuel().get(1).getArrivalDate()).append(" ").append(station.getFuel().get(1).getArrivalTime()));
                    dArrival.setText(new StringBuilder().append(station.getFuel().get(2).getArrivalDate()).append(" ").append(station.getFuel().get(2).getArrivalTime()));
                    sdArrival.setText(new StringBuilder().append(station.getFuel().get(3).getArrivalDate()).append(" ").append(station.getFuel().get(3).getArrivalTime()));

                    pID = station.getFuel().get(0).getId();
                    p95ID = station.getFuel().get(1).getId();
                    dID = station.getFuel().get(2).getId();
                    sdID = station.getFuel().get(3).getId();
                }
                else{
                    Toast.makeText(StationOwnerHome.this, "Unable to get station details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Station> call, @NonNull Throwable t) {
                Toast.makeText(StationOwnerHome.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}