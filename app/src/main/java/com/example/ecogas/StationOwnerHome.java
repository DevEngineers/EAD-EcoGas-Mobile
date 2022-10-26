package com.example.ecogas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

/**
 * This is StationOwnerHome screen java file to provide logic activity_station_owner_home layout xml
 * This screen is to display the station details to owner and give option to navigate to update fuel status details
 *
 * Author: IT19153414 Akeel M.N.M
 */

public class StationOwnerHome extends AppCompatActivity {
    Button btnUpdateP;
    TextView name,stName,petrol,superPetrol,diesel,superDiesel,location,pQ,psQ,dQ,sdQ,pArrival,psArrival,dArrival,sdArrival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_home);

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
        Intent intent = new Intent(StationOwnerHome.this, MainActivity.class);
        startActivity(intent);
    }

    private void editProfile() {
        /** Redirecting to edit profile via Intent **/
        Intent intent = new Intent(StationOwnerHome.this, EditProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /** check user is log in**/
        if(SessionApplication.getUserName().equals("")){
            Intent intent = new Intent(StationOwnerHome.this,MainActivity.class);
            startActivity(intent);
        }

    }


}