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

public class UpdateSuperDieselStatus extends AppCompatActivity {
    Button btnDateSDD, btnTimeSDT, btnUpdateSuperDiesel;
    TextView sdDate,sdTime,editTextSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_super_diesel_status);

        btnDateSDD = findViewById(R.id.datePickerSD);
        btnTimeSDT = findViewById(R.id.timePickerSD);
        sdDate = findViewById(R.id.editTextSDD);
        sdTime = findViewById(R.id.editTextSDT);
        btnUpdateSuperDiesel = findViewById(R.id.updateSuperDiesel);
        editTextSD = findViewById(R.id.editTextSD);

        btnDateSDD.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);

                sdDate.setText(new StringBuilder()
                        .append(month + 1).append("-").append(day).append("-")
                        .append(year).append(" "));
            };

            new DatePickerDialog(UpdateSuperDieselStatus.this,date,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnTimeSDT.setOnClickListener(view -> new TimePickerDialog(UpdateSuperDieselStatus.this, (tp, sHour, sMinute) -> sdTime.setText(new StringBuilder()
                .append(sHour).append(":").append(sMinute).append(" ")), Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show());


        btnUpdateSuperDiesel.setOnClickListener(view -> {
            Fuel fuel = new Fuel();
            fuel.setArrivalDate(sdDate.getText().toString().trim());
            fuel.setArrivalTime(sdTime.getText().toString().trim());
            fuel.setCapacity(editTextSD.getText().toString().trim());

            Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
            StationService stationService = retrofit.create(StationService.class);
            Call<Fuel> call = stationService.updateFuelStatus(SessionApplication.getStationID(),fuel); //Pass Station ID as id
            call.enqueue(new Callback<Fuel>() {
                @Override
                public void onResponse(@NonNull Call<Fuel> call, @NonNull Response<Fuel> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(UpdateSuperDieselStatus.this, response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Fuel> call, @NonNull Throwable t) {
                    Toast.makeText(UpdateSuperDieselStatus.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}