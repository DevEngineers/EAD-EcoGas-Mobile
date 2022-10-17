package com.example.ecogas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class UpdateSuperPetrolStatus extends AppCompatActivity {
    Button btnDateP95, btnTimeP95;
    TextView p95Date,p95Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_super_petrol_status);

        btnDateP95 = findViewById(R.id.datePickerP95);
        btnTimeP95 = findViewById(R.id.timePickerP95);
        p95Date = findViewById(R.id.editTextP95D);
        p95Time = findViewById(R.id.editTextP95T);

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
    }
}