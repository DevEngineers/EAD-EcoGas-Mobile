package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StationOwnerHome extends AppCompatActivity {
    Button btnUpdateP,btnUpdateSP,btnUpdateD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owner_home);

        btnUpdateP=findViewById(R.id.updateP);
        btnUpdateSP=findViewById(R.id.updateSuperP);
        btnUpdateD=findViewById(R.id.updateD);

        btnUpdateP.setOnClickListener(v -> {
            /*Redirecting to update petrol capacity from via Intent**/
            Intent intent = new Intent(StationOwnerHome.this, UpdatePetrolStatus.class);
            startActivity(intent);
        });

        btnUpdateSP.setOnClickListener(v -> {
                /*Redirecting to update super petrol capacity from via Intent**/
                Intent intent = new Intent(StationOwnerHome.this, UpdateSuperPetrolStatus.class);
                startActivity(intent);
        });

        btnUpdateD.setOnClickListener(v -> {
                /*Redirecting to update diesel capacity from via Intent**/
                Intent intent = new Intent(StationOwnerHome.this, UpdateDieselStatus.class);
                startActivity(intent);
        });
    }
}