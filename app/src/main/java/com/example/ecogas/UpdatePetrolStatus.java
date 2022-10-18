package com.example.ecogas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Service.StationService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdatePetrolStatus extends AppCompatActivity {
    Button btnDateP92, btnTimeP92,btnUpdatePetrol;
    TextView p92Date,p92Time,editTextP92;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_petrol_status);

        btnDateP92 = findViewById(R.id.datePickerP92);
        btnTimeP92 = findViewById(R.id.timePickerP92);
        p92Date = findViewById(R.id.editTextP92D);
        p92Time = findViewById(R.id.editTextP92T);
        btnUpdatePetrol = findViewById(R.id.updatePetrol);
        editTextP92 = findViewById(R.id.editTextP92);


        btnDateP92.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);

                p92Date.setText(new StringBuilder()
                        .append(month + 1).append("-").append(day).append("-")
                        .append(year).append(" "));
            };

            new DatePickerDialog(UpdatePetrolStatus.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnTimeP92.setOnClickListener(view -> new TimePickerDialog(UpdatePetrolStatus.this, (tp, sHour, sMinute) -> p92Time.setText(new StringBuilder()
                .append(sHour).append(":").append(sMinute).append(" ")), Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show());

        btnUpdatePetrol.setOnClickListener(view -> {
            Fuel fuel = new Fuel();
            fuel.setArrivalDate(p92Date.getText().toString().trim());
            fuel.setArrivalTime(p92Time.getText().toString().trim());
            fuel.setCapacity(editTextP92.getText().toString().trim());

            Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
            StationService stationService = retrofit.create(StationService.class);
            Call<Fuel> call = stationService.updateFuelStatus(SessionApplication.getStationID(),fuel); //Pass Station ID as id
            call.enqueue(new Callback<Fuel>() {
                @Override
                public void onResponse(@NonNull Call<Fuel> call, @NonNull Response<Fuel> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(UpdatePetrolStatus.this, response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Fuel> call, @NonNull Throwable t) {
                    Toast.makeText(UpdatePetrolStatus.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}

