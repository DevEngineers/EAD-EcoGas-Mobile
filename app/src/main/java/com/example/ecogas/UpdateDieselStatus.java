package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import java.util.Calendar;

public class UpdateDieselStatus extends AppCompatActivity {
    Button btnDateDD, btnTimeDT;
    TextView dDate,dTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_diesel_status);


        btnDateDD = findViewById(R.id.datePickerD);
        btnTimeDT = findViewById(R.id.timePickerD);
        dDate = findViewById(R.id.editTextDD);
        dTime = findViewById(R.id.editTextDT);

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
    }
}