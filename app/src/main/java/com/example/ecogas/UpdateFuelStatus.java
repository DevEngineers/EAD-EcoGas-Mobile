package com.example.ecogas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Model.Station;
import com.example.ecogas.Service.StationService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateFuelStatus extends AppCompatActivity {
    Button btnUpdate;
    TextView editTextAD,editTextAT,editTextFuelC;
    String fuelName;
    List<String> fuelType = Arrays.asList("Select Fuel Type","Petrol","SuperPetrol","Diesel","SuperDiesel");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_fuel_status);

        editTextAD = findViewById(R.id.editTextAD);
        editTextAT = findViewById(R.id.editTextAT);
        btnUpdate = findViewById(R.id.updateFuel);
        editTextFuelC = findViewById(R.id.editTextFuelC);
        Spinner fuelNameSpinner = (Spinner) findViewById(R.id.fuelSelectSpinner);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UpdateFuelStatus.this, android.R.layout.simple_spinner_item, fuelType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelNameSpinner.setAdapter(arrayAdapter);


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
                } else {
                    if(hourOfDay!=0) {
                        editTextAT.setText(new StringBuilder().append((hourOfDay)).append(":").append(minute).append(" AM"));
                    } else {
                        editTextAT.setText(new StringBuilder().append("12:").append(minute).append(" AM"));
                    }
                }
            };

            new TimePickerDialog(UpdateFuelStatus.this,time,Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show();

        });

        fuelNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fuelName = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnUpdate.setOnClickListener(view -> {
            Fuel fuel = new Fuel();
            fuel.setFuelName(fuelName);
            fuel.setArrivalDate(editTextAD.getText().toString().trim());
            fuel.setArrivalTime(editTextAT.getText().toString().trim());
            fuel.setCapacity(editTextFuelC.getText().toString().trim());

            if(fuelName.isEmpty() || fuelName.equals("Select Fuel Type")){
                Toast.makeText(UpdateFuelStatus.this,"Select Fuel Type.. ",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(editTextFuelC.getText()) || fuel.getCapacity().equals("0")){
                Toast.makeText(UpdateFuelStatus.this,"Enter Fuel Capacity.. ",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(editTextAD.getText())){
                Toast.makeText(UpdateFuelStatus.this,"Select Arrival Date.. ",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(editTextAT.getText())){
                Toast.makeText(UpdateFuelStatus.this,"Select Arrival Time.. ",Toast.LENGTH_SHORT).show();
            }
            else {
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
            }
        });

    }
}

