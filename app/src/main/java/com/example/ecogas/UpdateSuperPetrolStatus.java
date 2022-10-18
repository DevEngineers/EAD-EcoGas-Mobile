package com.example.ecogas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecogas.Model.Fuel;
import com.example.ecogas.Service.StationService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateSuperPetrolStatus extends AppCompatActivity {
    Button btnDateP95, btnTimeP95, btnUpdateSP;
    TextView p95Date,p95Time, editTextP95;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_super_petrol_status);

        btnDateP95 = findViewById(R.id.datePickerP95);
        btnTimeP95 = findViewById(R.id.timePickerP95);
        p95Date = findViewById(R.id.editTextP95D);
        p95Time = findViewById(R.id.editTextP95T);
        btnUpdateSP = findViewById(R.id.updateSuperPetrol);
        editTextP95 = findViewById(R.id.editTextP95);

        btnDateP95.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);

                p95Date.setText(new StringBuilder()
                        .append(month + 1).append("-").append(day).append("-")
                        .append(year).append(" "));
            };

            new DatePickerDialog(UpdateSuperPetrolStatus.this,date,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnTimeP95.setOnClickListener(view -> new TimePickerDialog(UpdateSuperPetrolStatus.this, (tp, sHour, sMinute) -> p95Time.setText(new StringBuilder()
                .append(sHour).append(":").append(sMinute).append(" ")), Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show());

        btnUpdateSP.setOnClickListener(view -> {
            Fuel fuel = new Fuel();
            fuel.setArrivalDate(p95Date.getText().toString().trim());
            fuel.setArrivalTime(p95Time.getText().toString().trim());
            fuel.setCapacity(editTextP95.getText().toString().trim());

            Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
            StationService stationService = retrofit.create(StationService.class);
            Call<Fuel> call = stationService.updateFuelStatus(SessionApplication.getStationID(),fuel); //Pass Station ID as id
            call.enqueue(new Callback<Fuel>() {
                @Override
                public void onResponse(@NonNull Call<Fuel> call, @NonNull Response<Fuel> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(UpdateSuperPetrolStatus.this, response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Fuel> call, @NonNull Throwable t) {
                    Toast.makeText(UpdateSuperPetrolStatus.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}