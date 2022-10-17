package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StationOwnerHome extends AppCompatActivity {
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_home);

        btnUpdate=findViewById(R.id.updateState);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**Redirecting to update fuel from via Intent**/
                Intent intent = new Intent(StationOwnerHome.this,UpdateFuelStatus.class);
                startActivity(intent);
            }
        });
    }
}