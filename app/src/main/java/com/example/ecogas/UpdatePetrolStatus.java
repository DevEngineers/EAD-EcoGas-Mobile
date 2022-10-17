package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import java.util.Calendar;

public class UpdatePetrolStatus extends AppCompatActivity {
    Button btnDateP92, btnTimeP92;
    TextView p92Date,p92Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_petrol_status);

        btnDateP92 = findViewById(R.id.datePickerP92);
        btnTimeP92 = findViewById(R.id.timePickerP92);
        p92Date = findViewById(R.id.editTextP92D);
        p92Time = findViewById(R.id.editTextP92T);


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

    }
}

