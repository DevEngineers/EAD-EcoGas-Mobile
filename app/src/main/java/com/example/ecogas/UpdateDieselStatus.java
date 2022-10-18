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

public class UpdateDieselStatus extends AppCompatActivity {
    Button btnDateDD, btnTimeDT, btnUpdateDiesel;
    TextView dDate,dTime,editTextD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diesel_status);


        btnDateDD = findViewById(R.id.datePickerD);
        btnTimeDT = findViewById(R.id.timePickerD);
        dDate = findViewById(R.id.editTextDD);
        dTime = findViewById(R.id.editTextDT);
        btnUpdateDiesel = findViewById(R.id.updateDiesel);
        editTextD = findViewById(R.id.editTextD);

        btnDateDD.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);

                dDate.setText(new StringBuilder()
                        .append(month + 1).append("-").append(day).append("-")
                        .append(year).append(" "));
            };

            new DatePickerDialog(UpdateDieselStatus.this,date,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnTimeDT.setOnClickListener(view -> new TimePickerDialog(UpdateDieselStatus.this, (tp, sHour, sMinute) -> dTime.setText(new StringBuilder()
                .append(sHour).append(":").append(sMinute).append(" ")), Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show());


        btnUpdateDiesel.setOnClickListener(view -> {
            Fuel fuel = new Fuel();
            fuel.setArrivalDate(dDate.getText().toString().trim());
            fuel.setArrivalTime(dTime.getText().toString().trim());
            fuel.setCapacity(editTextD.getText().toString().trim());

            Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
            StationService stationService = retrofit.create(StationService.class);
            Call<Fuel> call = stationService.updateFuelStatus(SessionApplication.getStationID(),fuel); //Pass Station ID as id
            call.enqueue(new Callback<Fuel>() {
                @Override
                public void onResponse(@NonNull Call<Fuel> call, @NonNull Response<Fuel> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(UpdateDieselStatus.this, response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Fuel> call, @NonNull Throwable t) {
                    Toast.makeText(UpdateDieselStatus.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}