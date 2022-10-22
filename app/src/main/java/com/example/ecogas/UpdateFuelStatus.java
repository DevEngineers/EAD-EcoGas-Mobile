package com.example.ecogas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Model.Station;
import com.example.ecogas.Service.StationService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateFuelStatus extends AppCompatActivity {
    Button btnUpdate;
    TextView editTextAD,editTextAT,editTextFuelC, fuelNameView;
    String fuelID,fuelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_fuel_status);

        editTextAD = findViewById(R.id.editTextAD);
        editTextAT = findViewById(R.id.editTextAT);
        btnUpdate = findViewById(R.id.updateFuel);
        editTextFuelC = findViewById(R.id.editTextFuelC);
        fuelNameView = findViewById(R.id.fuelNameView);

        Bundle bundle = getIntent().getExtras();
        fuelID = bundle.getString("FUEL_ID");
        fuelName = bundle.getString("FUEL_NAME");
        fuelNameView.setText(String.valueOf(fuelName));

        editTextAD.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);

                editTextAD.setText(new StringBuilder()
                        .append(month + 1).append("-").append(day).append("-")
                        .append(year).append(" "));
            };

            new DatePickerDialog(UpdateFuelStatus.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        editTextAT.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener time = (view12, hourOfDay, minute) -> {
                if(hourOfDay > 12) {
                    editTextAT.setText(new StringBuilder().append((hourOfDay - 12)).append(":").append(minute).append(" PM"));
                } else if(hourOfDay == 12) {
                    editTextAT.setText(new StringBuilder().append("12:").append(minute).append(" PM"));
                } else if(hourOfDay < 12) {
                    if(hourOfDay!=0) {
                        editTextAT.setText(new StringBuilder().append((hourOfDay)).append(":").append(minute).append(" AM"));
                    } else {
                        editTextAT.setText(new StringBuilder().append("12:").append(minute).append(" AM"));
                    }
                }
            };

            new TimePickerDialog(UpdateFuelStatus.this,time,Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show();

        });

        btnUpdate.setOnClickListener(view -> {
            Fuel fuel = new Fuel();
            fuel.setId(fuelID);
            fuel.setFuelName(fuelName);
            fuel.setArrivalDate(editTextAD.getText().toString().trim());
            fuel.setArrivalTime(editTextAT.getText().toString().trim());
            fuel.setCapacity(editTextFuelC.getText().toString().trim());

            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.5:29193/Station/").addConverterFactory(GsonConverterFactory.create()).build();
            StationService stationService = retrofit.create(StationService.class);
            Call<Station> call = stationService.updateFuelStatus(SessionApplication.getStationID(),fuel); //Pass Station ID as id
            call.enqueue(new Callback<Station>() {
                @Override
                public void onResponse(@NonNull Call<Station> call, @NonNull Response<Station> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(UpdateFuelStatus.this,"Data Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateFuelStatus.this, StationOwnerHome.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Station> call, @NonNull Throwable t) {
                    Toast.makeText(UpdateFuelStatus.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}

