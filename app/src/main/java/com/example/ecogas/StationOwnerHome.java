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
    Button btnUpdateP,btnUpdateSP,btnUpdateD;
    TextView name,stName,petrol,superPetrol,diesel,location,pQ,psQ,dQ,pArrival,psArrival,dArrival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_home);

        btnUpdateP=findViewById(R.id.updateP);
        btnUpdateSP=findViewById(R.id.updateSuperP);
        btnUpdateD=findViewById(R.id.updateD);

        name = findViewById(R.id.ownerName);
        stName = findViewById(R.id.stName);
        petrol = findViewById(R.id.petrol92);
        superPetrol = findViewById(R.id.petrol95);
        diesel = findViewById(R.id.diesel);
        pQ = findViewById(R.id.petrol92Q);
        psQ = findViewById(R.id.petrol95Q);
        dQ = findViewById(R.id.dieselQ);
        pArrival = findViewById(R.id.petrol92Arrival);
        psArrival = findViewById(R.id.petrol95Arrival);
        dArrival = findViewById(R.id.dieselArrival);
        location = findViewById(R.id.location);

        btnUpdateP.setOnClickListener(v -> {
            /*Redirecting to update petrol capacity from via Intent**/
            Intent intent = new Intent(StationOwnerHome.this, UpdatePetrolStatus.class);
            startActivity(intent);
        });

        btnUpdateSP.setOnClickListener(v -> {
            /*Redirecting to update super petrol capacity from via Intent**/
            Intent intent = new Intent(StationOwnerHome.this, UpdateSuperPetrolStatus.class);
            startActivity(intent);
        });

        btnUpdateD.setOnClickListener(v -> {
            /*Redirecting to update diesel capacity from via Intent**/
            Intent intent = new Intent(StationOwnerHome.this, UpdateDieselStatus.class);
            startActivity(intent);
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        StationService stationService = retrofit.create(StationService.class);
        Call<Station> call = stationService.getStationDetails();
        call.enqueue(new Callback<Station>() {
            @Override
            public void onResponse(@NonNull Call<Station> call, @NonNull Response<Station> response) {
                if(response.isSuccessful()){
                    Station station = response.body();

                    name.setText(station.getOwnerName());
                    stName.setText(station.getStationName());
                    petrol.setText(station.getPetrol().getCapacity());
                    superPetrol .setText(station.getSuperPetrol().getCapacity());
                    diesel.setText(station.getDiesel().getCapacity());
                    location.setText(station.getLocation());
                    pQ.setText(station.getPetrolQueue());
                    psQ.setText(station.getSuperPetrolQueue());
                    dQ.setText(station.getDieselQueue());
                    pArrival.setText(new StringBuilder().append(station.getPetrol().getArrivalDate()).append(" ").append(station.getPetrol().getArrivalTime()));
                    psArrival.setText(new StringBuilder().append(station.getSuperPetrol().getArrivalDate()).append(" ").append(station.getSuperPetrol().getArrivalTime()));
                    dArrival.setText(new StringBuilder().append(station.getDiesel().getArrivalDate()).append(" ").append(station.getDiesel().getArrivalTime()));

                }else{
                    Toast.makeText(StationOwnerHome.this, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Station> call, @NonNull Throwable t) {
                Toast.makeText(StationOwnerHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}