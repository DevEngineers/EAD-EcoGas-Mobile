package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Model.Station;
import com.example.ecogas.Model.User;
import com.example.ecogas.Service.DBMaster;
import com.example.ecogas.Service.StationService;
import com.example.ecogas.Service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationOwnerRegister extends AppCompatActivity {
    EditText name,userName, password, reTypePassword, location,stationName;
    Button signUp;
    DBMaster DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.stationOwnername);
        userName = (EditText) findViewById(R.id.shedOwnerUsername);
        password = (EditText) findViewById(R.id.shedOwnerPassword);
        reTypePassword = (EditText) findViewById(R.id.shedOwnerepassword);
        signUp = (Button) findViewById(R.id.shedOwnerbtnsignup);
        location = findViewById(R.id.shedLocation);
        stationName = findViewById(R.id.shedName);
        DB = new DBMaster(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shedOwnerPassword = password.getText().toString();
                String shedOwnerTypePassword = reTypePassword.getText().toString();

                User shedOwner = new User();
                shedOwner.setUserName(userName.getText().toString());
                shedOwner.setName(name.getText().toString());
                shedOwner.setType("StationOwner");

                if (shedOwner.getUserName().equals("") || shedOwnerPassword.equals("") || shedOwnerTypePassword.equals(""))
                    Toast.makeText(StationOwnerRegister.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
                    UserService userService = retrofit.create(UserService.class);
                    Call<User> call = userService.createUser(shedOwner);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                User createdUser=response.body();
                                createNewStation(createdUser.getId(),location.getText().toString().trim(),createdUser.getName(),stationName.getText().toString().trim());
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }

            private void createNewStation(String ownerID, String location,String name,String stationName) {
                ArrayList<Fuel> fuel = new ArrayList<Fuel>();
                fuel.add(new Fuel("Petrol","0","", ""));
                fuel.add(new Fuel("SuperPetrol","0","", ""));
                fuel.add(new Fuel("Diesel","0","", ""));
                fuel.add(new Fuel("SuperDiesel","0","", ""));


                Station station = new Station();
                station.setStationName(stationName);
                station.setOwnerID(ownerID);
                station.setOwnerName(name);
                station.setFuel(fuel);
                station.setLocation(location);
                station.setDieselQueue(0);
                station.setPetrolQueue(0);
                station.setSuperPetrolQueue(0);
                station.setSuperDieselQueue(0);

                Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
                StationService stationService = retrofit.create(StationService.class);
                Call<Station> call = stationService.createNewStation(station);
                call.enqueue(new Callback<Station>() {
                    @Override
                    public void onResponse(Call<Station> call, Response<Station> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(StationOwnerRegister.this, "Station Owner Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StationOwnerRegister.this,AdminHome.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Station> call, Throwable t) {

                    }
                });
            }
        });
    }
}